package com.turt2live.antishare.inventory;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.AntiShare.LogType;
import com.turt2live.antishare.inventory.ASInventory.InventoryType;
import com.turt2live.antishare.regions.ASRegion;
import com.turt2live.antishare.storage.SQL;

/**
 * Manages inventories within AntiShare
 * 
 * @author turt2live
 */
public class InventoryManager {

	private ConcurrentHashMap<String, ASInventory> creative = new ConcurrentHashMap<String, ASInventory>();
	private ConcurrentHashMap<String, ASInventory> survival = new ConcurrentHashMap<String, ASInventory>();
	private ConcurrentHashMap<String, ASInventory> region = new ConcurrentHashMap<String, ASInventory>();
	private ConcurrentHashMap<String, TemporaryASInventory> playerTemp = new ConcurrentHashMap<String, TemporaryASInventory>();

	/**
	 * Creates a new Inventory Manager
	 */
	public InventoryManager(){
		load();
	}

	/**
	 * Loads a specific player from file
	 * 
	 * @param player the player
	 */
	public void loadPlayer(Player player){
		// Standard inventories
		List<ASInventory> list = ASInventory.generateInventory(player.getName(), InventoryType.PLAYER);
		for(ASInventory inventory : list){
			World world = inventory.getWorld();
			GameMode gamemode = inventory.getGameMode();
			switch (gamemode){
			case CREATIVE:
				creative.put(player.getName() + "." + world.getName(), inventory);
				break;
			case SURVIVAL:
				survival.put(player.getName() + "." + world.getName(), inventory);
				break;
			}
		}

		// Temporary inventories
		list = ASInventory.generateInventory(player.getName(), InventoryType.TEMPORARY);
		for(ASInventory inventory : list){
			TemporaryASInventory spec = new TemporaryASInventory(ASInventory.generate(player, InventoryType.PLAYER), inventory);
			playerTemp.put(player.getName(), spec);
		}
	}

	/**
	 * Cleans up player data
	 * 
	 * @param player the player
	 */
	public void releasePlayer(Player player){
		if(player == null){
			return;
		}

		// Release
		if(isInTemporary(player)){
			removeFromTemporary(player);
		}

		// Save
		switch (player.getGameMode()){
		case CREATIVE:
			saveCreativeInventory(player, player.getWorld());
			break;
		case SURVIVAL:
			saveSurvivalInventory(player, player.getWorld());
			break;
		}

		// Cleanup
		for(World world : Bukkit.getWorlds()){
			if(creative.get(player.getName() + "." + world.getName()) != null){
				creative.get(player.getName() + "." + world.getName()).save();
			}
			if(survival.get(player.getName() + "." + world.getName()) != null){
				survival.get(player.getName() + "." + world.getName()).save();
			}
			creative.remove(player.getName() + "." + world.getName());
			survival.remove(player.getName() + "." + world.getName());
		}
	}

	/**
	 * Sets a player to a temporary inventory, such as a region inventory
	 * 
	 * @param player the player
	 * @param inventory the temporary inventory
	 */
	@SuppressWarnings ("deprecation")
	public void setToTemporary(Player player, ASInventory inventory){
		// Save current inventory
		switch (player.getGameMode()){
		case CREATIVE:
			saveCreativeInventory(player, player.getWorld());
			break;
		case SURVIVAL:
			saveSurvivalInventory(player, player.getWorld());
			break;
		}

		// Set to temp
		TemporaryASInventory spec = new TemporaryASInventory(ASInventory.generate(player, InventoryType.PLAYER), inventory);
		playerTemp.put(player.getName(), spec);
		if(inventory == null){
			player.getInventory().clear();
			player.updateInventory();
		}else{
			inventory.setTo(player);
		}
	}

	/**
	 * Removes the player from their temporary inventory, discarding it
	 * 
	 * @param player the player
	 */
	public void removeFromTemporary(Player player){
		TemporaryASInventory inventory = playerTemp.get(player.getName());
		if(inventory != null){
			inventory.getLastInventory().setTo(player);
			playerTemp.remove(player.getName());
		}
	}

	/**
	 * Determines if a player is in a temporary inventory
	 * 
	 * @param player the player
	 * @return true if they are in temporary
	 */
	public boolean isInTemporary(Player player){
		return playerTemp.containsKey(player.getName());
	}

