package com.turt2live.antishare.util.generic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.inventory.ASInventory;
import com.turt2live.antishare.inventory.ASInventory.InventoryType;

public class SelfCompatibility {

	private static enum Compat{
		BLOCKS(0),
		PLAYER_DATA(5),
		INV_313(10),
		WORLD_CONF(15),
		CONFIG_520(20);

		public final int BYTE_POS;

		private Compat(int byt){
			BYTE_POS = byt;
		}
	}

	private static enum FileType{
		CONFIG, NOTIFICATIONS, REGION, MESSAGES, WORLD;
	}

	private static final String COMPAT_NAME = "compat.antishare";

	private static void noLongerNeedsUpdate(Compat compat){
		RandomAccessFile file = getFile();
		if(file != null){
			try{
				file.seek(compat.BYTE_POS);
				file.writeBoolean(false);
			}catch(IOException e){}
		}
	}

	private static boolean needsUpdate(Compat compat){
		RandomAccessFile file = getFile();
		if(file != null){
			try{
				file.seek(compat.BYTE_POS);
				return file.readBoolean();
			}catch(IOException e){}
			return true;
		}
		return true;
	}

	private static RandomAccessFile getFile(){
		File rfile = new File(AntiShare.getInstance().getDataFolder(), "data" + File.separator + COMPAT_NAME);
		if(!rfile.exists()){
			try{
				rfile.createNewFile();
			}catch(IOException e){}
		}
		try{
			RandomAccessFile file = new RandomAccessFile(rfile, "rw");
			return file;
		}catch(FileNotFoundException e){}
		return null;
	}

	/**
	 * Migrates the world configurations to their own folder
	 */
	public static void migrateWorldConfigurations(){
		if(!needsUpdate(Compat.WORLD_CONF)){
			return;
		}
		File directory = AntiShare.getInstance().getDataFolder();
		File newDir = new File(directory, "world_configurations");
		int files = 0;
		if(directory.listFiles() != null){
			for(File file : directory.listFiles(new FileFilter(){
				@Override
				public boolean accept(File arg0){
					if(arg0.getName().endsWith("_config.yml")){
						return true;
					}
					return false;
				}
			})){
				files++;
				file.renameTo(new File(newDir, file.getName()));
				file.delete();
			}
			if(files > 0){
				AntiShare.getInstance().log("World Configurations Migrated: " + files, Level.INFO);
			}
		}
		noLongerNeedsUpdate(Compat.WORLD_CONF);
	}

	/**
	 * Convert 4.4.0 to 4.4.1+ system
	 */
	public static void convertBlocks(){
		if(!needsUpdate(Compat.BLOCKS)){
			return;
		}
		int converted = 0;
		AntiShare plugin = AntiShare.getInstance();
		File dir = new File(plugin.getDataFolder(), "data");
		dir.mkdirs();
		File nDir = new File(plugin.getDataFolder(), "data" + File.separator + "blocks");
		nDir.mkdirs();
		File oldBlockFile = new File(dir, "blocks.yml");
		if(!oldBlockFile.exists()){
			return;
		}
		EnhancedConfiguration blocks = new EnhancedConfiguration(oldBlockFile, plugin);
		blocks.load();
		for(String key : blocks.getKeys(false)){
			String[] keyParts = key.split(";");
			Location location = new Location(Bukkit.getWorld(keyParts[3]), Double.parseDouble(keyParts[0]), Double.parseDouble(keyParts[1]), Double.parseDouble(keyParts[2]));
			if(Bukkit.getWorld(keyParts[3]) == null || location == null || location.getWorld() == null){
				continue;
			}
			Block block = location.getBlock();
			if(block == null){
				location.getChunk().load();
				block = location.getBlock();
			}
			GameMode gm = GameMode.valueOf(blocks.getString(key));
			AntiShare.getInstance().getBlockManager().addBlock(gm, block);
			converted++;
		}
		oldBlockFile.delete();
		if(converted > 0){
			plugin.getLogger().info("Blocks Converted: " + converted);
		}
		noLongerNeedsUpdate(Compat.BLOCKS);
	}

