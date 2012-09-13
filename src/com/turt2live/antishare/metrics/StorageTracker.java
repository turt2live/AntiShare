/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
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