	/**
	 * Gets the temporary inventory handler for a player
	 * 
	 * @param player the player
	 * @return the temporary inventory handler, this can be null if the player doesn't have one
	 */
	public TemporaryASInventory getTemporaryInventoryHandler(Player player){
		return playerTemp.get(player.getName());
	}

	/**
	 * Saves a player's creative inventory
	 * 
	 * @param player the player
	 * @param world the world
	 */
	public void saveCreativeInventory(Player player, World world){
		creative.put(player.getName() + "." + world.getName(), ASInventory.generate(player, InventoryType.PLAYER));
	}

	/**
	 * Saves a player's survival inventory
	 * 
	 * @param player the player
	 * @param world the world
	 */
	public void saveSurvivalInventory(Player player, World world){
		survival.put(player.getName() + "." + world.getName(), ASInventory.generate(player, InventoryType.PLAYER));
	}

	/**
	 * Gets a player's creative inventory
	 * 
	 * @param player the player
	 * @return the inventory
	 * @param world the world
	 */
	public ASInventory getCreativeInventory(Player player, World world){
		ASInventory inventory = creative.get(player.getName() + "." + world.getName());
		if(inventory == null){
			inventory = new ASInventory(InventoryType.PLAYER, player.getName(), world, player.getGameMode());
			creative.put(player.getName() + "." + world.getName(), inventory);
		}
		return inventory;
	}

	/**
	 * Gets a player's survival inventory
	 * 
	 * @param player the player
	 * @return the inventory
	 * @param world the world
	 */
	public ASInventory getSurvivalInventory(Player player, World world){
		ASInventory inventory = survival.get(player.getName() + "." + world.getName());
		if(inventory == null){
			inventory = new ASInventory(InventoryType.PLAYER, player.getName(), world, player.getGameMode());
			survival.put(player.getName() + "." + world.getName(), inventory);
		}
		return inventory;
	}

	/**
	 * Gets a region's inventory
	 * 
	 * @param region the region
	 * @return the inventory
	 */
	public ASInventory getRegionInventory(ASRegion region){
		ASInventory inventory = new ASInventory(InventoryType.REGION, region.getUniqueID(), region.getWorld(), region.getGameMode());
		inventory.set(0, new ItemStack(Material.BOAT));
		return inventory;
		//return this.region.get(region.getUniqueID());
	}

	/**
	 * Loads the inventory manager
	 */
	public void load(){
		// Load players
		for(Player player : Bukkit.getOnlinePlayers()){
			loadPlayer(player);
		}

		// Loads regions
		for(ASRegion region : AntiShare.instance.getRegionManager().getAllRegions()){
			String UID = region.getUniqueID();
			List<ASInventory> inventory = ASInventory.generateInventory(UID, InventoryType.REGION);
			if(inventory != null){
				if(inventory.size() >= 1){
					region.setInventory(inventory.get(0));
				}else{
					region.setInventory(null);
				}
			}
		}

		// Status
		int loaded = creative.size() + survival.size() + region.size() + playerTemp.size();
		AntiShare.instance.getMessenger().log("Inventories loaded: " + loaded, Level.INFO, LogType.INFO);
	}

	/**
	 * Saves the inventory manager
	 */
	public void save(){
		// Save players
		for(Player player : Bukkit.getOnlinePlayers()){
			if(isInTemporary(player)){
				removeFromTemporary(player);
			}
			switch (player.getGameMode()){
			case CREATIVE:
				saveCreativeInventory(player, player.getWorld());
				break;
			case SURVIVAL:
				saveSurvivalInventory(player, player.getWorld());
				break;
			}
		}

		// Clear targets
		if(AntiShare.instance.useSQL()){
			AntiShare.instance.getSQL().wipeTable(SQL.INVENTORIES_TABLE);
		}else{
			for(InventoryType type : InventoryType.values()){
				File dir = new File(AntiShare.instance.getDataFolder(), "inventories" + File.separator + type.getRelativeFolderName());
				if(dir.listFiles() != null){
					for(File file : dir.listFiles()){
						file.delete();
					}
				}
			}
		}

		// Save inventories
		for(String key : creative.keySet()){
			creative.get(key).save();
		}
		for(String key : survival.keySet()){
			survival.get(key).save();
		}
		for(String key : region.keySet()){
			region.get(key).save();
		}
	}

	/**
	 * Reloads the inventory manager
	 */
	public void reload(){
		save();
		creative.clear();
		survival.clear();
		playerTemp.clear();
		region.clear();
		load();
	}
}
