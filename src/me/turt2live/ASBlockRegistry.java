package me.turt2live;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ASBlockRegistry {

	static File	blockListing	= new File(AntiShare.getSaveFolder(), "blocks");

	public static void saveCreativeBlock(Block block) {
		if (block == null) return;
		blockListing.mkdirs();
		File blockList = new File(blockListing, block.getChunk().getX() + "_" + block.getChunk().getZ() + ".yml");
		try {
			if (!blockList.exists()) blockList.createNewFile();
			FileConfiguration blocks = new YamlConfiguration();
			blocks.load(blockList);
			String yamlLocation = block.getX() + "." + block.getY() + "." + block.getZ();
			blocks.set(yamlLocation, block.getType().name());
			blocks.save(blockList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isBlockCreative(Block block) {
		if (block == null) return false;
		blockListing.mkdirs();
		File blockList = new File(blockListing, block.getChunk().getX() + "_" + block.getChunk().getZ() + ".yml");
		try {
			if (!blockList.exists()) blockList.createNewFile();
			FileConfiguration blocks = new YamlConfiguration();
			blocks.load(blockList);
			String yamlLocation = block.getX() + "." + block.getY() + "." + block.getZ();
			if (blocks.getString(yamlLocation) != null) if (!blocks.getString(yamlLocation).equalsIgnoreCase("AIR")) return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void unregisterCreativeBlock(Block block) {
		if (block == null) return;
		blockListing.mkdirs();
		File blockList = new File(blockListing, block.getChunk().getX() + "_" + block.getChunk().getZ() + ".yml");
		try {
			if (!blockList.exists()) blockList.createNewFile();
			FileConfiguration blocks = new YamlConfiguration();
			blocks.load(blockList);
			String yamlLocation = block.getX() + "." + block.getY() + "." + block.getZ();
			blocks.set(yamlLocation, "AIR"); //Cause now it is air...
			blocks.save(blockList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//For when I screw up...

	public static void saveCreativeBlock(Location location) {
		if (location == null) return;
		saveCreativeBlock(location.getBlock());
	}

	public static boolean isBlockCreative(Location location) {
		if (location == null) return false;
		return isBlockCreative(location.getBlock());
	}

	public static void unregisterCreativeBlock(Location location) {
		if (location == null) return;
		unregisterCreativeBlock(location.getBlock());
	}
}
