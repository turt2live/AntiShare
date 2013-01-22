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

	private EnhancedConfiguration list;

	/**
	 * Creates a new Item Map
	 */
	public ItemMap(){
		AntiShare plugin = AntiShare.getInstance();
		list = new EnhancedConfiguration(new File(plugin.getDataFolder(), "items.yml"), plugin);
		list.loadDefaults(plugin.getResource("resources/items.yml"));
		if(!list.fileExists() || !list.checkDefaults()){
			list.saveDefaults();
		}
		list.load();
	}

	/**
	 * Gets an item from the map
	 * 
	 * @param name the name
	 * @return the Material (or null if not found)
	 */
	public Material getItem(String name){
		if(list.getString(name) != null){
			return Material.getMaterial(list.getString(name));
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
		if(list.getString(name) != null){
			String[] parts = list.getString(name).split(":");
			if(parts.length == 2){
				return AntiShare.getInstance().getSignList().getSign(parts[1]);
			}
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
		if(list.getString(name) != null){
			name = list.getString(name);
		}
		String[] parts = name.split(":");
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
		list.load();
	}

}
