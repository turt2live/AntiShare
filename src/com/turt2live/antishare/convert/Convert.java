package com.turt2live.antishare.convert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.AntiShare.LogType;
import com.turt2live.antishare.ErrorLog;
import com.turt2live.antishare.inventory.ASInventory;
import com.turt2live.antishare.inventory.ASInventory.InventoryType;
import com.turt2live.antishare.storage.SQL;

/**
 * Main Converter Class
 * 
 * @author turt2live
 */
public class Convert {

	private static long start = 0L;
	private static int inventories = 0;
	private static int regions = 0;

	/**
	 * Starts the timer
	 */
	public static void start(){
		start = System.currentTimeMillis();
	}

	/**
	 * Stops the timer, displaying results
	 */
	public static void end(){
		if(inventories > 0 || regions > 0){
			long elapsed = System.currentTimeMillis() - start;
			AntiShare.getInstance().getLogger().info("Converted " + inventories + " inventories and " + regions + " regions in " + elapsed + " milliseconds.");
		}
	}

	/**
	 * Converts the configuration format to 3.2.0b from 3.1.3 (or 3.2.0a)
	 */
	public static void convertConfig313to320b(){
		AntiShare plugin = AntiShare.getInstance();

		// Check if we need to updated
		plugin.getConfig().loadDefaults(plugin.getResource("resources/config.yml"));
		if((!plugin.getConfig().fileExists() || !plugin.getConfig().checkDefaults()) && plugin.getConfig().getConfigurationSection("events") != null){

			// Backup the current configuration
			try{
				BufferedReader in = new BufferedReader(new FileReader(new File(plugin.getDataFolder(), "config.yml")));
				BufferedWriter out = new BufferedWriter(new FileWriter(new File(plugin.getDataFolder(), "config-backup-3.1.3.yml")));
				String line;
				while ((line = in.readLine()) != null){
					out.write(line + "\r\n");
				}
				out.close();
				in.close();
			}catch(Exception e){
				AntiShare.getInstance().getMessenger().log("AntiShare encountered and error. Please report this to turt2live.", Level.SEVERE, LogType.ERROR);
				AntiShare.getInstance().getMessenger().log("Please see " + ErrorLog.print(e) + " for the full error.", Level.SEVERE, LogType.ERROR);
			}

			// Event lists
			String blockBreakList = plugin.getConfig().getString("events.block_break");
			String blockPlaceList = plugin.getConfig().getString("events.block_place");
			String deathList = plugin.getConfig().getString("events.death");
			String dropList = plugin.getConfig().getString("events.drop_item");
			String interactList = plugin.getConfig().getString("events.interact");
			String commandsList = plugin.getConfig().getString("events.commands");

			// "Hazards" convert
			if(!plugin.getConfig().getBoolean("hazards.allow_eggs")){
				interactList = (interactList == null ? "0" : interactList) + ", egg, 383";
			}
			if(!plugin.getConfig().getBoolean("hazards.allow_exp_bottle")){
				interactList = (interactList == null ? "0" : interactList) + ", exp bottle";
			}
			if(!plugin.getConfig().getBoolean("hazards.allow_buckets")){
				interactList = (interactList == null ? "0" : interactList) + ", lava bucket, water bucket";
			}
			if(!plugin.getConfig().getBoolean("hazards.allow_fire_charge")){
				interactList = (interactList == null ? "0" : interactList) + ", fireball";
			}
			if(!plugin.getConfig().getBoolean("hazards.allow_flint")){
				interactList = (interactList == null ? "0" : interactList) + ", flint and steel";
			}
			if(!plugin.getConfig().getBoolean("hazards.allow_bedrock")){
				blockPlaceList = (blockPlaceList == null ? "0" : blockPlaceList) + ", bedrock";
			}
			if(!plugin.getConfig().getBoolean("hazards.allow_tnt")){
				blockPlaceList = (blockPlaceList == null ? "0" : blockPlaceList) + ", tnt";
			}

			// We ignore everything else for security reasons, and we want the 
			// users to review the config to double check that the lists exported
			// correctly.

			// Wipe old config
			for(String key : plugin.getConfig().getKeys(true)){
				if(plugin.getConfig().getString(key) != null){
					plugin.getConfig().set(key, null);
				}
			}

			// Save new defaults
			plugin.getConfig().saveDefaults();

			// Set new values
			plugin.getConfig().set("blocked-lists.block-place", blockPlaceList);
			plugin.getConfig().set("blocked-lists.block-break", blockBreakList);
			plugin.getConfig().set("blocked-lists.dropped-items-on-death", deathList);
			plugin.getConfig().set("blocked-lists.use-items", interactList);
			plugin.getConfig().set("blocked-lists.dropped-items", dropList);
			plugin.getConfig().set("blocked-lists.commands", commandsList);

			// Save
			plugin.getConfig().save();
		}
		plugin.getConfig().load(); // Failsafe
	}

