package com.turt2live.antishare.cuboid;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.manager.CuboidManager.CuboidPoint;

public class Cuboid implements Cloneable, ConfigurationSerializable {

	private Location minimum, maximum, point1, point2;
	private String worldName;

	/**
	 * Creates a new cuboid
	 * 
	 * @param l1 the first location
	 * @param l2 the second location
	 */
	public Cuboid(Location l1, Location l2){
		this.point1 = l1.clone();
		this.point2 = l2.clone();
		calculate();
	}

	/**
	 * Creates a blank Cuboid
	 */
	public Cuboid(){}

	/**
	 * Determines if a location is inside this cuboid
	 * 
	 * @param l the location to test
	 * @return true if contained
	 */
	public boolean isContained(Location l){
		if(!isValid()){
			return false;
		}
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
		if(!isValid()){
			return false;
		}
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
		return minimum == null ? null : minimum.clone();
	}

	/**
	 * Gets the largest possible coordinate in this cuboid
	 * 
	 * @return the largest coordinate
	 */
	public Location getMaximumPoint(){
		return maximum == null ? null : maximum.clone();
	}

	/**
	 * Sets new points for this region
	 * 
	 * @param l1 the first point
	 * @param l2 the second point
	 */
	public void setPoints(Location l1, Location l2){
		this.point1 = l1 != null ? l1.clone() : null;
		this.point2 = l2 != null ? l2.clone() : null;
		if(this.worldName == null && (l1 != null || l2 != null)){
			worldName = (l1 != null ? l1 : l2).getWorld().getName();
		}
		setWorld(getWorld());
		calculate();
	}

	/**
	 * Gets the volume of the region
	 * 
	 * @return the volume
	 */
	public int getVolume(){
		if(!isValid()){
			return 0;
		}
		int w = maximum.getBlockX() - minimum.getBlockX();
		int d = maximum.getBlockZ() - minimum.getBlockZ();
		int h = maximum.getBlockY() - minimum.getBlockY();
		return (w <= 0 ? 1 : w) * (d <= 0 ? 1 : d) * (h <= 0 ? 1 : h);
	}

	/**
	 * Updates the cuboid with a new world
	 * 
	 * @param world the new world
	 */
	public void setWorld(World world){
		this.worldName = world.getName();
		if(!isValid()){
			return;
		}
		if(minimum != null)
			minimum.setWorld(world);
		if(maximum != null)
			maximum.setWorld(world);
		if(point1 != null)
			point1.setWorld(world);
		if(point2 != null)
			point2.setWorld(world);
	}

	/**
	 * Sets a specific point in this cuboid
	 * 
	 * @param point the point
	 * @param value the value
	 */
	public void setPoint(CuboidPoint point, Location value){
		switch (point){
		case POINT1:
			this.point1 = value.clone();
			break;
		case POINT2:
			this.point2 = value.clone();
			break;
		}
		if(!isValid()){
			return;
		}
		calculate();
	}

	/**
	 * Gets the world applied to this cuboid
	 * 
	 * @return the world applied
	 */
	public World getWorld(){
		return worldName == null ? null : Bukkit.getWorld(worldName);
	}

	/**
	 * Determines if this cuboid is valid
	 * 
	 * @return true if valid
	 */
	public boolean isValid(){
		return worldName != null && point1 != null && point2 != null;
	}

	private void calculate(){
		if(!isValid()){
			return;
		}
		int mix = 0, miy = 0, miz = 0, max = 0, may = 0, maz = 0;
		if(!point1.getWorld().getName().equals(point2.getWorld().getName())){
			throw new IllegalArgumentException("Worlds not equal.");
		}
		this.worldName = point1.getWorld().getName();
		World world = getWorld();
		mix = point1.getBlockX() < point2.getBlockX() ? point1.getBlockX() : point2.getBlockX();
		miy = point1.getBlockY() < point2.getBlockY() ? point1.getBlockY() : point2.getBlockY();
		miz = point1.getBlockZ() < point2.getBlockZ() ? point1.getBlockZ() : point2.getBlockZ();
		max = point1.getBlockX() > point2.getBlockX() ? point1.getBlockX() : point2.getBlockX();
		may = point1.getBlockY() > point2.getBlockY() ? point1.getBlockY() : point2.getBlockY();
		maz = point1.getBlockZ() > point2.getBlockZ() ? point1.getBlockZ() : point2.getBlockZ();
		minimum = new Location(world, mix, miy, miz);
		maximum = new Location(world, max, may, maz);
	}

	public static Cuboid deserialize(Map<String, Object> map){
		String world = (String) map.get("world");
		int mix = (Integer) map.get("minimum X"), miy = (Integer) map.get("minimum Y"), miz = (Integer) map.get("minimum Z"), max = (Integer) map.get("maximum X"), may = (Integer) map.get("maximum Y"), maz = (Integer) map.get("maximum Z");
		if(world == null){
			throw new IllegalArgumentException("World not found: " + world);
		}
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
		map.put("world", worldName);
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
		Cuboid cuboid = new Cuboid();
		cuboid.setPoints(point1 != null ? point1.clone() : null, point2 != null ? point2.clone() : null);
		if(worldName != null){
			cuboid.setWorld(Bukkit.getWorld(worldName));
		}
		return cuboid;
	}

}
