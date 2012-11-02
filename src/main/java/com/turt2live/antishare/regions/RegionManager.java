/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.regions;

import java.io.File;
import java.io.FileFilter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.util.SQL;
import com.turt2live.antishare.util.generic.Selection;

/**
 * Handles and managed ASRegions
 * 
 * @author turt2live
 */
public class RegionManager {

	private AntiShare plugin;
	private HashMap<World, Set<ASRegion>> regions = new HashMap<World, Set<ASRegion>>();
	private boolean hasWorldEdit = false;

	/**
	 * Creates a new region manager
	 */
	public RegionManager(){
		plugin = AntiShare.getInstance();
		hasWorldEdit = plugin.getServer().getPluginManager().getPlugin("WorldEdit") != null;
		load();
	}

	/**
	 * Reloads the manager
	 */
	public void reload(){
		if(!hasWorldEdit()){
			return;
		}
		save();
		regions.clear();
		load();
	}

	/**
	 * Loads regions
	 */
	private void load(){
		if(!hasWorldEdit()){
			return;
		}

		// Check file/folder
		File dir = new File(plugin.getDataFolder(), "data" + File.separator + "regions");
		dir.mkdirs();
		int loadedRegions = 0;

		if(plugin.useSQL()){
			// SQL load

			// Load
			try{
				ResultSet results = plugin.getSQL().get("SELECT * FROM `" + SQL.REGIONS_TABLE + "`");
				if(results != null){
					while (results.next()){
						World world = plugin.getServer().getWorld(results.getString("world"));
						Location minimum = new Location(world,
								results.getDouble("mix"),
								results.getDouble("miy"),
								results.getDouble("miz"));
						Location maximum = new Location(world,
								results.getDouble("max"),
								results.getDouble("may"),
								results.getDouble("maz"));
						String setBy = results.getString("creator");
						GameMode gamemode = GameMode.valueOf(results.getString("gamemode"));
						String name = results.getString("regionName");
						boolean enterMessage = results.getInt("showEnter") == 1;
						boolean exitMessage = results.getInt("showExit") == 1;
						ASRegion region = new ASRegion(world, minimum, maximum, setBy, gamemode, name);
						region.setUniqueID(results.getString("uniqueID"));
						region.setEnterMessage(results.getString("enterMessage"));
						region.setExitMessage(results.getString("exitMessage"));
						region.setMessageOptions(enterMessage, exitMessage);

						// Inventory is set when the inventory manager loads

						// Assign and save
						Set<ASRegion> set = regions.get(region.getWorld()) == null ? new HashSet<ASRegion>() : regions.get(region.getWorld());
						set.add(region);
						regions.put(region.getWorld(), set);
						loadedRegions++;
					}
				}
			}catch(SQLException e){
				AntiShare.getInstance().log("AntiShare encountered and error. Please report this to turt2live.", Level.SEVERE);
				e.printStackTrace();
			}
		}else{
			//Flat-File (YAML) load

			// Loop with filter
			if(dir.listFiles() != null){
				for(File file : dir.listFiles(new FileFilter(){
					@Override
					public boolean accept(File file){
						return file.getName().toLowerCase().endsWith(".yml");
					}
				})){
					// Load
					EnhancedConfiguration rfile = new EnhancedConfiguration(file, plugin);
					String uid = file.getName().replace(".yml", "");
					rfile.load();

					// Get objects
					World world = Bukkit.getWorld(rfile.getString("worldName"));
					Location min = new Location(world, rfile.getDouble("mi-x"), rfile.getDouble("mi-y"), rfile.getDouble("mi-z"));
					Location max = new Location(world, rfile.getDouble("ma-x"), rfile.getDouble("ma-y"), rfile.getDouble("ma-z"));
					ASRegion region = new ASRegion(world, min, max, rfile.getString("set-by"), GameMode.valueOf(rfile.getString("gamemode")), rfile.getString("name"));
					region.setUniqueID(uid);
					region.setEnterMessage(rfile.getString("enterMessage"));
					region.setExitMessage(rfile.getString("exitMessage"));
					region.setMessageOptions(rfile.getBoolean("showEnter"), rfile.getBoolean("showExit"));
					region.loadPlayerInformation();

					// Inventory is set when the inventory manager loads

					// Assign and save
					Set<ASRegion> set = regions.get(region.getWorld()) == null ? new HashSet<ASRegion>() : regions.get(region.getWorld());
					set.add(region);
					regions.put(region.getWorld(), set);
					loadedRegions++;
				}
			}
		}
		plugin.log("Regions Loaded: " + loadedRegions, Level.INFO);
	}

