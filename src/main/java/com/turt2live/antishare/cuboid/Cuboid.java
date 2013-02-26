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

/**
 * An AntiShare cuboid
 * 
 * @author turt2live
 */
public class Cuboid implements Cloneable, ConfigurationSerializable {

	protected Location minimum, maximum, point1, point2;
	protected String worldName;

	/**
	 * Creates a new cuboid
	 * 
	 * @param location1 the first location
	 * @param location2 the second location
	 */
	public Cuboid(Location location1, Location location2){
		this.point1 = location1.clone();
		this.point2 = location2.clone();
		calculate();
	}

	/**
	 * Creates a blank Cuboid
	 */
	public Cuboid(){}

	/**
	 * Determines if a location is inside this cuboid
	 * 
	 * @param location the location to test
	 * @return true if contained
	 */
	public boolean isContained(Location location){
		if(!isValid()){
			return false;
		}
		if(location.getWorld().getName().equals(minimum.getWorld().getName())){
			if((location.getBlockX() >= minimum.getBlockX() && location.getBlockX() <= maximum.getBlockX()) && (location.getBlockY() >= minimum.getBlockY() && location.getBlockY() <= maximum.getBlockY()) && (location.getBlockZ() >= minimum.getBlockZ() && location.getBlockZ() <= maximum.getBlockZ())){
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
		if(cuboid.getMaximumPoint().getBlockX() < getMinimumPoint().getBlockX() || cuboid.getMinimumPoint().getBlockX() > getMaximumPoint().getBlockX()){
			return false;
		}else if(cuboid.getMaximumPoint().getBlockZ() < getMinimumPoint().getBlockZ() || cuboid.getMinimumPoint().getBlockZ() > getMaximumPoint().getBlockZ()){
			return false;
		}else if(cuboid.getMaximumPoint().getBlockY() < getMinimumPoint().getBlockY() || cuboid.getMinimumPoint().getBlockY() > getMaximumPoint().getBlockY()){
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
	 * @param location1 the first point
	 * @param location2 the second point
	 */
	public void setPoints(Location location1, Location location2){
		this.point1 = location1 != null ? location1.clone() : null;
		this.point2 = location2 != null ? location2.clone() : null;
		if(this.worldName == null && (location1 != null || location2 != null)){
			worldName = (location1 != null ? location1 : location2).getWorld().getName();
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
		if(minimum != null){
			minimum.setWorld(world);
		}
		if(maximum != null){
			maximum.setWorld(world);
		}
		if(point1 != null){
			point1.setWorld(world);
		}
		if(point2 != null){
			point2.setWorld(world);
		}
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
		int minX = 0, minY = 0, minZ = 0, maxX = 0, maxY = 0, maxZ = 0;
		if(!point1.getWorld().getName().equals(point2.getWorld().getName())){
			throw new IllegalArgumentException("Worlds not equal.");
		}
		this.worldName = point1.getWorld().getName();
		World world = getWorld();
		minX = point1.getBlockX() < point2.getBlockX() ? point1.getBlockX() : point2.getBlockX();
		minY = point1.getBlockY() < point2.getBlockY() ? point1.getBlockY() : point2.getBlockY();
		minZ = point1.getBlockZ() < point2.getBlockZ() ? point1.getBlockZ() : point2.getBlockZ();
		maxX = point1.getBlockX() > point2.getBlockX() ? point1.getBlockX() : point2.getBlockX();
		maxY = point1.getBlockY() > point2.getBlockY() ? point1.getBlockY() : point2.getBlockY();
		maxZ = point1.getBlockZ() > point2.getBlockZ() ? point1.getBlockZ() : point2.getBlockZ();
		minimum = new Location(world, minX, minY, minZ);
		maximum = new Location(world, maxX, maxY, maxZ);
	}

	/**
	 * Attempts to deserialize the cuboid from a map
	 * 
	 * @param map the map
	 * @return the cuboid
	 * @throws IllegalArgumentException if the map is invalid in any way
	 */
	public static Cuboid deserialize(Map<String, Object> map){
		String world = (String) map.get("world");
		int mix = (Integer) map.get("minimum X"), miy = (Integer) map.get("minimum Y"), miz = (Integer) map.get("minimum Z"), max = (Integer) map.get("maximum X"), may = (Integer) map.get("maximum Y"), maz = (Integer) map.get("maximum Z");
		if(world == null){
			throw new IllegalArgumentException("World not found: " + world);
		}
		World matching = AntiShare.p.getServer().getWorld(world);
		if(matching == null){
			throw new IllegalArgumentException("World not found: " + world);
		}
		Location mi = new Location(matching, mix, miy, miz);
		Location ma = new Location(matching, max, may, maz);
		Cuboid cuboid = new Cuboid(mi, ma);
		cuboid.setWorld(matching);
		return cuboid;
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

	/**
	 * Gets the first point
	 * 
	 * @return the first point
	 */
	public Location getPoint1(){
		return point1.clone();
	}

	/**
	 * Gets the second point
	 * 
	 * @return the second point
	 */
	public Location getPoint2(){
		return point2.clone();
	}

}
