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
package com.turt2live.antishare.storage;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.tekkitcompat.ScheduleLayer;
import com.turt2live.antishare.tekkitcompat.ServerHas;
import com.turt2live.antishare.util.ASUtils;
import com.turt2live.antishare.util.events.TrackerList;

/**
 * Block Manager - Handles creative/survival blocks
 * 
 * @author turt2live
 */
public class BlockManager {

	/**
	 * AntiShare material - used for simplicity
	 * 
	 * @author turt2live
	 */
	static class ASMaterial {
		public Location location;
		public GameMode gamemode;
	}

	private AntiShare plugin;
	private CopyOnWriteArrayList<String> loadedChunks = new CopyOnWriteArrayList<String>();
	private ConcurrentMap<String, ChunkWrapper> wrappers = new ConcurrentHashMap<String, ChunkWrapper>();
	TrackerList tracked_creative;
	TrackerList tracked_survival;
	TrackerList tracked_adventure;
	private CopyOnWriteArrayList<ASMaterial> recentlyRemoved = new CopyOnWriteArrayList<ASMaterial>();
	private ConcurrentMap<String, EnhancedConfiguration> saveFiles = new ConcurrentHashMap<String, EnhancedConfiguration>();
	private boolean doneLastSave = false;
	private final File entitiesDir;
	private final File blocksDir;
	private final ConcurrentMap<String, Boolean> activeSaves = new ConcurrentHashMap<String, Boolean>();

	/**
	 * Creates a new block manager, also loads the block lists
	 */
	public BlockManager(){
		this.plugin = AntiShare.getInstance();

		// Setup files
		entitiesDir = new File(plugin.getDataFolder(), "data" + File.separator + "entities");
		blocksDir = new File(plugin.getDataFolder(), "data" + File.separator + "blocks");
		blocksDir.mkdirs();
		entitiesDir.mkdirs();

		// Load blocks
		load();
	}

	String chunkToString(Chunk chunk){
		return chunk.getX() + "." + chunk.getZ() + "." + chunk.getWorld().getName();
	}

	/**
	 * Loads a chunk into the block manager
	 * 
	 * @param chunk the chunk to load
	 */
	public void loadChunk(Chunk chunk){
		String str = chunkToString(chunk);
		ChunkWrapper wrapper = new ChunkWrapper(this, chunk);
		wrappers.put(str, wrapper);
		wrapper.load(blocksDir, entitiesDir);
		loadedChunks.add(str);
	}

	/**
	 * Unloads a chunk from the block manager
	 * 
	 * @param chunk the chunk to unload
	 */
	public void unloadChunk(Chunk chunk){
		String key = chunkToString(chunk);
		ChunkWrapper wrapper = wrappers.get(key);
		if(wrapper != null){
			String[] names = new String[6];
			for(int i = 0; i < names.length; i++){
				names[i] = key + i;
				activeSaves.put(names[i], false);
			}
			wrapper.save(names, false, false, blocksDir, entitiesDir);
			wrappers.remove(wrapper);
			loadedChunks.remove(key);
		}
	}

	/**
	 * Saves everything to disk
	 * 
	 * @param clear set to true to prepare for a reload
	 * @param load set to true to load everything after saving (reload)
	 */
	public void save(boolean clear, boolean load){
		// Load files
		blocksDir.mkdirs();
		entitiesDir.mkdirs();
		ASUtils.wipeFolder(blocksDir, loadedChunks);
		ASUtils.wipeFolder(entitiesDir, loadedChunks);
		doneLastSave = false;

		// Create savers
		for(String key : wrappers.keySet()){
			ChunkWrapper wrapper = wrappers.get(key);
			if(wrapper == null){
				continue;
			}
			String[] names = new String[6];
			for(int i = 0; i < names.length; i++){
				names[i] = key + i;
				activeSaves.put(names[i], false);
			}
			wrapper.save(names, load, clear, blocksDir, entitiesDir);
		}

		// BlockSaver handles telling BlockManager that it is done
	}

	EnhancedConfiguration getFile(File dir, String fname){
		EnhancedConfiguration ymlFile = null;
		if(!saveFiles.containsKey(fname)){
			File file = new File(dir, fname);
			ymlFile = new EnhancedConfiguration(file, AntiShare.getInstance());
			saveFiles.put(fname, ymlFile);
		}else{
			ymlFile = saveFiles.get(fname);
		}
		return ymlFile;
	}

	void markSaveAsDone(String list, ObjectSaver save){
		if(doneLastSave == true){
			return;
		}
		activeSaves.put(list, true);
		for(String key : activeSaves.keySet()){
			if(!activeSaves.get(key)){
				return;
			}
		}
		if(!plugin.getConfig().getBoolean("other.more-quiet-shutdown")){
			plugin.getLogger().info("[Block Manager] Saving files...");
		}
		for(String key : saveFiles.keySet()){
			saveFiles.get(key).save();
		}
		saveFiles.clear();
		if(save.getClear()){
			loadedChunks.clear();
			wrappers.clear();
		}
		if(save.getLoad()){
			load();
		}
		doneLastSave = true;
		activeSaves.clear();
	}

