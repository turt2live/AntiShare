package com.turt2live;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.feildmaster.lib.configuration.EnhancedConfiguration;

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
	public static void load(Player player, GameMode gamemode){
		try{
			AntiShare plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
			File sdir = new File(plugin.getDataFolder(), "inventories");
			sdir.mkdirs();
			File saveFile = new File(sdir, player.getName() + "_" + gamemode.toString() + "_" + player.getWorld().getName() + ".yml");
			if(!saveFile.exists()){
				saveFile.createNewFile();
			}
			EnhancedConfiguration config = new EnhancedConfiguration(saveFile, plugin);
			config.load();
			Integer i = 0;
			Integer size = player.getInventory().getSize();
			player.getInventory().clear();
			for(i = 0; i < size; i++){
				ItemStack item = new ItemStack(0, 0);
				if(config.getItemStack(i.toString()) != null){
					item = config.getItemStack(i.toString());
					player.getInventory().setItem(i, item);
					player.updateInventory();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void save(Player player, GameMode gamemode){
		wipe(player);
		try{
			AntiShare plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
			File sdir = new File(plugin.getDataFolder(), "inventories");
			sdir.mkdirs();
			File saveFile = new File(sdir, player.getName() + "_" + gamemode.toString() + "_" + player.getWorld().getName() + ".yml");
			if(!saveFile.exists()){
				saveFile.createNewFile();
			}
			EnhancedConfiguration config = new EnhancedConfiguration(saveFile, plugin);
			config.load();
			Integer i = 0;
			Integer size = player.getInventory().getSize();
			for(i = 0; i < size; i++){
				ItemStack item = player.getInventory().getItem(i);
				config.set(i.toString(), item);
			}
			config.save();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void wipe(Player player){
		AntiShare plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
		File sdir = new File(plugin.getDataFolder(), "inventories");
		sdir.mkdirs();
		File saveFile = new File(sdir, player.getName() + "_" + player.getGameMode().toString() + "_" + player.getWorld().getName() + ".yml");
		if(saveFile.exists()){
			saveFile.delete();
			try{
				saveFile.createNewFile();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
