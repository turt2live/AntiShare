package com.turt2live;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ASInventory {

	public static void cleanup(){
		File sdir = new File(ASUtils.getSaveFolder(), "inventories");
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
			File sdir = new File(ASUtils.getSaveFolder(), "inventories");
			sdir.mkdirs();
			final File saveFile = new File(sdir, player.getName() + "_" + gamemode.toString() + "_" + player.getWorld().getName() + ".yml");
			if(!saveFile.exists()){
				saveFile.createNewFile();
			}
			FileConfiguration config = new YamlConfiguration();
			config.load(saveFile);
			Integer i = 0;
			Integer size = player.getInventory().getSize();
			player.getInventory().clear();
			for(i = 0; i < size; i++){
				ItemStack item = new ItemStack(0, 0);
				if(config.getInt(i.toString() + ".amount", 0) != 0){
					Integer amount = config.getInt(i.toString() + ".amount", 0);
					Integer durability = config.getInt(i.toString() + ".durability", 0);
					Integer type = config.getInt(i.toString() + ".type", 0);
					item.setAmount(amount);
					item.setTypeId(type);
					item.setDurability(Short.parseShort(durability.toString()));
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
			Integer size = player.getInventory().getSize();
			Integer i = 0;
			File sdir = new File(ASUtils.getSaveFolder(), "inventories");
			sdir.mkdirs();
			File saveFile = new File(sdir, player.getName() + "_" + gamemode.toString() + "_" + player.getWorld().getName() + ".yml");
			if(!saveFile.exists()){
				saveFile.createNewFile();
			}
			FileConfiguration config = new YamlConfiguration();
			config.load(saveFile);
			for(i = 0; i < size; i++){
				ItemStack item = player.getInventory().getItem(i);
				if(item.getAmount() != 0){
					config.set(i.toString() + ".amount", item.getAmount());
					Short durab = item.getDurability();
					config.set(i.toString() + ".durability", durab.intValue());
					config.set(i.toString() + ".type", item.getTypeId());
					config.save(saveFile);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void wipe(Player player){
		File sdir = new File(ASUtils.getSaveFolder(), "inventories");
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