	/**
	 * Determines if the last save is done
	 * 
	 * @return true if done
	 */
	public boolean isSaveDone(){
		return doneLastSave || loadedChunks.size() <= 0 || wrappers.size() <= 0;
	}

	/**
	 * Gets the percentage of the save completed
	 * 
	 * @return the percent of the save completed (as a whole number, eg: 10)
	 */
	public int percentSaveDone(){
		if(isSaveDone()){
			return 100;
		}
		double total = 0;
		int divisor = 0;
		for(String key : wrappers.keySet()){
			divisor++;
			total += wrappers.get(key).percentDoneSave();
		}
		if(divisor <= 0){
			return 100;
		}
		Double avg = total / divisor;
		return avg.intValue();
	}

	/**
	 * Loads from disk
	 */
	public void load(){
		// Setup lists
		tracked_creative = new TrackerList("config.yml", "block-tracking.tracked-creative-blocks", plugin.getConfig().getString("block-tracking.tracked-creative-blocks").split(","));
		tracked_survival = new TrackerList("config.yml", "block-tracking.tracked-survival-blocks", plugin.getConfig().getString("block-tracking.tracked-survival-blocks").split(","));
		tracked_adventure = new TrackerList("config.yml", "block-tracking.tracked-adventure-blocks", plugin.getConfig().getString("block-tracking.tracked-adventure-blocks").split(","));

		// Load
		for(World world : plugin.getServer().getWorlds()){
			for(Chunk chunk : world.getLoadedChunks()){
				loadChunk(chunk);
			}
		}

		// Tell console what we loaded
		int cb = 0, ce = 0, sb = 0, se = 0, ab = 0, ae = 0;
		for(String key : wrappers.keySet()){
			ChunkWrapper wrapper = wrappers.get(key);
			cb += wrapper.creative_blocks.size();
			ce += wrapper.creative_entities.size();
			sb += wrapper.survival_blocks.size();
			se += wrapper.survival_entities.size();
			ab += wrapper.adventure_blocks.size();
			ae += wrapper.adventure_entities.size();
		}
		if(cb > 0){
			plugin.getLogger().info("Creative Blocks Loaded: " + cb);
		}
		if(sb > 0){
			plugin.getLogger().info("Survival Blocks Loaded: " + sb);
		}
		if(ab > 0){
			plugin.getLogger().info("Adventure Blocks Loaded: " + ab);
		}
		if(ce > 0){
			plugin.getLogger().info("Creative Entities Loaded: " + ce);
		}
		if(se > 0){
			plugin.getLogger().info("Survival Entities Loaded: " + se);
		}
		if(ae > 0){
			plugin.getLogger().info("Adventure Entities Loaded: " + ae);
		}
	}

	/**
	 * Reloads the manager
	 */
	public void reload(){
		save(true, true);
	}

	/**
	 * Adds a block to the database
	 * 
	 * @param type the block type
	 * @param block the block
	 */
	public void addBlock(GameMode type, Block block){
		switch (type){
		case CREATIVE:
			if(!tracked_creative.isTracked(block)){
				return;
			}
			break;
		case SURVIVAL:
			if(!tracked_survival.isTracked(block)){
				return;
			}
			break;
		default:
			if(ServerHas.adventureMode()){
				if(!tracked_adventure.isTracked(block)){
					return;
				}
			}
			break;
		}
		String c = chunkToString(block.getChunk());
		ChunkWrapper wrapper = wrappers.get(c);
		wrapper.addBlock(type, block);
	}

	/**
	 * Adds an entity to the database
	 * 
	 * @param type the entity type
	 * @param entity the entity
	 */
	public void addEntity(GameMode type, Entity entity){
		switch (type){
		case CREATIVE:
			if(!tracked_creative.isTracked(entity)){
				return;
			}
			break;
		case SURVIVAL:
			if(!tracked_survival.isTracked(entity)){
				return;
			}
			break;
		default:
			if(ServerHas.adventureMode()){
				if(!tracked_adventure.isTracked(entity)){
					return;
				}
			}
			break;
		}
		String c = chunkToString(entity.getLocation().getChunk());
		ChunkWrapper wrapper = wrappers.get(c);
		wrapper.addEntity(type, entity);
	}

