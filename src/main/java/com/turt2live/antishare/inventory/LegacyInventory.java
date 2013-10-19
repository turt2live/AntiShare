package com.turt2live.antishare.inventory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.inventory.ASInventory.InventoryType;

/**
 * Legacy inventory (5.3.0)
 * 
 * @author turt2live
 */
public class LegacyInventory {

	/**
	 * Loads a single inventory
	 * 
	 * @param player the player
	 * @param gamemode the gamemode
	 * @param type the type
	 * @param world the world
	 * @return the inventory, never null
	 */
	public static ASInventory load(String player, GameMode gamemode, InventoryType type, String world) {
		// Setup
		File dir = new File(AntiShare.p.getDataFolder(), "data" + File.separator + "inventories" + File.separator + type.getRelativeFolderName());
		File saveFile = new File(dir, player + ".yml");
		if (!saveFile.exists()) {
			return ASInventory.EMPTY;
		}
		EnhancedConfiguration file = new EnhancedConfiguration(saveFile, AntiShare.p);
		file.load();
		ASInventory inventory = new ASInventory(gamemode, player, world, type);
		for (String stringSlot : file.getConfigurationSection(world + "." + gamemode.name()).getKeys(false)) {
			Integer slot = Integer.valueOf(stringSlot);
			if (slot >= 100) {
				slot = 36 + (slot - 100);
			}
			ItemStack item = file.getItemStack(world + "." + gamemode.name() + "." + stringSlot);
			inventory.set(slot, item);
		}
		// return
		return inventory;
	}

	/**
	 * Loads all inventories related to the player
	 * 
	 * @param player the player
	 * @param type the type
	 * @return a list of inventories, never null
	 */
	public static List<ASInventory> load(String player, InventoryType type) {
		// Setup
		List<ASInventory> inventories = new ArrayList<ASInventory>();

		// Setup
		File dir = new File(AntiShare.p.getDataFolder(), "data" + File.separator + "inventories" + File.separator + type.getRelativeFolderName());
		File saveFile = new File(dir, player + ".yml");
		if (!saveFile.exists()) {
			return inventories;
		}
		EnhancedConfiguration file = new EnhancedConfiguration(saveFile, AntiShare.p);
		file.load();

		// Load data
		// Structure: yml:world.gamemode.slot.properties
		for (String world : file.getKeys(false)) {
			for (String gamemode : file.getConfigurationSection(world).getKeys(false)) {
				World bukkitWorld = Bukkit.getWorld(world);
				if (bukkitWorld == null) {
					continue;
				}
				if (gamemode.equalsIgnoreCase("adventure") || gamemode.equalsIgnoreCase("creative") || gamemode.equalsIgnoreCase("survival")) {
					ASInventory inventory = new ASInventory(GameMode.valueOf(gamemode), player, world, type);
					for (String stringSlot : file.getConfigurationSection(world + "." + gamemode).getKeys(false)) {
						Integer slot = Integer.valueOf(stringSlot);
						if (slot >= 100) {
							slot = 36 + (slot - 100);
						}
						ItemStack item = file.getItemStack(world + "." + gamemode + "." + stringSlot);
						inventory.set(slot, item);
					}
					inventories.add(inventory);
				}
			}
		}
		return inventories;
	}

}