	/**
	 * Saves regions
	 */
	public void save(){
		if(!hasWorldEdit()){
			return;
		}

		// Save
		if(plugin.useSQL()){
			// SQL save

			// Cleanup
			plugin.getSQL().wipeTable(SQL.REGIONS_TABLE);

			// Save
			for(World world : Bukkit.getWorlds()){
				for(ASRegion region : getAllRegions(world)){
					plugin.getSQL().update("INSERT INTO `" + SQL.REGIONS_TABLE + "` (`regionName`, `mix`, `miy`, `miz`, `max`, `may`, `maz`, `creator`, `gamemode`, `showEnter`, `showExit`, `world`, `uniqueID`, `enterMessage`, `exitMessage`) VALUES ('" + region.getName() + "', '" + region.getSelection().getMinimumPoint().getBlockX() + "', '" + region.getSelection().getMinimumPoint().getBlockY() + "', '" + region.getSelection().getMinimumPoint().getBlockZ() + "', '" + region.getSelection().getMaximumPoint().getBlockX() + "', '" + region.getSelection().getMaximumPoint().getBlockY() + "', '" + region.getSelection().getMaximumPoint().getBlockZ() + "', '" + region.getWhoSet() + "', '" + region.getGameMode().name() + "', '" + (region.isEnterMessageActive() ? 1 : 0) + "', '" + (region.isExitMessageActive() ? 1 : 0) + "', '" + region.getWorld().getName() + "', '" + region.getUniqueID() + "', '" + region.getEnterMessage() + "', '" + region.getExitMessage() + "')");
				}
			}
		}else{
			// Flat-File (YAML) save

			// Check folders and cleanup
			File dir = new File(plugin.getDataFolder(), "data" + File.separator + "regions");
			if(dir.exists()){
				if(dir.listFiles() != null){
					for(File file : dir.listFiles()){
						file.delete();
					}
				}
			}
			dir.mkdirs();

			// Save
			for(World world : Bukkit.getWorlds()){
				for(ASRegion region : getAllRegions(world)){
					// Load file
					File file = new File(dir, region.getUniqueID() + ".yml");
					EnhancedConfiguration rfile = new EnhancedConfiguration(file, plugin);
					rfile.load();

					// Set keys
					rfile.set("worldName", world.getName());
					rfile.set("mi-x", region.getSelection().getMinimumPoint().getX());
					rfile.set("mi-y", region.getSelection().getMinimumPoint().getY());
					rfile.set("mi-z", region.getSelection().getMinimumPoint().getZ());
					rfile.set("ma-x", region.getSelection().getMaximumPoint().getX());
					rfile.set("ma-y", region.getSelection().getMaximumPoint().getY());
					rfile.set("ma-z", region.getSelection().getMaximumPoint().getZ());
					rfile.set("set-by", region.getWhoSet());
					rfile.set("gamemode", region.getGameMode().name());
					rfile.set("name", region.getName());
					rfile.set("showEnter", region.isEnterMessageActive());
					rfile.set("showExit", region.isExitMessageActive());
					rfile.set("enterMessage", region.getEnterMessage());
					rfile.set("exitMessage", region.getExitMessage());

					// Inventory is saved when the inventory manager saves

					// Save
					rfile.save();
					region.savePlayerInformation();
				}
			}
		}
	}

	/**
	 * Determines if the server has WorldEdit installed
	 * 
	 * @return true if WorldEdit was found
	 */
	public boolean hasWorldEdit(){
		return hasWorldEdit;
	}

