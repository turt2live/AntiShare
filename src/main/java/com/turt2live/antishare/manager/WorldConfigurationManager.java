package com.turt2live.antishare.manager;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import com.turt2live.antishare.regions.PerWorldConfig;

/**
 * Handles world configurations
 * 
 * @author turt2live
 */
public class WorldConfigurationManager extends AntiShareManager implements Listener {

	private HashMap<String, PerWorldConfig> config = new HashMap<String, PerWorldConfig>();

	@Override
	public boolean load(){
		config.clear();
		for(World world : Bukkit.getWorlds()){
			config.put(world.getName(), new PerWorldConfig(world.getName()));
		}
		return true;
	}

	@Override
	public boolean save(){
		return true;
	}

	/**
	 * Gets the configuration for the world
	 * 
	 * @param world the world
	 * @return the configuration
	 */
	public PerWorldConfig getConfig(String world){
		if(!config.containsKey(world)){
			config.put(world, new PerWorldConfig(world));
		}
		return config.get(world);
	}

	/**
	 * Gets the configuration for the world
	 * 
	 * @param world the world
	 * @return the configuration
	 */
	public PerWorldConfig getConfig(World world){
		return getConfig(world.getName());
	}

	@EventHandler
	public void onWorldLoad(WorldLoadEvent event){
		config.put(event.getWorld().getName(), new PerWorldConfig(event.getWorld().getName()));
	}

	@EventHandler
	public void onWorldUnload(WorldUnloadEvent event){
		if(event.isCancelled()){
			return;
		}
		World world = event.getWorld();
		config.remove(world.getName());
	}

}