	/**
	 * Migrates player data from region_players to data/region_players
	 */
	public static void migratePlayerData(){
		if(!needsUpdate(Compat.PLAYER_DATA)){
			return;
		}
		AntiShare plugin = AntiShare.getInstance();
		File newSaveFolder = new File(plugin.getDataFolder(), "data" + File.separator + "region_players");
		File oldSaveFolder = new File(plugin.getDataFolder(), "region_players");
		newSaveFolder.mkdirs();
		if(oldSaveFolder.exists()){
			File[] files = oldSaveFolder.listFiles();
			if(files != null && files.length > 0){
				for(File file : files){
					file.renameTo(new File(newSaveFolder, file.getName()));
				}
				plugin.getLogger().info("Region Player Files Migrated: " + files.length);
			}
			oldSaveFolder.delete();
		}
		noLongerNeedsUpdate(Compat.PLAYER_DATA);
	}

	/**
	 * Converts 3.1.3 inventories to 3.2.0+ style
	 */
	public static void convert313Inventories(){
		if(!needsUpdate(Compat.INV_313)){
			return;
		}
		AntiShare plugin = AntiShare.getInstance();
		File[] files = new File(plugin.getDataFolder(), "inventories").listFiles();
		if(files != null){
			for(File file : files){
				EnhancedConfiguration inventory = new EnhancedConfiguration(file, plugin);
				inventory.load();
				String fname = file.getName();
				GameMode gamemode;
				if(fname.replace("_CREATIVE_", "").length() != fname.length()){
					gamemode = GameMode.CREATIVE;
				}else if(fname.replace("_SURVIVAL_", "").length() != fname.length()){
					gamemode = GameMode.SURVIVAL;
				}else{
					continue;
				}
				fname = fname.replace("_" + gamemode.name() + "_", "ANTI=SHARE"); // Unique character
				String[] nameparts = fname.split("\\.")[0].split("ANTI=SHARE");
				if(nameparts.length < 2){
					continue;
				}
				String playerName = nameparts[0];
				World world = plugin.getServer().getWorld(nameparts[1]);
				if(world == null){
					continue;
				}
				List<ASInventory> list = ASInventory.generateInventory(playerName, InventoryType.PLAYER);
				if(list.size() > 0){
					continue;
				}
				ASInventory newi = new ASInventory(InventoryType.PLAYER, playerName, world, gamemode);
				for(String key : inventory.getKeys(false)){
					ItemStack item = inventory.getItemStack(key);
					int slot = -1;
					try{
						slot = Integer.parseInt(key);
					}catch(NumberFormatException e){
						continue;
					}
					if(slot < 0){
						continue;
					}
					if(item != null && item.getType() != Material.AIR){
						newi.set(slot, item);
					}
				}
				newi.save();
			}
			for(File file : files){
				file.delete();
			}
			if(files.length > 0){
				plugin.getLogger().info("Player Inventories Converted: " + files.length);
			}
			noLongerNeedsUpdate(Compat.INV_313);
		}
	}

