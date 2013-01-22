/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.metrics;

import com.turt2live.antishare.metrics.Metrics.Plotter;
import com.turt2live.antishare.metrics.TrackerList.TrackerType;

/**
 * Tracker
 * 
 * @author turt2live
 */
public class Tracker extends Plotter {

	protected int value = 0;
	private String name = "UNKNOWN";
	private TrackerType type;

	/**
	 * Creates a new tracker
	 * 
	 * @param name the name
	 * @param type the type
	 */
	public Tracker(String name, TrackerType type){
		this.name = name;
		this.type = type;
	}

	/**
	 * Gets the tracker type
	 * 
	 * @return the type
	 */
	public TrackerType getType(){
		return type;
	}

	/**
	 * Increment the tracker
	 * 
	 * @param amount the amount to increment by
	 */
	public void increment(int amount){
		value += amount;
	}

	@Override
	public String getColumnName(){
		return name;
	}

	@Override
	public void reset(){
		value = 0;
	}

	@Override
	public int getValue(){
		return value;
	}

	public String getGraphName(){
		return type.getGraphName();
	}

}
