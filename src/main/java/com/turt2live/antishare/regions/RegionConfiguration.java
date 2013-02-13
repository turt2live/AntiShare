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

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.Systems.Manager;
import com.turt2live.antishare.manager.WorldConfigurationManager;
import com.turt2live.antishare.regions.PerWorldConfig.ListType;
import com.turt2live.antishare.util.ASUtils;
import com.turt2live.antishare.util.events.EntityList;
import com.turt2live.antishare.util.events.EventList;

/**
 * Region configuration
 * 
 * @author turt2live
 */
public class RegionConfiguration {

	private EventList blockBreak;
	private EventList blockPlace;
	private EventList rightClick;
	private EventList use;
	private EventList drop;
	private EventList pickup;
	private EventList death;
	private EventList commands;
	private EntityList mobs;
	private EntityList rmobs;
	private EventList crafting;
	private boolean clearInventoriesOnBreak = true;
	private boolean combatPlayers = false;
	private boolean combatMobs = false;
	private String world;
	private Region region;
	private WorldConfigurationManager worldConfigs = null;

	/**
	 * Creates a new region configuration
	 * 
	 * @param region the region
	 */
	public RegionConfiguration(Region region){
		AntiShare plugin = AntiShare.getInstance();
		if(plugin.getSystemsManager().isEnabled(Manager.WORLD_CONFIGS)){
			worldConfigs = (WorldConfigurationManager) plugin.getSystemsManager().getManager(Manager.WORLD_CONFIGS);
		}
		this.world = region.getWorldName();
		this.region = region;

		// Setup configuration
		File path = Region.REGION_CONFIGURATIONS;
		EnhancedConfiguration regionConfig = new EnhancedConfiguration(new File(path, ASUtils.fileSafeName(region.getName()) + ".yml"), plugin);
		regionConfig.loadDefaults(plugin.getResource("resources/region.yml"));
		if(!regionConfig.fileExists() || !regionConfig.checkDefaults()){
			regionConfig.saveDefaults();
		}
		regionConfig.load();

		// Generate lists
		blockPlace = getList("block-place", "block-place", regionConfig, false);
		blockBreak = getList("block-break", "block-break", regionConfig, false);
		death = getList("drop-items-on-death", "dropped-items-on-death", regionConfig, false);
		pickup = getList("pickup-items", "picked-up-items", regionConfig, false);
		drop = getList("drop-items", "dropped-items", regionConfig, false);
		rightClick = getList("right-click", "right-click", regionConfig, false);
		use = getList("use-items", "use-items", regionConfig, false);
		commands = getList("commands", "commands", regionConfig, true);
		mobs = getList("combat-against-mobs", "mobs", regionConfig);
		rmobs = getList("right-click-mobs", "right-click-mobs", regionConfig);
		crafting = getList("crafting-recipes", "crafting-recipes", regionConfig, false);

		// Get options
		boolean value = regionConfig.getBoolean("enabled-features.no-drops-when-block-break.inventories");
		if(regionConfig.getString("enabled-features.no-drops-when-block-break.inventories").equalsIgnoreCase("global")){
			value = plugin.getConfig().getBoolean("enabled-features.no-drops-when-block-break.inventories");
		}
		if(regionConfig.getString("enabled-features.no-drops-when-block-break.inventories").equalsIgnoreCase("world")){
			String tvalue = worldConfigs.getConfig(world).getRaw().getString("enabled-features.no-drops-when-block-break.inventories");
			if(tvalue.equalsIgnoreCase("global")){
				value = plugin.getConfig().getBoolean("enabled-features.no-drops-when-block-break.inventories");
			}else{
				value = worldConfigs.getConfig(world).getRaw().getBoolean("enabled-features.no-drops-when-block-break.inventories");
			}
		}
		clearInventoriesOnBreak = value;
		value = regionConfig.getBoolean("enabled-features.no-drops-when-block-break.attached-blocks");
		if(regionConfig.getString("enabled-features.no-drops-when-block-break.attached-blocks").equalsIgnoreCase("global")){
			value = plugin.getConfig().getBoolean("enabled-features.no-drops-when-block-break.attached-blocks");
		}
		if(regionConfig.getString("enabled-features.no-drops-when-block-break.attached-blocks").equalsIgnoreCase("world")){
			String tvalue = worldConfigs.getConfig(world).getRaw().getString("enabled-features.no-drops-when-block-break.attached-blocks");
			if(tvalue.equalsIgnoreCase("global")){
				value = plugin.getConfig().getBoolean("enabled-features.no-drops-when-block-break.attached-blocks");
			}else{
				value = worldConfigs.getConfig(world).getRaw().getBoolean("enabled-features.no-drops-when-block-break.attached-blocks");
			}
		}
		value = regionConfig.getBoolean("blocked-actions.combat-against-players");
		if(regionConfig.getString("blocked-actions.combat-against-players").equalsIgnoreCase("global")){
			value = plugin.getConfig().getBoolean("blocked-actions.combat-against-players");
		}else if(regionConfig.getString("blocked-actions.combat-against-players").equalsIgnoreCase("world")){
			value = worldConfigs.getConfig(world).combatAgainstPlayers();
		}
		combatPlayers = value;
		value = regionConfig.getBoolean("blocked-actions.combat-against-mobs");
		if(regionConfig.getString("blocked-actions.combat-against-mobs").equalsIgnoreCase("global")){
			value = plugin.getConfig().getBoolean("blocked-actions.combat-against-mobs");
		}else if(regionConfig.getString("blocked-actions.combat-against-mobs").equalsIgnoreCase("world")){
			value = worldConfigs.getConfig(world).combatAgainstMobs();
		}
		combatMobs = value;
	}

