package com.turt2live.antishare.storage;

import java.io.File;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.SQL.SQLManager;

public class CreativeBlockSaver {

	private static File blockListing = new File(Bukkit.getServer().getPluginManager().getPlugin("AntiShare").getDataFolder(), "blocks");
	private Vector<Block> blocks;
	private AntiShare plugin;

	public CreativeBlockSaver(Vector<Block> blocks, AntiShare plugin){
		this.blocks = blocks;
		this.plugin = plugin;
	}

	public void save(){
		for(Block block : blocks){
			boolean skip = false;
			if(plugin.getConfig().getBoolean("SQL.use") && plugin.getSQLManager() != null){
				if(plugin.getSQLManager().isConnected()){
					SQLManager sql = plugin.getSQLManager();
					sql.insertQuery("INSERT INTO AntiShare_Blocks (blockX, blockY, blockZ, blockID, blockName, world) " +
							"VALUES ('" + block.getX() + "', '" + block.getY() + "', '" + block.getZ() + "', '" + block.getTypeId() + "', '" + block.getType().name() + "', '" + block.getWorld().getName() + "')");
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
	}
}
