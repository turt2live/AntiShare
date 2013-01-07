package com.turt2live.antishare.cuboid;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.feildmaster.lib.configuration.EnhancedConfiguration;

/**
 * Cuboid manager
 */
public class CuboidManager {

	/**
	 * A cuboid point
	 */
	public static enum CuboidPoint{
		POINT1, POINT2;
	}

	private AntiShare plugin = AntiShare.getInstance();
	private Map<String, Cuboid> cuboids = new HashMap<String, Cuboid>();

	/**
	 * Creates a new cuboid manager
	 */
	public CuboidManager(){
		load();
	}

	/**
	 * Gets the cuboid for a player
	 * 
	 * @param player the player
	 * @return the cuboid, or null if not found
	 */
	public Cuboid getCuboid(String player){
		return cuboids.containsKey(player) ? cuboids.get(player).clone() : null;
	}

	/**
	 * Determines if the cuboid in the manager is complete
	 * 
	 * @param player the player
	 * @return true if valid and complete
	 */
	public boolean isCuboidComplete(String player){
		Cuboid c = getCuboid(player);
		if(c != null){
			return c.isValid();
		}
		return false;
	}

	/**
	 * Updates a cuboid
	 * 
	 * @param player the player
	 * @param point the point
	 * @param value the value
	 */
	public void updateCuboid(String player, CuboidPoint point, Location value){
		Cuboid c = getCuboid(player);
		if(c == null){
			c = new Cuboid();
		}
		c.setPoint(point, value);
		c.setWorld(value.getWorld());
		cuboids.put(player, c.clone());
	}

	/**
	 * Saves all the cuboids to disk for loading later
	 */
	public void save(){
		File file = new File(plugin.getDataFolder(), "data" + File.separator + "cuboids.yml");
		if(file.exists()){
			file.delete();
		}
		EnhancedConfiguration config = new EnhancedConfiguration(file, plugin);
		config.load();
		for(String player : cuboids.keySet()){
			Cuboid cuboid = cuboids.get(player);
			config.set(player, cuboid);
		}
		config.save();
		cuboids.clear();
	}

	/**
	 * Loads all cuboids
	 */
	public void load(){
		File file = new File(plugin.getDataFolder(), "data" + File.separator + "cuboids.yml");
		if(!file.exists()){
			return;
		}
		EnhancedConfiguration config = new EnhancedConfiguration(file, plugin);
		config.load();
		for(String player : config.getKeys(false)){
			Cuboid cuboid = (Cuboid) config.get(player);
			cuboids.put(player, cuboid);
		}
		if(cuboids.keySet().size() > 0){
			plugin.getLogger().info("Cuboids Loaded: " + cuboids.keySet().size());
		}
	}

	/**
	 * Reloads the cuboid manager
	 */
	public void reload(){
		save();
		load();
	}

	/**
	 * Removes a player's cuboid
	 * 
	 * @param name the player name
	 */
	public void removeCuboid(String name){
		cuboids.remove(name);
	}

}
