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
package com.turt2live.antishare.inventory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.tekkitcompat.ServerHas;

/**
 * AntiShare Inventory
 * 
 * @author turt2live
 */
public class ASInventory implements Cloneable {

	/**
	 * An enum to represent inventory types
	 * 
	 * @author turt2live
	 */
	public static enum InventoryType{
		PLAYER("players"),
		REGION("regions"),
		TEMPORARY("temporary"),
		ENDER("ender");

		private String relativeFolderName;

		private InventoryType(String relativeFolderName){
			this.relativeFolderName = relativeFolderName;
		}

		/**
		 * Gets the relative folder name
		 * 
		 * @return the folder
		 */
		public String getRelativeFolderName(){
			return relativeFolderName;
		}
	}

	/**
	 * Generates an AntiShare Inventory from a player
	 * 
	 * @param player the player
	 * @param type the inventory type to generate
	 * @return the inventory
	 */
	public static ASInventory generate(Player player, InventoryType type){
		ASInventory inventory = new ASInventory(type, player.getName(), player.getWorld(), player.getGameMode());
		if(type == InventoryType.ENDER && ServerHas.enderChests()){
			ItemStack[] contents = player.getEnderChest().getContents();
			int slot = 0;
			for(ItemStack item : contents){
				inventory.set(slot, item);
				slot++;
			}
		}else{
			ItemStack[] contents = player.getInventory().getContents();
			int slot = 0;
			for(ItemStack item : contents){
				inventory.set(slot, item);
				slot++;
			}
			contents = player.getInventory().getArmorContents();
			slot = 100;
			for(ItemStack item : contents){
				inventory.set(slot, item);
				slot++;
			}
		}
		return inventory;
	}

	/**
	 * Generates an inventory list
	 * 
	 * @param name inventory name
	 * @param type the Inventory Type
	 * @return the inventories
	 */
	public static List<ASInventory> generateInventory(String name, InventoryType type){
		// Setup
		List<ASInventory> inventories = new ArrayList<ASInventory>();

		// Configuration check
		if(!AntiShare.getInstance().getConfig().getBoolean("handled-actions.gamemode-inventories")){
			return inventories;
		}
		// Setup
		File dir = new File(AntiShare.getInstance().getDataFolder(), "inventories" + File.separator + type.getRelativeFolderName());
		dir.mkdirs();
		File saveFile = new File(dir, name + ".yml");
		if(!saveFile.exists()){
			return inventories;
		}
		EnhancedConfiguration file = new EnhancedConfiguration(saveFile, AntiShare.getInstance());
		file.load();

		// Load data
		// Structure: yml:world.gamemode.slot.properties
		List<String> removeWorlds = new ArrayList<String>();
		for(String world : file.getKeys(false)){
			for(String gamemode : file.getConfigurationSection(world).getKeys(false)){
				World worldV = Bukkit.getWorld(world);
				if(worldV == null){
					AntiShare.getInstance().log("World '" + world + "' does not exist (Inventory: " + type.name() + ", " + name + ".yml) AntiShare is ignoring this world.", Level.SEVERE);
					if(AntiShare.getInstance().getConfig().getBoolean("settings.remove-old-inventories")){
						AntiShare.getInstance().log("=== AntiShare is REMOVING this world from the player ===", Level.SEVERE);
						AntiShare.getInstance().log("This cannot be reversed. Check your settings if you don't like this.", Level.SEVERE);
						removeWorlds.add(world);
					}
					continue;
				}
				if((gamemode.equalsIgnoreCase("adventure") && ServerHas.adventureMode()) ||
						(gamemode.equalsIgnoreCase("creative") || gamemode.equalsIgnoreCase("survival"))){
					ASInventory inventory = new ASInventory(type, name, worldV, GameMode.valueOf(gamemode));
					for(String strSlot : file.getConfigurationSection(world + "." + gamemode).getKeys(false)){
						Integer slot = Integer.valueOf(strSlot);
						ItemStack item = file.getItemStack(world + "." + gamemode + "." + strSlot);
						inventory.set(slot, item);
					}
					inventories.add(inventory);
				}
			}
		}
		// Remove old worlds
		if(AntiShare.getInstance().getConfig().getBoolean("settings.remove-old-inventories")){ // Safe-guard check
			for(String world : removeWorlds){
				file.set(world, null);
			}
			file.save();
		}

		// return
		return inventories;
	}

	private HashMap<Integer, ItemStack> inventory = new HashMap<Integer, ItemStack>();
	private AntiShare plugin;
	private InventoryType type = InventoryType.PLAYER;
	private String inventoryName = "UNKNOWN";
	private World world;
	private GameMode gamemode;

	/**
	 * Creates a new AntiShare Inventory
	 * 
	 * @param type the type
	 * @param inventoryName the name
	 * @param world the world
	 * @param gamemode the gamemode
	 */
	public ASInventory(InventoryType type, String inventoryName, World world, GameMode gamemode){
		plugin = AntiShare.getInstance();
		this.type = type;
		this.inventoryName = inventoryName;
		this.world = world;
		this.gamemode = gamemode;
	}

