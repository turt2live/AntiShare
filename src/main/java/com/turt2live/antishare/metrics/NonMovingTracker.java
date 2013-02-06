package com.turt2live.antishare.metrics;

import com.turt2live.antishare.metrics.TrackerList.TrackerType;

public class NonMovingTracker extends Tracker {

	public NonMovingTracker(String name, TrackerType type){
		super(name, type);
		if(type == TrackerType.LOCALE){
			super.value = 1;
		}
	}

	@Override
	public void increment(int amount){
		super.value = 1;
	}

}
