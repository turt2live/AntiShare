package com.turt2live.antishare;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.turt2live.antishare.SQL.SQLManager;

public class ASBlockRegistry {

	static File blockListing = new File(Bukkit.getServer().getPluginManager().getPlugin("AntiShare").getDataFolder(), "blocks");

	public static boolean isBlockCreative(Block block){
		if(block == null){
			return false;
		}
		AntiShare plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
		boolean skip = false;
		if(plugin.getConfig().getBoolean("SQL.use") && plugin.getSQLManager() != null){
			if(plugin.getSQLManager().isConnected()){
				SQLManager sql = plugin.getSQLManager();
				ResultSet blocks = sql.getQuery("SELECT * FROM AntiShare_Blocks WHERE blockX='" + block.getX() + "' AND blockY='" + block.getY() + "' AND blockZ='" + block.getZ() + "' AND world='" + block.getWorld().getName() + "'");
				if(blocks != null){
					try{
						while (blocks.next()){
							if(blocks.getString("blockName").equalsIgnoreCase("AIR")){
								return false;
							}else{
								return true;
							}
						}
						skip = true;
					}catch(SQLException e){
						plugin.log.severe("[" + plugin.getDescription().getFullName() + "] Cannot handle blocks: " + e.getMessage());
					}
				}else{
					skip = true;
				}
			}
		}
		if(skip){
			return false;
		}
		blockListing.mkdirs();
		File blockList = new File(blockListing, block.getWorld().getName() + "_" + block.getChunk().getX() + "_" + block.getChunk().getZ() + ".yml");
		try{
			if(!blockList.exists()){
				blockList.createNewFile();
			}
			FileConfiguration blocks = new YamlConfiguration();
			blocks.load(blockList);
			String yamlLocation = block.getX() + "." + block.getY() + "." + block.getZ();
			if(blocks.getString(yamlLocation) != null){
				if(!blocks.getString(yamlLocation).equalsIgnoreCase("AIR")){
					return true;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	public static void saveCreativeBlock(Block block, String usernameWhoPlaced){
		if(block == null){
			return;
		}
		AntiShare plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
		if(!trackBlock(block, plugin)){
			return;
		}
		boolean skip = false;
		if(plugin.getConfig().getBoolean("SQL.use") && plugin.getSQLManager() != null){
			if(plugin.getSQLManager().isConnected()){
				SQLManager sql = plugin.getSQLManager();
				sql.insertQuery("INSERT INTO AntiShare_Blocks (username_placer, blockX, blockY, blockZ, blockID, blockName, world) " +
						"VALUES ('" + usernameWhoPlaced + "', '" + block.getX() + "', '" + block.getY() + "', '" + block.getZ() + "', '" + block.getTypeId() + "', '" + block.getType().name() + "', '" + block.getWorld().getName() + "')");
				skip = true;
			}
		}
		if(skip){
			return;
		}
		blockListing.mkdirs();
		File blockList = new File(blockListing, block.getWorld().getName() + "_" + block.getChunk().getX() + "_" + block.getChunk().getZ() + ".yml");
		try{
			if(!blockList.exists()){
				blockList.createNewFile();
			}
			FileConfiguration blocks = new YamlConfiguration();
			blocks.load(blockList);
			String yamlLocation = block.getX() + "." + block.getY() + "." + block.getZ();
			blocks.set(yamlLocation, block.getType().name());
			blocks.save(blockList);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static boolean trackBlock(Block block, AntiShare plugin){
		String trackedBlocks[] = plugin.getConfig().getString("other.tracked-blocks").split(" ");
		for(String tblock : trackedBlocks){
			if(tblock.equalsIgnoreCase(block.getTypeId() + "")){
				return true;
			}
		}
		return false;
	}

	public static void unregisterCreativeBlock(Block block){
		if(block == null){
			return;
		}
		AntiShare plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
		boolean skip = false;
		if(plugin.getConfig().getBoolean("SQL.use") && plugin.getSQLManager() != null){
			if(plugin.getSQLManager().isConnected()){
				SQLManager sql = plugin.getSQLManager();
				ResultSet blocks = sql.getQuery("SELECT * FROM AntiShare_Blocks WHERE blockX='" + block.getX() + "' AND blockY='" + block.getY() + "' AND blockZ='" + block.getZ() + "' AND world='" + block.getWorld().getName() + "'");
				if(blocks != null){
					try{
						while (blocks.next()){
							int id = blocks.getInt("id");
							sql.deleteQuery("DELETE FROM AntiShare_Blocks WHERE id='" + id + "' LIMIT 1");
						}
						skip = true;
					}catch(SQLException e){
						plugin.log.severe("[" + plugin.getDescription().getFullName() + "] Cannot handle blocks: " + e.getMessage());
					}
				}else{
					skip = true;
				}
			}
		}
		if(skip){
			return;
		}
		blockListing.mkdirs();
		File blockList = new File(blockListing, block.getWorld().getName() + "_" + block.getChunk().getX() + "_" + block.getChunk().getZ() + ".yml");
		try{
			if(!blockList.exists()){
				blockList.createNewFile();
			}
			FileConfiguration blocks = new YamlConfiguration();
			blocks.load(blockList);
			String yamlLocation = block.getX() + "." + block.getY() + "." + block.getZ();
			blocks.set(yamlLocation, "AIR"); //Cause now it is air...
			blocks.save(blockList);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
