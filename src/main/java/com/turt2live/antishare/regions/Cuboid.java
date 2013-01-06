package com.turt2live.antishare.regions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.turt2live.antishare.AntiShare;

public class Cuboid implements Cloneable, ConfigurationSerializable {

	private Location minimum, maximum;

	/**
	 * Creates a new cuboid
	 * 
	 * @param l1 the first location
	 * @param l2 the second location
	 */
	public Cuboid(Location l1, Location l2){
		this.minimum = l1.clone();
		this.maximum = l2.clone();
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
		return minimum.clone();
	}

	/**
	 * Gets the largest possible coordinate in this cuboid
	 * 
	 * @return the largest coordinate
	 */
	public Location getMaximumPoint(){
		return maximum.clone();
	}

	/**
	 * Sets new points for this region
	 * 
	 * @param l1 the first point
	 * @param l2 the second point
	 */
	public void setPoints(Location l1, Location l2){
		this.minimum = l1.clone();
		this.maximum = l2.clone();
		calculate();
	}

	/**
	 * Gets the volume of the region
	 * 
	 * @return the volume
	 */
	public int getVolume(){
		int w = maximum.getBlockX() - minimum.getBlockX();
		int d = maximum.getBlockZ() - minimum.getBlockZ();
		int h = maximum.getBlockY() - minimum.getBlockY();
		return w * d * h;
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

	public static Cuboid deserialize(Map<String, Object> map){
		String world = (String) map.get("world");
		int mix = (Integer) map.get("minimum X"), miy = (Integer) map.get("minimum Y"), miz = (Integer) map.get("minimum Z"), max = (Integer) map.get("maximum X"), may = (Integer) map.get("maximum Y"), maz = (Integer) map.get("maximum Z");
		World matching = AntiShare.getInstance().getServer().getWorld(world);
		if(matching == null){
			throw new IllegalArgumentException("World not found: " + world);
		}
		Location mi = new Location(matching, mix, miy, miz);
		Location ma = new Location(matching, max, may, maz);
		return new Cuboid(mi, ma);
	}

	@Override
	public Map<String, Object> serialize(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("world", maximum.getWorld().getName());
		map.put("minimum X", minimum.getBlockX());
		map.put("minimum Y", minimum.getBlockY());
		map.put("minimum Z", minimum.getBlockZ());
		map.put("maximum X", maximum.getBlockX());
		map.put("maximum Y", maximum.getBlockY());
		map.put("maximum Z", maximum.getBlockZ());
		return Collections.unmodifiableMap(map);
	}

	@Override
	public Cuboid clone(){
		return new Cuboid(minimum.clone(), maximum.clone());
	}

}
