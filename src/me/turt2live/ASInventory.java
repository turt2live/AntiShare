package me.turt2live;

import java.io.File;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ASInventory {

	public static void save(Player player, GameMode gamemode){
		try{
			Integer size = player.getInventory().getSize();
			Integer i = 0;
			File sdir = new File(AntiShare.getSaveFolder(), "inventories");
			sdir.mkdirs();
			File saveFile = new File(sdir, player.getName() + "_" + gamemode.toString() + ".yml");
			if(!saveFile.exists())
				saveFile.createNewFile();
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
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@SuppressWarnings ("deprecation")
	public static void load(Player player, GameMode gamemode){
		try{
			File sdir = new File(AntiShare.getSaveFolder(), "inventories");
			sdir.mkdirs();
			File saveFile = new File(sdir, player.getName() + "_" + gamemode.toString() + ".yml");
			if(!saveFile.exists())
				saveFile.createNewFile();
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
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
