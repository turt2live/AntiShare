package com.turt2live.antishare.cuboid;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.turt2live.antishare.regions.Region;

public class RegionCuboid extends Cuboid {

	public static final RegionCuboid NO_REGION_CUBOID = new RegionCuboid(null);

	private Region region;

	/**
	 * Creates a new region cuboid
	 * 
	 * @param l1 the first location
	 * @param l2 the second location
	 * @param region the owning region
	 */
	public RegionCuboid(Region region, Location l1, Location l2){
		super(l1, l2);
		this.region = region;
	}

	/**
	 * Creates a blank region cuboid
	 * 
	 * @param region the owning region
	 */
	public RegionCuboid(Region region){
		super();
		this.region = region;
	}

	/**
	 * Gets the region this cuboid is for
	 * 
	 * @return the owning region
	 */
	public Region getRegion(){
		return region;
	}

	/**
	 * Determines if this cuboid has a region
	 * 
	 * @return true if a valid region is attached
	 */
	public boolean hasRegion(){
		return region == null;
	}

	/**
	 * Sets this cuboid's region
	 * 
	 * @param region the region
	 */
	public void setRegion(Region region){
		this.region = region;
	}

	/**
	 * Converts a cuboid to a region cuboid
	 * 
	 * @param cuboid the cuboid the cuboid
	 * @param region the region to assign
	 * @return the new region cuboid
	 */
	public static RegionCuboid fromCuboid(Cuboid cuboid, Region region){
		if(cuboid instanceof RegionCuboid){
			RegionCuboid rcuboid = (RegionCuboid) cuboid.clone();
			rcuboid.setRegion(region);
			return rcuboid;
		}
		RegionCuboid rcuboid = new RegionCuboid(region);
		rcuboid.setPoints(cuboid.point1, cuboid.point2);
		rcuboid.setWorld(cuboid.getWorld());
		return rcuboid;
	}

	@Override
	public RegionCuboid clone(){
		RegionCuboid cuboid = new RegionCuboid(region);
		cuboid.setPoints(point1 != null ? point1.clone() : null, point2 != null ? point2.clone() : null);
		if(worldName != null){
			cuboid.setWorld(Bukkit.getWorld(worldName));
		}
		return cuboid;
	}

}
