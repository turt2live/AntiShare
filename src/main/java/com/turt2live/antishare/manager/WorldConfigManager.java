/*******************************************************************************
 * Copyright (c) 2013 Travis Ralston.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.manager;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import com.turt2live.antishare.config.PerWorldConfig;

/**
 * Handles world configurations
 * 
 * @author turt2live
 */
public class WorldConfigManager implements Listener {

	private HashMap<String, PerWorldConfig> config = new HashMap<String, PerWorldConfig>();

	/**
	 * Loads all world configurations
	 */
	public void load(){
		config.clear();
		for(World world : Bukkit.getWorlds()){
			config.put(world.getName(), PerWorldConfig.getConfig(world.getName()));
		}
	}

	/**
	 * Reloads the configuration manager
	 */
	public void reload(){
		load();
	}

	/**
	 * Gets the configuration for the world
	 * 
	 * @param world the world
	 * @return the configuration
	 */
	public PerWorldConfig getConfig(String world){
		if(!config.containsKey(world)){
			config.put(world, PerWorldConfig.getConfig(world));
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
		config.put(event.getWorld().getName(), PerWorldConfig.getConfig(event.getWorld().getName()));
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
