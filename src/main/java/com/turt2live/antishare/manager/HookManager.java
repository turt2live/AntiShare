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
package com.turt2live.antishare.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.compatibility.ChestShop;
import com.turt2live.antishare.compatibility.LWC;
import com.turt2live.antishare.compatibility.Lockette;
import com.turt2live.antishare.compatibility.LogBlock;
import com.turt2live.antishare.compatibility.Towny;
import com.turt2live.antishare.compatibility.type.BlockLogger;
import com.turt2live.antishare.compatibility.type.BlockProtection;
import com.turt2live.antishare.compatibility.type.RegionProtection;
import com.turt2live.antishare.lang.LocaleMessage;
import com.turt2live.antishare.lang.Localization;

/**
 * Manages hooks into other plugins
 * 
 * @author turt2live
 */
public class HookManager extends AntiShareManager {

	private AntiShare plugin = AntiShare.getInstance();
	private final List<BlockProtection> blocks = new ArrayList<BlockProtection>();
	private final List<RegionProtection> regions = new ArrayList<RegionProtection>();
	private final List<BlockLogger> loggers = new ArrayList<BlockLogger>();

	/**
	 * Sends a block break to all block logging plugins
	 * 
	 * @param playerName the player name involved, or null for no associated player
	 * @param location the location
	 * @param before the material before the break
	 * @param data the data before the break
	 */
	public void sendBlockBreak(String playerName, Location location, Material before, byte data){
		for(BlockLogger logger : loggers){
			logger.breakBlock(playerName != null ? BlockLogger.PLAYER_NAME + "_" + playerName : BlockLogger.PLAYER_NAME, location, before, data);
		}
	}

	/**
	 * Sends a block place to all block logging plugins
	 * 
	 * @param playerName the player name involved, or null for no associated player
	 * @param location the location
	 * @param after the material after the break
	 * @param data the data after the break
	 */
	public void sendBlockPlace(String playerName, Location location, Material after, byte data){
		for(BlockLogger logger : loggers){
			logger.placeBlock(playerName != null ? BlockLogger.PLAYER_NAME + "_" + playerName : BlockLogger.PLAYER_NAME, location, after, data);
		}
	}

	/**
	 * Determines if there is a region at a location
	 * 
	 * @param location the location
	 * @return true if a region was found, false otherwise
	 */
	public boolean checkForRegion(Location location){
		for(RegionProtection protection : regions){
			if(protection.isRegion(location)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks for a player placing/breaking a block into a region
	 * 
	 * @param player the player
	 * @param block the block
	 * @return true if region found (not allowed), false otherwise
	 */
	public boolean checkForRegion(Player player, Block block){
		for(RegionProtection protection : regions){
			if(!protection.isAllowed(player, block)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks a block for sign protection
	 * 
	 * @param block the block
	 * @return true if a sign protection plugin claims this block protected
	 */
	public boolean checkForSignProtection(Block block){
		return checkForBlockProtection(block);
	}

	/**
	 * Checks a block for protection
	 * 
	 * @param block the block
	 * @return true if a plugin claims this block protected
	 */
	public boolean checkForBlockProtection(Block block){
		for(BlockProtection protection : blocks){
			if(protection.isProtected(block)){
				return true;
			}
		}
		return false;
	}

	private void hooked(Plugin hook){
		if(!plugin.getConfig().getBoolean("other.more-quiet-startup") || AntiShare.isDebug()){
			plugin.getLogger().info(Localization.getMessage(LocaleMessage.STATUS_HOOKING, hook.getName()));
		}
	}

	@Override
	public boolean load(){
		// Clear
		blocks.clear();
		regions.clear();

		// Find plugins
		Plugin chestshop = plugin.getServer().getPluginManager().getPlugin("ChestShop");
		if(chestshop != null){
			blocks.add(new ChestShop());
			hooked(chestshop);
		}
		Plugin lwc = plugin.getServer().getPluginManager().getPlugin("LWC");
		if(lwc != null){
			blocks.add(new LWC());
			hooked(lwc);
		}
		Plugin lockette = plugin.getServer().getPluginManager().getPlugin("Lockette");
		if(lockette != null){
			blocks.add(new Lockette());
			hooked(lockette);
		}
		Plugin towny = plugin.getServer().getPluginManager().getPlugin("Towny");
		if(towny != null){
			regions.add(new Towny());
			hooked(towny);
		}
		Plugin logblock = plugin.getServer().getPluginManager().getPlugin("LogBlock");
		if(logblock != null){
			loggers.add(new LogBlock());
			hooked(logblock);
		}
		return true;
	}

	@Override
	public boolean save(){
		return true;
	}

}
