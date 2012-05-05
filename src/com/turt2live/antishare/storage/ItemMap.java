package com.turt2live.antishare.storage;

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
				return AntiShare.getInstance().getSignManager().getSign(parts[1]);
			}
		}
		return null;
	}

	/**
	 * Reloads the item map
	 */
	public void reload(){
		list.load();
	}

}