	public boolean isEmpty(){
		for(Integer slot : inventory.keySet()){
			ItemStack stack = inventory.get(slot);
			if(stack != null && stack.getType() != Material.AIR){
				return false;
			}
		}
		return true;
	}

	/**
	 * Sets a slot to an item
	 * 
	 * @param slot the slot
	 * @param item the item
	 */
	public void set(int slot, ItemStack item){
		if(item == null){
			item = new ItemStack(Material.AIR, 1);
		}
		inventory.put(slot, item);
	}

	/**
	 * Sets the player's inventory to this inventory
	 * 
	 * @param player the player
	 */
	@SuppressWarnings ("deprecation")
	public void setTo(Player player){
		Inventory pInventory;
		if(type == InventoryType.ENDER && ServerHas.enderChests()){
			pInventory = player.getEnderChest();
		}else{
			pInventory = player.getInventory();
			ItemStack air = new ItemStack(Material.AIR);
			ItemStack[] armor = {air, air, air, air};
			((PlayerInventory) pInventory).setArmorContents(armor);
		}
		pInventory.clear();
		for(Integer slot : inventory.keySet()){
			ItemStack item = inventory.get(slot);
			if(item == null){
				inventory.put(slot, new ItemStack(Material.AIR, 1));
				item = new ItemStack(Material.AIR, 1);
			}
			if(slot < 100){
				pInventory.setItem(slot, item);
			}else{
				if(pInventory instanceof PlayerInventory){
					switch (slot){
					case 100:
						((PlayerInventory) pInventory).setBoots(item);
						break;
					case 101:
						((PlayerInventory) pInventory).setLeggings(item);
						break;
					case 102:
						((PlayerInventory) pInventory).setChestplate(item);
						break;
					case 103:
						((PlayerInventory) pInventory).setHelmet(item);
						break;
					}
				}
			}
		}
		player.updateInventory();
	}

	/**
	 * Saves the inventory to disk
	 */
	public void save(){
		// Configuration check
		if(!AntiShare.getInstance().getConfig().getBoolean("handled-actions.gamemode-inventories")){
			return;
		}
		// Setup
		File dir = new File(plugin.getDataFolder(), "inventories" + File.separator + type.getRelativeFolderName());
		dir.mkdirs();
		File saveFile = new File(dir, inventoryName + ".yml");
		EnhancedConfiguration file = new EnhancedConfiguration(saveFile, plugin);
		file.load();
		file.set(world.getName() + "." + gamemode.name(), null);

		// Save data
		// Structure: yml:world.gamemode.slot.properties
		for(Integer slot : inventory.keySet()){
			// Don't save AIR
			ItemStack item = inventory.get(slot);
			if(item == null || item.getType() == Material.AIR){
				continue;
			}

			// Save item
			file.set(world.getName() + "." + gamemode.name() + "." + String.valueOf(slot), item);
		}
		file.save();
	}

	/**
	 * Gets the world of this inventory
	 * 
	 * @return the world
	 */
	public World getWorld(){
		return world;
	}

	/**
	 * Gets the game mode of this inventory
	 * 
	 * @return the game mode
	 */
	public GameMode getGameMode(){
		return gamemode;
	}

	/**
	 * Changes the type of this inventory
	 * 
	 * @param type the new type
	 */
	public void setType(InventoryType type){
		this.type = type;
	}

	/**
	 * Gets the inventory type
	 * 
	 * @return the type
	 */
	public InventoryType getType(){
		return type;
	}

	@Override
	public ASInventory clone(){
		ASInventory newI = new ASInventory(this.type, this.inventoryName, this.world, this.gamemode);
		for(int slot : this.inventory.keySet()){
			newI.set(slot, this.inventory.get(slot));
		}
		return newI;
	}

	/**
	 * Sets the gamemode of the inventory
	 * 
	 * @param gamemode the game mode
	 */
	public void setGamemode(GameMode gamemode){
		this.gamemode = gamemode;
	}

	/**
	 * Set the world this inventory is bound to
	 * 
	 * @param world the world
	 */
	public void setWorld(World world){
		this.world = world;
	}

	/**
	 * Gets the size of this inventory
	 * 
	 * @return inventory size
	 */
	public int getSize(){
		switch (this.type){
		case ENDER:
			return 27;
		case PLAYER:
		case REGION:
		case TEMPORARY:
		default:
			return 36;
		}
	}

	void populateOtherInventory(Inventory inventory){
		inventory.clear();
		for(Integer slot : this.inventory.keySet()){
			inventory.setItem(slot, this.inventory.get(slot));
		}
	}

	void populateSelf(Inventory inventory){
		for(int i = 0; i < inventory.getSize(); i++){
			ItemStack item = inventory.getItem(i);
			if(item == null || item.getType() == Material.AIR){
				continue;
			}
			set(i, item);
		}
		inventory.clear();
	}

	/**
	 * Gets the name of this inventory
	 * 
	 * @return the inventory name
	 */
	public String getName(){
		return inventoryName;
	}

}
