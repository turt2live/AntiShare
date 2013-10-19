/*******************************************************************************
 * Copyright (c) 2013 Travis Ralston.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.util;

import com.turt2live.antishare.regions.Region;

/**
 * Used to determine what is blocked inside the listener
 * 
 * @author turt2live
 */
public class ProtectionInformation {

	public final boolean illegal, isRegion;
	public final Region sourceRegion, targetRegion;

	public ProtectionInformation(boolean illegal, boolean isRegion, Region source, Region target) {
		this.illegal = illegal;
		this.isRegion = isRegion;
		this.sourceRegion = source;
		this.targetRegion = target;
	}

}
