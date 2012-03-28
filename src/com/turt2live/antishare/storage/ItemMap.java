package com.turt2live.antishare.storage;

import java.io.File;

import org.bukkit.Material;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;

public class ItemMap {

	private AntiShare plugin;
	private EnhancedConfiguration list;

	public ItemMap(AntiShare plugin){
		list = new EnhancedConfiguration(new File(plugin.getDataFolder(), "items.yml"), plugin);
		list.loadDefaults(plugin.getResource("resources/items.yml"));
		if(!list.fileExists() || !list.checkDefaults()){
			list.saveDefaults();
		}
		list.load();
	}

	public Material getItem(String name){
		if(list.getInt(name, -1) != -1){
			return Material.getMaterial(list.getInt(name, -1));
		}
		return Material.matchMaterial(name); // Returns null if not found
	}

	public void reload(){
		list.load();
	}

	public AntiShare getPlugin(){
		return plugin;
	}

}