	private EventList getList(String triggerPath, String listPath, EnhancedConfiguration regionConfig, boolean stringsOnly){
		// Setup
		boolean enabled = false;
		boolean global = false;
		boolean gworld = false;
		AntiShare plugin = AntiShare.getInstance();

		// Determine if enabled
		if(regionConfig.getString("blocked-actions." + triggerPath).equalsIgnoreCase("global")){
			global = true;
			enabled = plugin.getConfig().getBoolean("blocked-actions." + triggerPath);
		}else if(regionConfig.getString("blocked-actions." + triggerPath).equalsIgnoreCase("world")){
			PerWorldConfig perWorld = worldConfigs.getConfig(world);
			if(perWorld == null){
				enabled = true;
				gworld = true;
				plugin.getLogger().warning("Failed to find world configuration for " + world);
			}else{
				EnhancedConfiguration raw = perWorld.getRaw();
				if(raw.getString("blocked-actions." + triggerPath).equalsIgnoreCase("global")){
					enabled = plugin.getConfig().getBoolean("blocked-actions." + triggerPath);
					gworld = true;
				}else{
					enabled = raw.getBoolean("blocked-actions." + triggerPath);
					global = true;
				}
			}
		}else{
			enabled = regionConfig.getBoolean("blocked-actions." + triggerPath);
		}

		// Get the list
		String list = "";
		if(enabled){
			list = regionConfig.getString("blocked-lists." + listPath);
			if(list.equalsIgnoreCase("global")){
				list = plugin.getConfig().getString("blocked-lists." + listPath);
			}
			if(list.equalsIgnoreCase("world")){
				list = worldConfigs.getConfig(world).getRaw().getString("blocked-lists." + listPath);
				if(list.equalsIgnoreCase("global")){
					list = plugin.getConfig().getString("blocked-lists." + listPath);
				}
			}
		}

		// Generate and return
		return stringsOnly ? new EventList(true, list.split(",")) : new EventList(gworld ? world + ".yml" : (global ? "config.yml" : "region_configurations/" + region.getName() + ".yml"), "blocked-actions." + triggerPath, list.split(","));
	}