	/**
	 * Removes/Archives old inventories
	 */
	public static void cleanupOldInventories(){
		AntiShare plugin = AntiShare.getInstance();
		if(plugin.getConfig().getBoolean("settings.cleanup.use")){
			File timeFile = new File(plugin.getDataFolder(), "data" + File.separator + "lastCleanup");
			if(timeFile.exists()){
				try{
					BufferedReader in = new BufferedReader(new FileReader(timeFile));
					String line = in.readLine();
					int lastMS = Integer.parseInt(line);
					int hours = 3600000 * 6;
					if(System.currentTimeMillis() - lastMS < hours){
						return; // Don't clean
					}
					in.close();
				}catch(IOException e){}catch(NumberFormatException e){}
			}
			try{
				BufferedWriter out = new BufferedWriter(new FileWriter(timeFile, false));
				out.write(String.valueOf(System.currentTimeMillis()));
				out.close();
			}catch(IOException e){}
			long time = plugin.getConfig().getLong("settings.cleanup.after");
			boolean delete = plugin.getConfig().getString("settings.cleanup.method").equalsIgnoreCase("delete");
			File archiveLocation = new File(plugin.getDataFolder(), "archive" + File.separator + "inventories" + File.separator + "players");
			if(!delete && !archiveLocation.exists()){
				archiveLocation.mkdirs();
			}
			File[] files = new File(plugin.getDataFolder(), "inventories" + File.separator + InventoryType.PLAYER.getRelativeFolderName()).listFiles();
			int cleaned = 0;
			if(files != null){
				for(File file : files){
					String player = file.getName().split("\\.")[0];
					OfflinePlayer p = plugin.getServer().getOfflinePlayer(player);
					long diff = System.currentTimeMillis() - p.getLastPlayed();
					long days = diff / (24 * 60 * 60 * 1000);
					if(days >= time){
						if(delete){
							file.delete();
						}else{
							file.renameTo(new File(archiveLocation, file.getName()));
						}
						cleaned++;
					}
				}
			}
			if(cleaned > 0){
				plugin.getLogger().info("Player Inventories Archived/Deleted: " + cleaned);
			}
		}
	}

	/**
	 * Cleans YAML files
	 */
	public static void cleanupYAML(){
		if(!needsUpdate(Compat.CONFIG_520)){
			return;
		}
		int cleaned = 0;
		AntiShare plugin = AntiShare.getInstance();
		Map<String, FileType> files = new HashMap<String, FileType>();
		files.put("config.yml", FileType.CONFIG);
		files.put("messages.yml", FileType.MESSAGES);
		files.put("notifications.yml", FileType.NOTIFICATIONS);
		files.put("data" + File.separator + "regions", FileType.REGION);
		files.put("world_configurations", FileType.WORLD);
		for(String name : files.keySet()){
			File file = new File(plugin.getDataFolder(), name);
			if(file.isDirectory()){
				cleaned += cleanFolder(file, files.get(name));
			}else{
				cleanFile(file, files.get(name));
				cleaned++;
			}
		}
		if(cleaned > 0){
			plugin.getLogger().info("Configuration files cleaned: " + cleaned);
		}
		noLongerNeedsUpdate(Compat.CONFIG_520);
	}

	private static int cleanFolder(File folder, FileType type){
		int cleaned = 0;
		if(folder.listFiles() != null){
			for(File file : folder.listFiles()){
				if(file.getName().endsWith(".yml")){
					cleanFile(file, type);
					cleaned++;
				}
			}
		}
		return cleaned;
	}

	private static void cleanFile(File file, FileType type){
		AntiShare plugin = AntiShare.getInstance();
		File temp = new File(plugin.getDataFolder(), "temp");
		temp.mkdirs();
		EnhancedConfiguration local = new EnhancedConfiguration(new File(temp, "temp1"), plugin);
		switch (type){
		case CONFIG:
			local.loadDefaults(plugin.getResource("resources/config.yml"));
			break;
		case MESSAGES:
			local.loadDefaults(plugin.getResource("resources/messages.yml"));
			break;
		case NOTIFICATIONS:
			local.loadDefaults(plugin.getResource("resources/notifications.yml"));
			break;
		case REGION:
			local.loadDefaults(plugin.getResource("resources/region.yml"));
			break;
		case WORLD:
			local.loadDefaults(plugin.getResource("resources/world.yml"));
			break;
		}
		local.save();
		EnhancedConfiguration actual = new EnhancedConfiguration(file, plugin);
		actual.load();
		for(String key : actual.getKeys(true)){
			if(local.get(key) == null){
				actual.set(key, null);
			}
		}
		actual.save();
	}

}
