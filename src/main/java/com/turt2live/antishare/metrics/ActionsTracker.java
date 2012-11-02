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