	private EntityList getList(String triggerPath, String listPath, EnhancedConfiguration worldConfig){
		// Setup
		boolean enabled = false;
		boolean global = false;
		AntiShare plugin = AntiShare.getInstance();

		// Determine if enabled
		if(worldConfig.getString("blocked-actions." + triggerPath).equalsIgnoreCase("global")){
			global = true;
			enabled = plugin.getConfig().getBoolean("blocked-actions." + triggerPath);
		}else{
			enabled = worldConfig.getBoolean("blocked-actions." + triggerPath);
		}

		// Get the list
		String list = "";
		if(enabled){
			list = worldConfig.getString("blocked-lists." + listPath);
			if(list.equalsIgnoreCase("global")){
				list = plugin.getConfig().getString("blocked-lists." + listPath);
			}
		}

		// Generate and return
		return new EntityList(global ? "config.yml" : world + "_config.yml", "blocked-actions." + triggerPath, list.split(","));
	}

	/**
	 * Checks if an item in this world is blocked
	 * 
	 * @param material the material
	 * @param list the list type
	 * @return true if blocked
	 */
	public boolean isBlocked(Material material, ListType list){
		switch (list){
		case BLOCK_BREAK:
			return blockBreak.isBlocked(material);
		case BLOCK_PLACE:
			return blockPlace.isBlocked(material);
		case RIGHT_CLICK:
			return rightClick.isBlocked(material);
		case USE:
			return use.isBlocked(material);
		case DROP:
			return drop.isBlocked(material);
		case PICKUP:
			return pickup.isBlocked(material);
		case DEATH:
			return death.isBlocked(material);
		case CRAFTING:
			return crafting.isBlocked(material);
		default:
			return false;
		}
	}

	/**
	 * Checks if an item in this region is blocked
	 * 
	 * @param block the block
	 * @param list the list type
	 * @return true if blocked
	 */
	public boolean isBlocked(Block block, ListType list){
		switch (list){
		case BLOCK_BREAK:
			return blockBreak.isBlocked(block);
		case BLOCK_PLACE:
			return blockPlace.isBlocked(block);
		case RIGHT_CLICK:
			return rightClick.isBlocked(block);
		case USE:
			return use.isBlocked(block);
		case DROP:
			return drop.isBlocked(block);
		case PICKUP:
			return pickup.isBlocked(block);
		case DEATH:
			return death.isBlocked(block);
		default:
			return false;
		}
	}

	/**
	 * Checks if a string (usually command) in this world is blocked
	 * 
	 * @param string the string
	 * @param list the list type
	 * @return true if blocked
	 */
	public boolean isBlocked(String string, ListType list){
		switch (list){
		case COMMAND:
			return commands.isBlocked(string);
		default:
			return false;
		}
	}

	/**
	 * Determines if an entity is blocked
	 * 
	 * @param entity the entity
	 * @param list the list
	 * @return true if blocked
	 */
	public boolean isBlocked(Entity entity, ListType list){
		switch (list){
		case MOBS:
			return mobs.isBlocked(entity);
		case RIGHT_CLICK_MOBS:
			return rmobs.isBlocked(entity);
		default:
			return false;
		}
	}

	/**
	 * Determines if a block's inventory (eg: chest) should be cleared upon it being broken
	 * 
	 * @return true if clearing should be done
	 */
	public boolean clearBlockInventoryOnBreak(){
		return clearInventoriesOnBreak;
	}

	/**
	 * Determines if combat against players is allowed in this region
	 * 
	 * @return true if allowed
	 */
	public boolean combatAgainstPlayers(){
		return combatPlayers;
	}

	/**
	 * Determines if combat against mobs is allowed in this region
	 * 
	 * @return true if allowed
	 */
	public boolean combatAgainstMobs(){
		return combatMobs;
	}

	/**
	 * Gets the region that uses this configuration
	 * 
	 * @return the region
	 */
	public Region getRegion(){
		return region;
	}

	/**
	 * Gets the world for this configuration
	 * 
	 * @return the world
	 */
	public String getWorldName(){
		return world;
	}

	/**
	 * Determines if thrown potions are allowed in this region
	 * 
	 * @return true if allowed
	 */
	public boolean isThrownPotionAllowed(){
		return use.isThrownPotionAllowed();
	}

	/**
	 * Determines if potions are allowed in this region
	 * 
	 * @return true if allowed
	 */
	public boolean isPotionAllowed(){
		return use.isPotionAllowed();
	}

}
