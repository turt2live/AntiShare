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
package com.turt2live.antishare.api;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import com.sk89q.worldedit.bukkit.selections.Selection;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.deprecatedregions.ASRegion;
import com.turt2live.antishare.deprecatedregions.RegionKey.RegionKeyType;

/**
 * The AntiShare API<br>
 * Currently the API only supports regions
 * 
 * @author turt2live
 */
@SuppressWarnings ("deprecation")
public class ASAPI {

	private AntiShare plugin;

	/**
	 * Creates a new AntiShare API instance
	 */
	public ASAPI(){
		plugin = AntiShare.getInstance();
	}

	/**
	 * Gets the plugin instance of AntiShare
	 * 
	 * @return the plugin
	 */
	public AntiShare getPluginInstance(){
		return plugin;
	}

	/**
	 * Creates a new AntiShare Region and adds it to the world
	 * 
	 * @param selection the WorldEdit selection
	 * @param owner the region owner/creator (cannot be null, does not have to be unique)
	 * @param gamemode the region's Game Mode
	 * @param name the region name
	 */
	@Deprecated
	public void createRegion(Selection selection, String owner, GameMode gamemode, String name){
		// TODO: Regions
		//plugin.getRegionManager().addRegion(new com.turt2live.antishare.util.generic.Selection(selection), owner, name, gamemode);
	}

	/**
	 * Gets a region by name
	 * 
	 * @param name the region name
	 * @return the region (null if none found)
	 */
	@Deprecated
	public ASRegion getRegion(String name){
		// TODO: Regions
		//return plugin.getRegionManager().getRegion(name);
		return null;
	}

	/**
	 * Gets a region by location. The location must be somewhere in the region (even border)
	 * 
	 * @param location the location
	 * @return the region (null if none found)
	 */
	@Deprecated
	public ASRegion getRegion(Location location){
		// TODO: Regions
		//return plugin.getRegionManager().getRegion(location);
		return null;
	}

	/**
	 * Edits a region
	 * 
	 * @param region the region
	 * @param key the key
	 * @param value the value
	 * @param target the target editing the region (cannot be null)
	 */
	@Deprecated
	public void editRegion(ASRegion region, RegionKeyType key, String value, CommandSender target){
		// TODO: Regions
		//plugin.getRegionFactory().editRegion(region, key, value, target);
	}

}
