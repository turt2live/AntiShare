package com.turt2live.antishare.metrics;

import java.util.ArrayList;

/**
 * Tracker list
 * 
 * @author turt2live
 */
public class TrackerList extends ArrayList<Tracker> {

	public static enum TrackerType{
		STARTUP;
	}

	private static final long serialVersionUID = 8386186678486064850L;

	/**
	 * Creates a new Tracker List
	 */
	public TrackerList(){
		add(new Tracker("Startup", TrackerType.STARTUP));
	}

	/**
	 * Gets a tracker
	 * 
	 * @return the tracker
	 */
	public Tracker getTracker(TrackerType type){
		for(int i = 0; i < size(); i++){
			Tracker t = get(i);
			if(t.getType() == type){
				return t;
			}
		}
		return null;
	}

	/**
	 * Adds all the trackers to the metrics
	 * 
	 * @param metrics the metrics
	 */
	public void addTo(Metrics metrics){
		for(int i = 0; i < size(); i++){
			metrics.addCustomData(get(i));
		}
	}

}
