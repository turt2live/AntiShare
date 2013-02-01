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
package com.turt2live.antishare.util.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.signs.Sign;
import com.turt2live.antishare.util.ASUtils;

/**
 * Used similar to an Event List, but handles block-tracking instead
 * 
 * @author turt2live
 */
public class TrackerList {

	private final List<String> tracked = new ArrayList<String>();
	private final List<Sign> trackedsigns = new ArrayList<Sign>();

	/**
	 * Creates a new Tracker List
	 * 
	 * @param file the configuration file name
	 * @param node the configuration node
	 * @param configurationValue the values
	 */
	public TrackerList(String file, String node, String... configurationValue){
		if(configurationValue.length <= 0){
			return;
		}

		// Setup
		AntiShare plugin = AntiShare.getInstance();

		// Loop
		for(String tracked : configurationValue){
			tracked = tracked.trim();
			boolean negate = false;
			if(tracked.startsWith("-")){
				negate = true;
				tracked = tracked.replaceFirst("-", "");
			}

			// Check for "all"/"none"
			if(configurationValue.length == 1){
				if(tracked.equalsIgnoreCase("*") || tracked.equalsIgnoreCase("all")){
					// Add materials
					for(Material m : Material.values()){
						if(!negate){
							this.tracked.add(ASUtils.materialToString(m, false));
						}else{
							this.tracked.remove(ASUtils.materialToString(m, false));
						}
					}

					// Add signs
					for(Sign s : plugin.getSignList().getAllSigns()){
						if(!negate){
							trackedsigns.add(s);
						}else{
							trackedsigns.remove(s);
						}
					}
					continue;
				}else if(tracked.equalsIgnoreCase("none")){
					trackedsigns.clear();
					this.tracked.clear();
					continue; // For sanity sake
				}
			}

			// Sign?
			if(tracked.toLowerCase().startsWith("sign:")){
				String signname = tracked.split(":").length > 0 ? tracked.split(":")[1] : null;
				if(signname == null){
					// TODO: Locale
					plugin.getLogger().warning("Configuration Problem: '" + (negate ? "-" : "") + tracked + "' is not valid! (See '" + node + "' in your " + file + ")");
					continue;
				}
				Sign sign = plugin.getSignList().getSign(signname);
				if(sign == null){
					// TODO: Locale
					plugin.getLogger().warning("Configuration Problem: '" + (negate ? "-" : "") + tracked + "' is not valid! (See '" + node + "' in your " + file + ")");
					continue;
				}
				if(!negate){
					trackedsigns.add(sign);
				}else{
					trackedsigns.remove(sign);
				}
				continue;
			}

			// Special case: Furnaces
			if(tracked.equalsIgnoreCase("furnace") || tracked.replaceAll(" ", "").replaceAll("_", "").equalsIgnoreCase("litfurnace") || tracked.equalsIgnoreCase("oven") || tracked.equalsIgnoreCase(String.valueOf(Material.FURNACE.getId())) || tracked.equalsIgnoreCase(String.valueOf(Material.BURNING_FURNACE.getId()))){
				if(!negate){
					this.tracked.add(ASUtils.materialToString(Material.FURNACE, false));
					this.tracked.add(ASUtils.materialToString(Material.BURNING_FURNACE, false));
				}else{
					this.tracked.remove(ASUtils.materialToString(Material.FURNACE, false));
					this.tracked.remove(ASUtils.materialToString(Material.BURNING_FURNACE, false));
				}
				continue;
			}

			// Special case: Sign
			if(tracked.equalsIgnoreCase("sign") || tracked.replaceAll(" ", "").replaceAll("_", "").equalsIgnoreCase("wallsign") || tracked.replaceAll(" ", "").replaceAll("_", "").equalsIgnoreCase("signpost") || tracked.equalsIgnoreCase(String.valueOf(Material.SIGN.getId())) || tracked.equalsIgnoreCase(String.valueOf(Material.WALL_SIGN.getId())) || tracked.equalsIgnoreCase(String.valueOf(Material.SIGN_POST.getId()))){
				if(!negate){
					this.tracked.add(ASUtils.materialToString(Material.SIGN, false));
					this.tracked.add(ASUtils.materialToString(Material.SIGN_POST, false));
					this.tracked.add(ASUtils.materialToString(Material.WALL_SIGN, false));
				}else{
					this.tracked.remove(ASUtils.materialToString(Material.SIGN, false));
					this.tracked.remove(ASUtils.materialToString(Material.SIGN_POST, false));
					this.tracked.remove(ASUtils.materialToString(Material.WALL_SIGN, false));
				}
				continue;
			}

			// Special case: Brewing Stand
			if(tracked.replaceAll(" ", "").replaceAll("_", "").equalsIgnoreCase("brewingstand") || tracked.replaceAll(" ", "").replaceAll("_", "").equalsIgnoreCase("brewingstanditem") || tracked.equalsIgnoreCase(String.valueOf(Material.BREWING_STAND.getId())) || tracked.equalsIgnoreCase(String.valueOf(Material.BREWING_STAND_ITEM.getId()))){
				if(!negate){
					this.tracked.add(ASUtils.materialToString(Material.BREWING_STAND, false));
					this.tracked.add(ASUtils.materialToString(Material.BREWING_STAND_ITEM, false));
				}else{
					this.tracked.remove(ASUtils.materialToString(Material.BREWING_STAND, false));
					this.tracked.remove(ASUtils.materialToString(Material.BREWING_STAND_ITEM, false));
				}
				continue;
			}

			// Special case: Wool
			if(ASUtils.getWool(tracked) != null){
				if(!negate){
					this.tracked.add(ASUtils.getWool(tracked));
				}else{
					this.tracked.remove(ASUtils.getWool(tracked));
				}
				continue;
			}

			// Try to add the item, warn otherwise
			if(plugin.getItemMap().getSign(tracked) != null){
				if(!negate){
					this.trackedsigns.add(plugin.getItemMap().getSign(tracked));
				}else{
					this.trackedsigns.remove(plugin.getItemMap().getSign(tracked));
				}
				continue;
			}
			try{
				if(plugin.getItemMap().getItem(tracked, false, false) == null){
					throw new Exception("");
				}
				if(!negate){
					this.tracked.add(plugin.getItemMap().getItem(tracked, false, false));
				}else{
					this.tracked.remove(plugin.getItemMap().getItem(tracked, false, false));
				}
			}catch(Exception e){
				plugin.getLogger().warning("Configuration Problem: '" + (negate ? "-" : "") + tracked + "' is not valid! (See '" + node + "' in your " + file + ")");
			}
		}
	}

