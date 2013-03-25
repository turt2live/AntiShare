package com.turt2live.antishare.inventory;

import java.io.File;

import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.inventory.ASInventory.InventoryType;

// TODO Document
public class LegacyInventory{

	public static ASInventory load(String player, GameMode gamemode, InventoryType type, String world){
		// Setup
		File dir = new File(AntiShare.p.getDataFolder(), "data" + File.separator + "inventories" + File.separator + type.getRelativeFolderName());
		File saveFile = new File(dir, player + ".yml");
		if(!saveFile.exists()){
			return ASInventory.EMPTY;
		}
		EnhancedConfiguration file = new EnhancedConfiguration(saveFile, AntiShare.p);
		file.load();
		ASInventory inventory = new ASInventory(gamemode, player, world, type);
		for(String stringSlot : file.getConfigurationSection(world + "." + gamemode.name()).getKeys(false)){
			Integer slot = Integer.valueOf(stringSlot);
			if(slot >= 100){
				slot = 36 + (slot - 100);
			}
			ItemStack item = file.getItemStack(world + "." + gamemode.name() + "." + stringSlot);
			inventory.set(slot, item);
		}
		// return
		return inventory;
	}

}
