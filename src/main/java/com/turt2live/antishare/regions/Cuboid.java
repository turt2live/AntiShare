package com.turt2live.antishare.regions;

import org.bukkit.Location;
import org.bukkit.World;

public class Cuboid {

	private Location minimum, maximum;

	/**
	 * Creates a new cuboid
	 * 
	 * @param l1 the first location
	 * @param l2 the second location
	 */
	public Cuboid(Location l1, Location l2){
		this.minimum = l1;
		this.maximum = l2;
		calculate();
	}

	/**
	 * Determines if a location is inside this cuboid
	 * 
	 * @param l the location to test
	 * @return true if contained
	 */
	public boolean isContained(Location l){
		if(l.getWorld().getName().equals(minimum.getWorld().getName())){
			if((l.getBlockX() >= minimum.getBlockX() && l.getBlockX() <= maximum.getBlockX())
					&& (l.getBlockY() >= minimum.getBlockY() && l.getBlockY() <= maximum.getBlockY())
					&& (l.getBlockZ() >= minimum.getBlockZ() && l.getBlockZ() <= maximum.getBlockZ())){
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines if a cuboid is overlapping another
	 * 
	 * @param cuboid the other cuboid
	 * @return true if overlapping
	 */
	public boolean isOverlapping(Cuboid cuboid){
		// Thanks to Sleaker for letting me use this code :D
		// Modified from: https://github.com/MilkBowl/LocalShops/blob/master/src/net/milkbowl/localshops/ShopManager.java#L216
		if(cuboid.getMaximumPoint().getBlockX() < getMinimumPoint().getBlockX()
				|| cuboid.getMinimumPoint().getBlockX() > getMaximumPoint().getBlockX()){
			return false;
		}else if(cuboid.getMaximumPoint().getBlockZ() < getMinimumPoint().getBlockZ()
				|| cuboid.getMinimumPoint().getBlockZ() > getMaximumPoint().getBlockZ()){
			return false;
		}else if(cuboid.getMaximumPoint().getBlockY() < getMinimumPoint().getBlockY()
				|| cuboid.getMinimumPoint().getBlockY() > getMaximumPoint().getBlockY()){
			return false;
		}else{
			return true; // All 3 planes meet, therefore regions are in contact
		}
	}

	/**
	 * Gets the smallest possible coordinate in this cuboid
	 * 
	 * @return the smallest coordinate
	 */
	public Location getMinimumPoint(){
		return minimum;
	}

	/**
	 * Gets the largest possible coordinate in this cuboid
	 * 
	 * @return the largest coordinate
	 */
	public Location getMaximumPoint(){
		return maximum;
	}

	/**
	 * Sets new points for this region
	 * 
	 * @param l1 the first point
	 * @param l2 the second point
	 */
	public void setPoints(Location l1, Location l2){
		this.minimum = l1;
		this.maximum = l2;
		calculate();
	}

	private void calculate(){
		int mix = 0, miy = 0, miz = 0, max = 0, may = 0, maz = 0;
		if(!minimum.getWorld().getName().equals(maximum.getWorld().getName())){
			throw new IllegalArgumentException("Worlds not equal.");
		}
		World world = minimum.getWorld();
		mix = minimum.getBlockX() < maximum.getBlockX() ? minimum.getBlockX() : maximum.getBlockX();
		miy = minimum.getBlockY() < maximum.getBlockY() ? minimum.getBlockY() : maximum.getBlockY();
		miz = minimum.getBlockZ() < maximum.getBlockZ() ? minimum.getBlockZ() : maximum.getBlockZ();
		max = minimum.getBlockX() > maximum.getBlockX() ? minimum.getBlockX() : maximum.getBlockX();
		may = minimum.getBlockY() > maximum.getBlockY() ? minimum.getBlockY() : maximum.getBlockY();
		maz = minimum.getBlockZ() > maximum.getBlockZ() ? minimum.getBlockZ() : maximum.getBlockZ();
		minimum = new Location(world, mix, miy, miz);
		maximum = new Location(world, max, may, maz);
	}

}
