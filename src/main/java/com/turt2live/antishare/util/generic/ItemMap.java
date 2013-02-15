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
package com.turt2live.antishare.util.generic;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.signs.Sign;

/**
 * Item Map - Allows for "custom" item names
 * 
 * @author turt2live
 */
public class ItemMap {

	private EnhancedConfiguration yaml;
	private AntiShare plugin = AntiShare.getInstance();
	private Map<String, String> temporaryItems = new HashMap<String, String>();
	private Map<String, Sign> temporarySigns = new HashMap<String, Sign>();

	/**
	 * Creates a new Item Map
	 */
	public ItemMap(){
		yaml = new EnhancedConfiguration(new File(plugin.getDataFolder(), "items.yml"), plugin);
		yaml.loadDefaults(plugin.getResource("resources/items.yml"));
		if(!yaml.fileExists() || !yaml.checkDefaults()){
			yaml.saveDefaults();
		}
		yaml.load();
	}

	/**
	 * Gets an item from the map
	 * 
	 * @param name the name
	 * @return the Material (or null if not found)
	 */
	public Material getItem(String name){
		if(name == null){
			return null;
		}
		if(yaml.getString(name) != null){
			return Material.matchMaterial(yaml.getString(name));
		}
		if(temporaryItems.containsKey(name.toLowerCase())){
			return Material.matchMaterial(temporaryItems.get(name.toLowerCase()));
		}
		return Material.matchMaterial(name); // Returns null if not found
	}

	/**
	 * Gets a sign from the map
	 * 
	 * @param name the name
	 * @return the sign (or null if not found)
	 */
	public Sign getSign(String name){
		if(yaml.getString(name) != null){
			String[] parts = yaml.getString(name).split(":");
			if(parts.length == 2){
				return plugin.getSignList().getSign(parts[1]);
			}
		}
		if(temporarySigns.containsKey(name.toLowerCase())){
			return temporarySigns.get(name.toLowerCase());
		}
		return null;
	}

	/**
	 * Gets an item from the map. This returns the id:data format
	 * 
	 * @param name the item name to lookup
	 * @param zero if true, and the data is zero, the :0 will be removed
	 * @param isDataNumber set to true to force the method to do a number check
	 * @return the id:data format
	 */
	public String getItem(String name, boolean zero, boolean isDataNumber){
		if(name == null){
			return null;
		}
		String[] parts = name.split(":");
		if(yaml.getString(parts[0]) != null){
			parts[0] = yaml.getString(parts[0]);
		}
		if(temporaryItems.containsKey(parts[0].toLowerCase())){
			parts[0] = temporaryItems.get(parts[0].toLowerCase());
		}
		Material m = Material.matchMaterial(parts[0]);
		if(m == null){
			return null;
		}
		StringBuilder ret = new StringBuilder();
		ret.append(m.getId());
		if(parts.length > 1){
			if(isDataNumber){
				int dval = 0;
				try{
					dval = Integer.parseInt(parts[1]);
				}catch(NumberFormatException e){}
				if(dval == 0 && !zero){
					ret.append(":");
					ret.append(dval);
				}else if(dval != 0){
					ret.append(":");
					ret.append(dval);
				}
			}else{
				ret.append(":");
				ret.append(parts[1]);
			}
		}
		if(ret.toString().split(":").length < 2 && !zero){
			ret.append(":");
			ret.append("*");
		}
		return ret.toString();
	}

	/**
	 * Reloads the item map
	 */
	public void reload(){
		yaml.load();
		temporaryItems.clear();
	}

	/**
	 * Adds a sign to the Item Map. <b>This is TEMPORARY and will NOT SAVE.</b><br>
	 * <i>Signs added here <b>CANNOT</b> be reached from the Sign List</i>
	 * 
	 * @param name the sign name
	 * @param sign the sign
	 */
	public void addTemporarySign(String name, Sign sign){
		this.temporarySigns.put(name.toLowerCase(), sign);
	}

	/**
	 * Adds a Material to the Item Map. <b>This is TEMPORARY and will NOT SAVE.</b>
	 * 
	 * @param name the sign name
	 * @param material the material
	 */
	public void addTemporaryItem(String name, Material material){
		temporaryItems.put(name.toLowerCase(), material.name());
	}

	/**
	 * Adds a Material to the Item Map. <b>This is TEMPORARY and will NOT SAVE.</b> <br>
	 * <i>This method will <b>NOT</b> verify the material input.</i>
	 * 
	 * @param name the sign name
	 * @param material the material
	 */
	public void addTemporaryItem(String name, String material){
		temporaryItems.put(name.toLowerCase(), material);
	}

}
