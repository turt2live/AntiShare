package com.turt2live.antishare.metrics;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.metrics.TrackerList.TrackerType;
import com.turt2live.antishare.signs.Sign;

/**
 * Specific tracker for AntiShare signs
 * 
 * @author turt2live
 */
public class SignTracker extends Tracker {

	/**
	 * An enum to represent sign types
	 * 
	 * @author turt2live
	 */
	public static enum SignType{
		ENABLED,
		DISABLED,
		CASE_SENSITIVE,
		CASE_INSENSITIVE,
		ALL;
	}

	private AntiShare plugin;
	private SignType stype;

	/**
	 * Creates a new Sign Tracker
	 * 
	 * @param name the tracker name
	 * @param type the type
	 * @param signtype the Sign Type to track
	 */
	public SignTracker(String name, TrackerType type, SignType signtype){
		super(name, type);
		this.plugin = AntiShare.getInstance();
		this.stype = signtype;
	}

	@Override
	public int getValue(){
		switch (stype){
		case ENABLED:
			return plugin.getSignManager().getEnabledSigns().size();
		case DISABLED:
			return plugin.getSignManager().getDisabledSigns().size();
		case CASE_SENSITIVE:
			int s = 0;
			for(Sign sign : plugin.getSignManager()){
				if(sign.isCaseSensitive()){
					s++;
				}
			}
			return s;
		case CASE_INSENSITIVE:
			s = 0;
			for(Sign sign : plugin.getSignManager()){
				if(!sign.isCaseSensitive()){
					s++;
				}
			}
			return s;
		case ALL:
			return plugin.getSignManager().getAllSigns().size();
		}
		return 0;
	}

}