	/**
	 * Adds an entity to the database
	 * 
	 * @param type the entity gamemode
	 * @param entityType the entity type
	 * @param entity the entity
	 */
	public void addEntity(GameMode type, Location entity, EntityType entityType){
		switch (type){
		case CREATIVE:
			if(!tracked_creative.isTracked(entityType)){
				return;
			}
			break;
		case SURVIVAL:
			if(!tracked_survival.isTracked(entityType)){
				return;
			}
			break;
		default:
			if(ServerHas.adventureMode()){
				if(!tracked_adventure.isTracked(entityType)){
					return;
				}
			}
			break;
		}
		String c = chunkToString(entity.getChunk());
		ChunkWrapper wrapper = wrappers.get(c);
		wrapper.addEntity(type, entity, entityType);
	}

	/**
	 * Removes an entity from the database
	 * 
	 * @param entity the entity
	 */
	public void removeEntity(Entity entity){
		GameMode type = getType(entity);
		if(type != null){
			ASMaterial material = new ASMaterial();
			material.gamemode = type;
			material.location = entity.getLocation();
			recentlyRemoved.add(material);
			String c = chunkToString(entity.getLocation().getChunk());
			ChunkWrapper wrapper = wrappers.get(c);
			wrapper.removeEntity(entity);
		}
	}

	/**
	 * Removes a block from the database
	 * 
	 * @param block the block
	 */
	public void removeBlock(Block block){
		GameMode type = getType(block);
		if(type != null){
			ASMaterial material = new ASMaterial();
			material.gamemode = type;
			material.location = block.getLocation();
			recentlyRemoved.add(material);
			String c = chunkToString(block.getChunk());
			ChunkWrapper wrapper = wrappers.get(c);
			wrapper.removeBlock(block);
		}
	}

	/**
	 * Moves a block in the system. This auto-detects type
	 * 
	 * @param oldLocation the old location
	 * @param newLocation the new location
	 */
	public void moveBlock(Location oldLocation, final Location newLocation){
		final GameMode type = getType(oldLocation.getBlock());
		Block oldBlock = oldLocation.getBlock();

		if(type == null){
			return;
		}

		// Remove old block
		removeBlock(oldBlock);

		// Start a thread to wait until the block changes
		final Material oldType = oldBlock.getType();
		ScheduleLayer.runTaskAsynchronously(plugin, new Runnable(){
			@Override
			public void run(){
				// Setup vars
				int runs = 0;
				int maxRuns = 10;
				long delay = 100;
				boolean updated = false;

				// Loop
				while (runs <= maxRuns && !updated){
					// Check block
					Block newBlock = newLocation.getBlock();
					if(newBlock.getType() == oldType){
						addBlock(type, newBlock);
						updated = true;
					}

					// Count and wait
					runs++;
					try{
						Thread.sleep(delay);
					}catch(InterruptedException e){
						AntiShare.getInstance().log("AntiShare encountered and error. Please report this to turt2live.", Level.SEVERE);
						e.printStackTrace();
					}
				}

				// Warn if not updated
				if(!updated){
					plugin.log("Move block took longer than " + (delay * maxRuns) + " milliseconds.", Level.SEVERE);
				}
			}
		});
	}

	/**
	 * Gets the gamemode associated with a block
	 * 
	 * @param block the block
	 * @return the gamemode, or null if no assignment
	 */
	public GameMode getType(Block block){
		String c = chunkToString(block.getChunk());
		ChunkWrapper wrapper = wrappers.get(c);
		return wrapper.getType(block);
	}

	/**
	 * Gets the gamemode associated with a entity
	 * 
	 * @param entity the entity
	 * @return the gamemode, or null if no assignment
	 */
	public GameMode getType(Entity entity){
		String c = chunkToString(entity.getLocation().getChunk());
		ChunkWrapper wrapper = wrappers.get(c);
		return wrapper.getType(entity);
	}

	/**
	 * Gets the Game Mode of a recently broken block at a location
	 * 
	 * @param location the location
	 * @return the Game Mode (or null if not applicable)
	 */
	public GameMode getRecentBreak(Location location){
		for(ASMaterial material : recentlyRemoved){
			Location l = material.location;
			if(Math.floor(l.getX()) == Math.floor(location.getX())
					&& Math.floor(l.getY()) == Math.floor(location.getY())
					&& Math.floor(l.getZ()) == Math.floor(location.getZ())
					&& l.getWorld().getName().equalsIgnoreCase(location.getWorld().getName())){
				return material.gamemode;
			}
		}
		return null;
	}

	String blockToString(Block block){
		return block.getChunk().getX() + ";" + block.getChunk().getZ() + ";" + block.getWorld().getName() + ";" + block.getX() + ";" + block.getY() + ";" + block.getZ();
	}

	String entityToString(Entity entity){
		Location location = entity.getLocation();
		return location.getChunk().getX() + ";" + location.getChunk().getZ() + ";" + location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + entity.getType().name();
	}

	String entityToString(Location entity, EntityType type){
		return entity.getChunk().getX() + ";" + entity.getChunk().getZ() + ";" + entity.getWorld().getName() + ";" + entity.getX() + ";" + entity.getY() + ";" + entity.getZ() + ";" + type.name();
	}

}
