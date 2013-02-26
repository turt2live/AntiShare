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

	public ProtectionInformation(boolean illegal, boolean isRegion, Region source, Region target){
		this.illegal = illegal;
		this.isRegion = isRegion;
		this.sourceRegion = source;
		this.targetRegion = target;
	}

}
