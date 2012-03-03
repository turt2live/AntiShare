package com.turt2live.antishare;

import java.io.File;

import org.bukkit.Bukkit;

public class ASInventory {

	public static void cleanup(){
		AntiShare plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
		File sdir = new File(plugin.getDataFolder(), "inventories");
		String world = Bukkit.getWorlds().get(0).getName();
		if(sdir.exists()){
			for(File f : sdir.listFiles()){
				if(f.getName().endsWith("CREATIVE.yml")
						|| f.getName().endsWith("SURVIVAL.yml")){
					File newName = new File(f.getParent(), f.getName().replace("SURVIVAL", "SURVIVAL_" + world).replace("CREATIVE", "CREATIVE_" + world));
					f.renameTo(newName);
				}
			}
		}
	}
}
