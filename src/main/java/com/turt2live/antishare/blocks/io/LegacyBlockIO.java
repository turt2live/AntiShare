package com.turt2live.antishare.blocks.io;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.manager.ChunkWrapper;

/**
 * Legacy support class for block/entity operations
 * 
 * @author turt2live
 */
public class LegacyBlockIO{

	@Deprecated
	/**
	 * Loads a location from a string
	 * @param world the world
	 * @param string the string
	 * @return a location
	 */
	public static Location locationFromString(World world, String string){
		String[] parts = string.split(";");
		return new Location(world, Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
	}

	@Deprecated
	/**
	 * Loads an EntityType from string
	 * @param string the string
	 * @return an entity type, or null if not found
	 */
	public static EntityType entityFromString(String string){
		String[] parts = string.split(";");
		EntityType e = null;
		try{
			e = EntityType.fromName(parts[6]);
		}catch(Exception ex){}
		return e;
	}

	/**
	 * Loads a YAML-Styled block/entity file (Pre-5.4.0)
	 * 
	 * @param isBlock true for block file, false otherwise
	 * @param file the file to load
	 * @param wrapper the wrapper to load into
	 */
	public static void load(boolean isBlock, File file, ChunkWrapper wrapper){
		AntiShare plugin = AntiShare.p;
		EnhancedConfiguration blocks = new EnhancedConfiguration(file, plugin);
		blocks.load();
		for(String key : blocks.getKeys(false)){
			String[] keyParts = key.split(";");
			if(keyParts.length < (isBlock ? 3 : 4)){
				plugin.getLogger().severe(plugin.getMessages().getMessage("bad-file", file.getAbsolutePath()));
				continue;
			}
			Location location = new Location(Bukkit.getWorld(keyParts[3]), Double.parseDouble(keyParts[0]), Double.parseDouble(keyParts[1]), Double.parseDouble(keyParts[2]));
			if(Bukkit.getWorld(keyParts[3]) == null || location == null || location.getWorld() == null){
				continue;
			}
			EntityType entityType = null;
			if(keyParts.length > 4){
				try{
					entityType = EntityType.fromName(keyParts[4]);
				}catch(Exception e){ // Prevents messy consoles
					entityType = null;
				}
			}
			GameMode gamemode = GameMode.valueOf(blocks.getString(key));
			if(isBlock){
				Block block = location.getBlock();
				if(block == null){
					location.getChunk().load();
					block = location.getBlock();
				}
				wrapper.addBlock(gamemode, block);
			}else{
				if(entityType == null){
					plugin.getLogger().severe(plugin.getMessages().getMessage("bad-file", file.getAbsolutePath()));
					continue;
				}
				wrapper.addEntity(gamemode, location, entityType);
			}
		}
	}

}
