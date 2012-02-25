package com.turt2live.antishare;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.turt2live.antishare.storage.ASVirtualInventory;

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

	@SuppressWarnings ("deprecation")
	public static void load(Player player, GameMode gamemode, World world){
		AntiShare plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
		ASVirtualInventory manager = plugin.storage.getInventoryManager(player, world);
		HashMap<Integer, ItemStack> inventory = null;
		if(gamemode.equals(GameMode.CREATIVE)){
			inventory = manager.getCreativeInventory();
		}else if(gamemode.equals(GameMode.SURVIVAL)){
			inventory = manager.getSurvivalInventory();
		}
		player.getInventory().clear();
		for(Integer slot : inventory.keySet()){
			player.getInventory().setItem(slot, inventory.get(slot));
		}
		player.updateInventory();
	}

	public static void save(Player player, GameMode gamemode, World world){
		AntiShare plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
		plugin.storage.getInventoryManager(player, world).saveInventory(gamemode);
	}
}