	/**
	 * Converts the general data to 3.2.0b format (inventories and regions) from 3.2.0a or 3.1.3 format
	 */
	public static void convert313to320b(){
		AntiShare plugin = AntiShare.getInstance();

		// ############## FLAT FILE
		// Convert Inventories
		// Stepped converter (1.0.X -> pre-3.0.0 -> 3.2.0a -> 3.2.0b)
		File saveDir = new File(plugin.getDataFolder(), "inventories");
		boolean recheck = false;
		String world = Bukkit.getWorlds().get(0).getName();
		if(saveDir.exists()){
			if(saveDir.listFiles() != null){
				for(File f : saveDir.listFiles()){
					if(f.getName().endsWith("CREATIVE.yml")
							|| f.getName().endsWith("SURVIVAL.yml")){
						File newName = new File(f.getParent(), f.getName().replace("SURVIVAL", "SURVIVAL_" + world).replace("CREATIVE", "CREATIVE_" + world));
						f.renameTo(newName);
						recheck = true;
					}
				}
			}
		}
		if(recheck){
			List<File> delete = new ArrayList<File>();
			if(saveDir.listFiles() != null){
				for(File file : saveDir.listFiles(new FileFilter(){
					@Override
					public boolean accept(File file){
						if(file.getName().endsWith(".yml")){
							return true;
						}
						return false;
					}
				})){
					OldInventoryFormat old = new OldInventoryFormat(file);
					if(old.isValid()){
						old.split();
						ConcurrentHashMap<Integer, ItemStack> inventory = old.getInventory();
						String playername = old.getPlayerName();
						World bukkitWorld = old.getWorld();
						GameMode gamemode = old.getGameMode();
						ASInventory antishareInventory = new ASInventory(InventoryType.PLAYER, playername, bukkitWorld, gamemode);
						// Skip 3.2.0a-3.2.0b format by parsing directly
						for(Integer slot : inventory.keySet()){
							antishareInventory.set(slot, inventory.get(slot));
						}
						antishareInventory.save();
						delete.add(file);
						inventories++;
					}
				}
			}
			for(File file : delete){
				file.delete();
			}
		}

		// Convert regions
		// Luckily this is really easy, just  move them
		File oldDir = new File(plugin.getDataFolder(), "regions");
		File newDir = new File(plugin.getDataFolder() + File.separator + "data", "regions");
		newDir.mkdirs();
		if(oldDir.listFiles() != null){
			for(File f : oldDir.listFiles()){
				File newFile = new File(newDir, f.getName());
				f.renameTo(newFile);
				regions++;
			}
		}
		oldDir.delete();

		// ############## SQL
		if(!plugin.useSQL()){
			return;
		}

		// Convert Regions
		// Luckily, regions are just a simple transfer
		if(plugin.getSQL().tableExists("AntiShare_Regions")){
			try{
				ResultSet oldRegions = plugin.getSQL().getQuery(plugin.getSQL().getConnection().prepareStatement("SELECT * FROM AntiShare_Regions"));
				if(oldRegions != null){
					while (oldRegions.next()){
						PreparedStatement statement = plugin.getSQL().getConnection().prepareStatement("INSERT INTO " + SQL.REGIONS_TABLE + " (regionName, mix, miy, miz, max, may, maz, creator, gamemode, showEnter, showExit, world, uniqueID, enterMessage, exitMessage) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
						statement.setString(1, oldRegions.getString("regionName"));
						statement.setDouble(2, oldRegions.getDouble("mix"));
						statement.setDouble(3, oldRegions.getDouble("miy"));
						statement.setDouble(4, oldRegions.getDouble("miz"));
						statement.setDouble(5, oldRegions.getDouble("max"));
						statement.setDouble(6, oldRegions.getDouble("may"));
						statement.setDouble(7, oldRegions.getDouble("maz"));
						statement.setString(8, oldRegions.getString("creator"));
						statement.setString(9, oldRegions.getString("gamemode"));
						statement.setInt(10, oldRegions.getInt("showEnter"));
						statement.setInt(11, oldRegions.getInt("showExit"));
						statement.setString(12, oldRegions.getString("world"));
						statement.setString(13, oldRegions.getString("uniqueID"));
						statement.setString(14, oldRegions.getString("enterMessage"));
						statement.setString(15, oldRegions.getString("exitMessage"));
						plugin.getSQL().insertQuery(statement);
						regions++;
					}
					plugin.getSQL().deleteQuery(plugin.getSQL().getConnection().prepareStatement("DROP TABLE AntiShare_Regions"));
				}
			}catch(SQLException e){
				AntiShare.getInstance().getMessenger().log("AntiShare encountered and error. Please report this to turt2live.", Level.SEVERE, LogType.ERROR);
				AntiShare.getInstance().getMessenger().log("Please see " + ErrorLog.print(e) + " for the full error.", Level.SEVERE, LogType.ERROR);
			}
		}

		// Convert Inventories
		if(plugin.getSQL().tableExists("AntiShare_Inventory")){
			try{
				ResultSet results = plugin.getSQL().getQuery(plugin.getSQL().getConnection().prepareStatement("SELECT * FROM AntiShare_Inventory"));
				if(results != null){
					while (results.next()){
						PreparedStatement statement = plugin.getSQL().getConnection().prepareStatement("INSERT INTO " + SQL.INVENTORIES_TABLE + " (type, name, gamemode, world, slot, itemID, itemName, itemDurability, itemAmount, itemData, itemEnchant) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
						statement.setString(1, InventoryType.PLAYER.name());
						statement.setString(2, results.getString("username"));
						statement.setString(3, results.getString("gamemode"));
						statement.setString(4, results.getString("world"));
						statement.setInt(5, results.getInt("slot"));
						statement.setInt(6, results.getInt("itemID"));
						statement.setString(7, results.getString("itemName"));
						statement.setInt(8, results.getInt("itemDurability"));
						statement.setInt(9, results.getInt("itemAmount"));
						statement.setInt(10, results.getInt("itemData"));
						statement.setString(11, results.getString("itemEnchant"));
						plugin.getSQL().insertQuery(statement);
						inventories++;
					}
					plugin.getSQL().deleteQuery(plugin.getSQL().getConnection().prepareStatement("DROP TABLE AntiShare_Inventories"));
				}
			}catch(SQLException e){
				AntiShare.getInstance().getMessenger().log("AntiShare encountered and error. Please report this to turt2live.", Level.SEVERE, LogType.ERROR);
				AntiShare.getInstance().getMessenger().log("Please see " + ErrorLog.print(e) + " for the full error.", Level.SEVERE, LogType.ERROR);
			}
		}
		if(plugin.getSQL().tableExists("AntiShare_MiscInventory")){
			try{
				ResultSet results = plugin.getSQL().getQuery(plugin.getSQL().getConnection().prepareStatement("SELECT * FROM AntiShare_MiscInventory"));
				if(results != null){
					while (results.next()){
						PreparedStatement statement = plugin.getSQL().getConnection().prepareStatement("INSERT INTO " + SQL.INVENTORIES_TABLE + " (type, name, gamemode, world, slot, itemID, itemName, itemDurability, itemAmount, itemData, itemEnchant) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
						statement.setString(1, InventoryType.REGION.name());
						statement.setString(2, results.getString("uniqueID"));
						statement.setString(3, GameMode.CREATIVE.name()); // Not used, therefore we can use anything
						statement.setString(4, Bukkit.getWorlds().get(0).getName()); // Not used, therefore we can use anything
						statement.setInt(5, results.getInt("slot"));
						statement.setInt(6, results.getInt("itemID"));
						statement.setString(7, results.getString("itemName"));
						statement.setInt(8, results.getInt("itemDurability"));
						statement.setInt(9, results.getInt("itemAmount"));
						statement.setInt(10, results.getInt("itemData"));
						statement.setString(11, results.getString("itemEnchant"));
						plugin.getSQL().insertQuery(statement);
						inventories++;
					}
					plugin.getSQL().deleteQuery(plugin.getSQL().getConnection().prepareStatement("DROP TABLE AntiShare_MiscInventories"));
				}
			}catch(SQLException e){
				AntiShare.getInstance().getMessenger().log("AntiShare encountered and error. Please report this to turt2live.", Level.SEVERE, LogType.ERROR);
				AntiShare.getInstance().getMessenger().log("Please see " + ErrorLog.print(e) + " for the full error.", Level.SEVERE, LogType.ERROR);
			}
		}

		// Cleanup other tables
		if(plugin.getSQL().tableExists("AntiShare_RegionInfo")){
			try{
				plugin.getSQL().deleteQuery(plugin.getSQL().getConnection().prepareStatement("DROP TABLE AntiShare_RegionInfo"));
			}catch(SQLException e){
				AntiShare.getInstance().getMessenger().log("AntiShare encountered and error. Please report this to turt2live.", Level.SEVERE, LogType.ERROR);
				AntiShare.getInstance().getMessenger().log("Please see " + ErrorLog.print(e) + " for the full error.", Level.SEVERE, LogType.ERROR);
			}
		}
	}

}
