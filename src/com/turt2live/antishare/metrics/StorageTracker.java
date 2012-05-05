package com.turt2live.antishare.metrics;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.metrics.TrackerList.TrackerType;

/**
 * Specific tracker for storage types
 * 
 * @author turt2live
 */
public class StorageTracker extends Tracker {

	private AntiShare plugin;

	/**
	 * Creates a new Storage Tracker
	 * 
	 * @param name the tracker name
	 * @param type the type
	 */
	public StorageTracker(String name, TrackerType type){
		super(name, type);
		this.plugin = AntiShare.getInstance();
	}

	@Override
	public int getValue(){
		if(getType() == TrackerType.SQL){
			return plugin.useSQL() ? 1 : 0;
		}else if(getType() == TrackerType.FLAT_FILE){
			return plugin.useSQL() ? 0 : 1;
		}
		return 0;
	}

}