	/**
	 * Determines if a location is within a region
	 * 
	 * @param location the location
	 * @return true if there is a location there
	 */
	public boolean isRegion(Location location){
		World world = location.getWorld();
		Set<ASRegion> regions = this.regions.get(world);
		if(regions == null){
			return false;
		}
		for(ASRegion region : regions){
			if(region.has(location)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the region for a location
	 * 
	 * @param location the location
	 * @return the region, or null if no region
	 */
	public ASRegion getRegion(Location location){
		World world = location.getWorld();
		Set<ASRegion> regions = this.regions.get(world);
		if(regions == null){
			return null;
		}
		for(ASRegion region : regions){
			if(region.has(location)){
				return region;
			}
		}
		return null;
	}

	/**
	 * Gets the region based off name
	 * 
	 * @param name the region name
	 * @return the region, or null if no region
	 */
	public ASRegion getRegion(String name){
		for(World world : Bukkit.getWorlds()){
			for(ASRegion region : regions.get(world)){
				if(region.getName().equalsIgnoreCase(name)){
					return region;
				}
			}
		}
		return null;
	}

	/**
	 * Adds a region to the game
	 * 
	 * @param selection the region area
	 * @param owner the region creator
	 * @param name the name of the region
	 * @param gamemode the gamemode of the region
	 */
	public void addRegion(Selection selection, String owner, String name, GameMode gamemode){
		if(selection == null || owner == null || name == null || gamemode == null){
			throw new NullPointerException("Region creation failed: Null field");
		}

		// Create region
		ASRegion region = new ASRegion(selection.getWorldEditSelection(), owner, gamemode, name);

		// Add to set
		Set<ASRegion> set = regions.get(region.getWorld()) == null ? new HashSet<ASRegion>() : regions.get(region.getWorld());
		set.add(region);
		regions.put(region.getWorld(), set);
	}

	/**
	 * Removes a region by location
	 * 
	 * @param location the location
	 */
	public void removeRegion(Location location){
		Set<ASRegion> regions = this.regions.get(location.getWorld()) == null ? new HashSet<ASRegion>() : this.regions.get(location.getWorld());
		for(ASRegion region : regions){
			if(region.has(location)){
				region.delete();
				regions.remove(region);
				break;
			}
		}
	}

	/**
	 * Removes a region by name
	 * 
	 * @param name the name
	 */
	public void removeRegion(String name){
		for(World world : regions.keySet()){
			for(ASRegion region : regions.get(world) == null ? new HashSet<ASRegion>() : regions.get(world)){
				if(region.getName().equalsIgnoreCase(name)){
					region.delete();
					regions.get(world).remove(region);
					return;
				}
			}
		}
	}

	/**
	 * Determines if a region name is already in use
	 * 
	 * @param name the name
	 * @return true if in use
	 */
	public boolean regionNameExists(String name){
		for(World world : regions.keySet()){
			for(ASRegion region : regions.get(world) == null ? new HashSet<ASRegion>() : regions.get(world)){
				if(region.getName().equalsIgnoreCase(name)){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets all the regions known to AntiShare in a world
	 * 
	 * @param world the world
	 * @return the list of regions
	 */
	public Set<ASRegion> getAllRegions(World world){
		return regions.get(world) == null ? new HashSet<ASRegion>() : regions.get(world);
	}

	/**
	 * Gets all the regions known to AntiShare
	 * 
	 * @return a set of ASRegions
	 */
	public List<ASRegion> getAllRegions(){
		List<ASRegion> regions = new ArrayList<ASRegion>();
		for(World world : Bukkit.getWorlds()){
			regions.addAll(getAllRegions(world));
		}
		return regions;
	}

	/**
	 * Gets all the regions for a Game Mode
	 * 
	 * @param gamemode the Game Mode
	 * @return the list of regions
	 */
	public List<ASRegion> getAllRegions(GameMode gamemode){
		List<ASRegion> regions = new ArrayList<ASRegion>();
		for(World world : Bukkit.getWorlds()){
			for(ASRegion region : getAllRegions(world)){
				if(region.getGameMode() == gamemode){
					regions.add(region);
				}
			}
		}
		return regions;
	}

}
