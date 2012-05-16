package com.turt2live.antishare;

import org.bukkit.Material;

/**
 * An enum to represent the maximum amount of data values in an item
 * 
 * @author turt2live
 */
public enum MaxDataValue{

	WOOL(15);

	// TODO: The rest

	private int max;

	private MaxDataValue(int max){
		this.max = max;
	}

	/**
	 * Gets the maximum number of data values for this item
	 * 
	 * @return the maximum number of data values
	 */
	public int max(){
		return max;
	}

	/**
	 * Gets the maximum from a material
	 * 
	 * @param material the material
	 * @return the maximum
	 */
	public static int getMax(Material material){
		switch (material){
		case WOOL:
			return MaxDataValue.WOOL.max();
		}
		return 0;
	}

}
