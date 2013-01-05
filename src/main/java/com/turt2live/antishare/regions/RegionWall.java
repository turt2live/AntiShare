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
package com.turt2live.antishare.regions;

import org.bukkit.Location;

/**
 * Region wall
 * 
 * @author turt2live
 */
@Deprecated
public class RegionWall {

	/**
	 * An enum to represent a wall
	 * 
	 * @author turt2live
	 */
	public static enum Wall{
		NORTH,
		SOUTH,
		EAST,
		WEST,
		CEILING,
		FLOOR;
	}

	private Wall location;
	private Location point;

	/**
	 * Creates a new region wall
	 * 
	 * @param location the wall type
	 * @param point the point
	 */
	public RegionWall(Wall location, Location point){
		this.location = location;
		this.point = point;
	}

	/**
	 * Adds an amount to the wall
	 * 
	 * @param amount the amount to add (absolute)
	 * @return the new region wall
	 */
	public RegionWall add(double amount){
		amount = Math.abs(amount);
		switch (location){
		case NORTH:
			point.setX(point.getX() - (point.getX() < 0 ? amount * +1 : amount * -1));
			break;
		case SOUTH:
			point.setX(point.getX() + (point.getX() < 0 ? amount * +1 : amount * -1));
			break;
		case EAST:
			point.setZ(point.getZ() - (point.getZ() < 0 ? amount * +1 : amount * -1));
			break;
		case WEST:
			point.setZ(point.getZ() + (point.getZ() < 0 ? amount * +1 : amount * -1));
			break;
		case CEILING:
			point.setY(point.getY() - (point.getY() < 0 ? amount * +1 : amount * -1));
			break;
		case FLOOR:
			point.setY(point.getY() + (point.getY() < 0 ? amount * +1 : amount * -1));
			break;
		}
		return this;
	}

	/**
	 * Gets the point associated with the wall
	 * 
	 * @return the point
	 */
	public Location getPoint(){
		return point;
	}

	/**
	 * Gets the wall type
	 * 
	 * @return the wall type
	 */
	public Wall getType(){
		return location;
	}
}
