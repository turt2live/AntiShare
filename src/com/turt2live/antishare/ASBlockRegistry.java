package com.turt2live.antishare;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

public class ASBlockRegistry {

	static File blockListing = new File(Bukkit.getServer().getPluginManager().getPlugin("AntiShare").getDataFolder(), "blocks");

	public static boolean isBlockCreative(Block block){
		AntiShare plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
		return plugin.storage.isCreativeBlock(block, BlockedType.CREATIVE_BLOCK_PLACE, block.getWorld());
	}

	public static void saveCreativeBlock(Block block){
		AntiShare plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
		plugin.storage.saveCreativeBlock(block, BlockedType.CREATIVE_BLOCK_PLACE, block.getWorld());
	}

	public static void unregisterCreativeBlock(Block block){
		AntiShare plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
		plugin.storage.saveCreativeBlock(block, BlockedType.CREATIVE_BLOCK_BREAK, block.getWorld());
	}
}