	/**
	 * Determines if a block is tracked or not
	 * 
	 * @param block the block
	 * @return true if tracked
	 */
	public boolean isTracked(Block block){
		if(tracked.size() == 0){
			return false;
		}
		String defaultID = ASUtils.blockToString(block, false);
		if(tracked.contains(defaultID)){
			return true;
		}
		defaultID = block.getTypeId() + ":*";
		if(tracked.contains(defaultID)){
			return true;
		}
		return false;
	}

	/**
	 * Determines if an entity is tracked or not
	 * 
	 * @param entity the entity
	 * @return true if tracked
	 */
	public boolean isTracked(Entity entity){
		return isTracked(entity.getType());
	}

	/**
	 * Determines if an entity is tracked or not
	 * 
	 * @param entityType the entity
	 * @return true if tracked
	 */
	public boolean isTracked(EntityType entityType){
		if(tracked.size() == 0){
			return false;
		}
		Material mat = Material.AIR;
		switch(entityType){
		case PAINTING:
			mat = Material.PAINTING;
			break;
		case ITEM_FRAME:
			mat = Material.ITEM_FRAME;
			break;
		default:
			break;
		}
		if(mat == Material.AIR){
			return false;
		}
		String defaultID = mat.getId() + ":*";
		if(tracked.contains(defaultID)){
			return true;
		}
		return false;
	}

}
