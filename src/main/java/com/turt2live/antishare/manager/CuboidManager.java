package com.turt2live.antishare.manager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.cuboid.Cuboid;
import com.turt2live.antishare.lang.LocaleMessage;
import com.turt2live.antishare.lang.Localization;

/**
 * Cuboid manager
 */
public class CuboidManager extends AntiShareManager {

	/**
	 * A cuboid point
	 */
	public static enum CuboidPoint{
		POINT1,
		POINT2;
	}

	private final Map<String, Cuboid> cuboids = new HashMap<String, Cuboid>();

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
		Cuboid cuboid = getCuboid(player);
		if(cuboid != null){
			return cuboid.isValid();
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
		Cuboid cuboid = getCuboid(player);
		if(cuboid == null){
			cuboid = new Cuboid();
		}
		cuboid.setPoint(point, value);
		cuboid.setWorld(value.getWorld());
		cuboids.put(player, cuboid.clone());
	}

	/**
	 * Saves all the cuboids to disk for loading later
	 */
	@Override
	public boolean save(){
		File file = new File(plugin.getDataFolder(), "data" + File.separator + "cuboids.yml");
		if(file.exists()){
			file.delete();
		}
		EnhancedConfiguration yamlFile = new EnhancedConfiguration(file, plugin);
		yamlFile.load();
		for(String player : cuboids.keySet()){
			Cuboid cuboid = cuboids.get(player);
			yamlFile.set(player, cuboid);
		}
		yamlFile.save();
		cuboids.clear();
		return true;
	}

	/**
	 * Loads all cuboids
	 */
	@Override
	public boolean load(){
		File file = new File(plugin.getDataFolder(), "data" + File.separator + "cuboids.yml");
		if(!file.exists()){
			return true;
		}
		EnhancedConfiguration yamlFile = new EnhancedConfiguration(file, plugin);
		yamlFile.load();
		for(String player : yamlFile.getKeys(false)){
			Cuboid cuboid = (Cuboid) yamlFile.get(player);
			cuboids.put(player, cuboid);
		}
		if(cuboids.keySet().size() > 0){
			plugin.getLogger().info(Localization.getMessage(LocaleMessage.STATUS_CUBOIDS, String.valueOf(cuboids.keySet().size())));
		}
		return true;
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
