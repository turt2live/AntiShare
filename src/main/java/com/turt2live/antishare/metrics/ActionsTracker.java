package com.turt2live.antishare.metrics;

import com.turt2live.antishare.metrics.TrackerList.TrackerType;

public class ActionsTracker extends Tracker {

	private String graphName;

	public ActionsTracker(String name, TrackerType type, String graphName){
		super(name, type);
		this.graphName = graphName;
	}

	@Override
	public String getGraphName(){
		return graphName;
	}

}
