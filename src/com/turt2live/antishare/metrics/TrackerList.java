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
	private Tracker startupTracker;

	/**
	 * Creates a new Tracker List
	 */
	public TrackerList(){
		startupTracker = new Tracker("Startup", TrackerType.STARTUP);
		add(startupTracker);
	}

	/**
	 * Gets the startup tracker
	 * 
	 * @return the startup tracker
	 */
	public Tracker getStartupTracker(){
		return startupTracker;
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
