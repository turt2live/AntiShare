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
package com.turt2live.antishare.cuboid;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.regions.Region;

/**
 * RegionCuboid - simple class to better tell what cuboid AntiShare is passing around
 * 
 * @author turt2live
 */
public class RegionCuboid extends Cuboid {

	public static final RegionCuboid NO_REGION_CUBOID = new RegionCuboid(null);

	private Region region;

	/**
	 * Creates a new region cuboid
	 * 
	 * @param location1 the first location
	 * @param location2 the second location
	 * @param region the owning region
	 */
	public RegionCuboid(Region region, Location location1, Location location2) {
		super(location1, location2);
		this.region = region;
	}

	/**
	 * Creates a blank region cuboid
	 * 
	 * @param region the owning region
	 */
	public RegionCuboid(Region region) {
		super();
		this.region = region;
	}

	/**
	 * Gets the region this cuboid is for
	 * 
	 * @return the owning region
	 */
	public Region getRegion() {
		return region;
	}

	/**
	 * Determines if this cuboid has a region
	 * 
	 * @return true if a valid region is attached
	 */
	public boolean hasRegion() {
		return region == null;
	}

	/**
	 * Sets this cuboid's region
	 * 
	 * @param region the region
	 */
	public void setRegion(Region region) {
		this.region = region;
	}

	/**
	 * Converts a cuboid to a region cuboid
	 * 
	 * @param cuboid the cuboid the cuboid
	 * @param region the region to assign
	 * @return the new region cuboid
	 */
	public static RegionCuboid fromCuboid(Cuboid cuboid, Region region) {
		if (cuboid == null || region == null) {
			throw new IllegalArgumentException("Null arguments are bad!");
		}
		if (cuboid instanceof RegionCuboid) {
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
	public RegionCuboid clone() {
		RegionCuboid cuboid = new RegionCuboid(region);
		cuboid.setPoints(point1 != null ? point1.clone() : null, point2 != null ? point2.clone() : null);
		if (worldName != null) {
			cuboid.setWorld(Bukkit.getWorld(worldName));
		}
		return cuboid;
	}

	/**
	 * Attempts to deserialize the cuboid from a map
	 * 
	 * @param map the map
	 * @return the cuboid
	 * @throws IllegalArgumentException if the map is invalid in any way
	 */
	public static Cuboid deserialize(Map<String, Object> map) {
		String world = (String) map.get("world");
		int mix = (Integer) map.get("minimum X"), miy = (Integer) map.get("minimum Y"), miz = (Integer) map.get("minimum Z"), max = (Integer) map.get("maximum X"), may = (Integer) map.get("maximum Y"), maz = (Integer) map.get("maximum Z");
		if (world == null) {
			throw new IllegalArgumentException("World not found: " + world);
		}
		World matching = AntiShare.p.getServer().getWorld(world);
		if (matching == null) {
			throw new IllegalArgumentException("World not found: " + world);
		}
		Location mi = new Location(matching, mix, miy, miz);
		Location ma = new Location(matching, max, may, maz);
		Cuboid cuboid = new Cuboid(mi, ma);
		cuboid.setWorld(matching);
		return cuboid;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("world", worldName);
		map.put("minimum X", minimum.getBlockX());
		map.put("minimum Y", minimum.getBlockY());
		map.put("minimum Z", minimum.getBlockZ());
		map.put("maximum X", maximum.getBlockX());
		map.put("maximum Y", maximum.getBlockY());
		map.put("maximum Z", maximum.getBlockZ());
		map.put("region-cuboid", true);
		return Collections.unmodifiableMap(map);
	}

}
