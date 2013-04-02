/*******************************************************************************
 * Copyright (c) 2013 Travis Ralston.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import com.turt2live.antishare.AntiShare;
import com.turt2live.materials.MaterialAPI;

/**
 * Block Manager
 * 
 * @author turt2live
 */
//TODO: Schedule for rewrite
public class BlockManager{

	static class ASMaterial{
		public Location location;
		public GameMode gamemode;
		public long added;
	}

	private final File entitiesDir;
	private final File blocksDir;
	private final CopyOnWriteArrayList<ASMaterial> recentlyRemoved = new CopyOnWriteArrayList<ASMaterial>();
	private final ConcurrentMap<String, ChunkWrapper> wrappers = new ConcurrentHashMap<String, ChunkWrapper>();
	private boolean doneSave = false;
	private int percent = 0;
	private AntiShare plugin = AntiShare.p;

	/**
	 * Creates a new Block Manager
	 */
	public BlockManager(){
		// Setup files
		entitiesDir = new File(plugin.getDataFolder(), "data" + File.separator + "entities");
		blocksDir = new File(plugin.getDataFolder(), "data" + File.separator + "blocks");

		// Start cleanup
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

			@Override
			public void run(){
				List<ASMaterial> r = new ArrayList<ASMaterial>();
				for(ASMaterial m : recentlyRemoved){
					if(System.currentTimeMillis() - m.added >= 1000){
						r.add(m);
					}
				}
				recentlyRemoved.removeAll(r);
			}
		}, 0, 20 * 5);
	}

	/**
	 * Loads the block manager
	 */
	public void load(){
		// Load
		wrappers.clear();
		for(World world : plugin.getServer().getWorlds()){
			for(Chunk chunk : world.getLoadedChunks()){
				loadChunk(chunk);
			}
		}

		// Tell console what we loaded
		int cb = 0, ce = 0, sb = 0, se = 0, ab = 0, ae = 0;
		for(String key : wrappers.keySet()){
			ChunkWrapper wrapper = wrappers.get(key);
			cb += wrapper.creativeBlocks.size();
			ce += wrapper.creativeEntities.size();
			sb += wrapper.survivalBlocks.size();
			se += wrapper.survivalEntities.size();
			ab += wrapper.adventureBlocks.size();
			ae += wrapper.adventureEntities.size();
		}
		if(cb > 0){
			plugin.getLogger().info(plugin.getMessages().getMessage("block-manager-load", "Creative", "Blocks", String.valueOf(cb)));
		}
		if(sb > 0){
			plugin.getLogger().info(plugin.getMessages().getMessage("block-manager-load", "Survival", "Blocks", String.valueOf(sb)));
		}
		if(ab > 0){
			plugin.getLogger().info(plugin.getMessages().getMessage("block-manager-load", "Adventure", "Blocks", String.valueOf(ab)));
		}
		if(ce > 0){
			plugin.getLogger().info(plugin.getMessages().getMessage("block-manager-load", "Creative", "Entities", String.valueOf(ce)));
		}
		if(se > 0){
			plugin.getLogger().info(plugin.getMessages().getMessage("block-manager-load", "Survival", "Entities", String.valueOf(se)));
		}
		if(ae > 0){
			plugin.getLogger().info(plugin.getMessages().getMessage("block-manager-load", "Adventure", "Entities", String.valueOf(ae)));
		}
		return;
	}

	/**
	 * Saves the block manager
	 */
	public void save(){
		doneSave = false;
		Double max = ((Integer) wrappers.size()).doubleValue();
		Double done = 0.0;
		for(String key : wrappers.keySet()){
			ChunkWrapper w = wrappers.get(key);
			w.save(false, true);
			done++;
			this.percent = ((Double) (done / max)).intValue();
		}
		wrappers.clear();
		doneSave = true;
	}

	/**
	 * Loads a chunk into the block manager
	 * 
	 * @param chunk the chunk to load
	 */
	public void loadChunk(Chunk chunk){
		String str = chunkToString(chunk);
		ChunkWrapper wrapper = new ChunkWrapper(chunk, blocksDir, entitiesDir);
		wrapper.load();
		wrappers.put(str, wrapper);
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
			wrapper.save(false, false);
			wrappers.remove(wrapper);
		}
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
			if(Math.floor(l.getX()) == Math.floor(location.getX()) && Math.floor(l.getY()) == Math.floor(location.getY()) && Math.floor(l.getZ()) == Math.floor(location.getZ()) && l.getWorld().getName().equalsIgnoreCase(location.getWorld().getName())){
				return material.gamemode;
			}
		}
		return null;
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
	 * Adds a block to the database
	 * 
	 * @param type the block type
	 * @param block the block
	 */
	public void addBlock(GameMode type, Block block){
		switch (type){
		case CREATIVE:
			if(!plugin.settings().trackedCreative.has(block)){
				return;
			}
			break;
		case SURVIVAL:
			if(!plugin.settings().trackedSurvival.has(block)){
				return;
			}
			break;
		case ADVENTURE:
			if(!plugin.settings().trackedAdventure.has(block)){
				return;
			}
			break;
		default:
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
		Material material = MaterialAPI.getMaterialForEntity(entity);
		if(material == null){
			return;
		}
		switch (type){
		case CREATIVE:
			if(!plugin.settings().trackedCreative.has(material)){
				return;
			}
			break;
		case SURVIVAL:
			if(!plugin.settings().trackedSurvival.has(material)){
				return;
			}
			break;
		case ADVENTURE:
			if(!plugin.settings().trackedAdventure.has(material)){
				return;
			}
			break;
		default:
			break;
		}
		String c = chunkToString(entity.getLocation().getChunk());
		ChunkWrapper wrapper = wrappers.get(c);
		wrapper.addEntity(type, entity);
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
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run(){
				// Setup vars
				int runs = 0;
				int maxRuns = 10;
				long delay = 100;
				boolean updated = false;

				// Loop
				while(runs <= maxRuns && !updated){
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
						e.printStackTrace();
					}
				}

				// Warn if not updated
				if(!updated){
					plugin.getLogger().severe(plugin.getMessages().getMessage("failed-to-update", String.valueOf(delay * maxRuns)));
				}
			}
		});
	}

	// TODO REPLACE
	@Deprecated
	String chunkToString(Chunk chunk){
		return chunk.getX() + "." + chunk.getZ() + "." + chunk.getWorld().getName();
	}

	/**
	 * Gets the percentage of the save done
	 * 
	 * @return the percentage done
	 */
	public int percentSaveDone(){
		if(isSaveDone()){
			return 100;
		}
		return percent;
	}

	/**
	 * Determines if the save has been completed
	 * 
	 * @return true if completed, false otherwise
	 */
	public boolean isSaveDone(){
		return this.doneSave;
	}

	/**
	 * Reloads the block manager
	 */
	public void reload(){
		save();
		load();
	}

}
