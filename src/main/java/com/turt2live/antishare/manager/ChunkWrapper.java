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
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.blocks.io.ASRegion;
import com.turt2live.antishare.blocks.io.ASRegion.EntryInfo;
import com.turt2live.antishare.blocks.io.LegacyBlockIO;
import com.turt2live.antishare.manager.BlockManager.ASMaterial;
import com.turt2live.materials.MaterialAPI;

/**
 * Used to load/save data per-chunk
 * 
 * @author turt2live
 */
//TODO: Schedule for rewrite
public class ChunkWrapper{

	private final BlockManager manager;
	private final AntiShare plugin = AntiShare.p;
	CopyOnWriteArrayList<String> creativeBlocks = new CopyOnWriteArrayList<String>();
	CopyOnWriteArrayList<String> survivalBlocks = new CopyOnWriteArrayList<String>();
	CopyOnWriteArrayList<String> adventureBlocks = new CopyOnWriteArrayList<String>();
	CopyOnWriteArrayList<String> creativeEntities = new CopyOnWriteArrayList<String>();
	CopyOnWriteArrayList<String> survivalEntities = new CopyOnWriteArrayList<String>();
	CopyOnWriteArrayList<String> adventureEntities = new CopyOnWriteArrayList<String>();
	private final int chunkX, chunkZ;
	private final String world;
	private final File blocksDir, entitiesDir;

	ChunkWrapper(BlockManager manager, Chunk chunk, File blocksDir, File entitiesDir){
		this.manager = manager;
		this.chunkX = chunk.getX();
		this.chunkZ = chunk.getZ();
		this.world = chunk.getWorld().getName();
		this.blocksDir = blocksDir;
		this.entitiesDir = entitiesDir;

		// Check for YAML
		File blockFile = new File(blocksDir, chunkX + "." + chunkZ + "." + world + ".yml");
		File entityFile = new File(entitiesDir, chunkX + "." + chunkZ + "." + world + ".yml");

		if(blockFile.exists()){
			LegacyBlockIO.load(true, blockFile, this);
			blockFile.delete();
		}
		if(entityFile.exists()){
			LegacyBlockIO.load(false, entityFile, this);
			entityFile.delete();
		}
	}

	/**
	 * Gets the world name for this chunk
	 * 
	 * @return the world name
	 */
	public String getWorldName(){
		return world;
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
				break;
			}
			creativeBlocks.add(manager.blockToString(block));
			break;
		case SURVIVAL:
			if(!plugin.settings().trackedSurvival.has(block)){
				break;
			}
			survivalBlocks.add(manager.blockToString(block));
			break;
		case ADVENTURE:
			if(!plugin.settings().trackedAdventure.has(block)){
				break;
			}
			adventureBlocks.add(manager.blockToString(block));
			break;
		default:
			break;
		}
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
				break;
			}
			creativeEntities.add(manager.entityToString(entity));
			break;
		case SURVIVAL:
			if(!plugin.settings().trackedSurvival.has(material)){
				break;
			}
			survivalEntities.add(manager.entityToString(entity));
			break;
		case ADVENTURE:
			if(!plugin.settings().trackedAdventure.has(material)){
				break;
			}
			adventureEntities.add(manager.entityToString(entity));
			break;
		default:
			break;
		}
	}

	/**
	 * Adds an entity to the database
	 * 
	 * @param type the entity type
	 * @param location the entity location
	 * @param entity the entity
	 */
	public void addEntity(GameMode type, Location location, EntityType entity){
		Material material = MaterialAPI.getMaterialForEntity(entity);
		if(material == null){
			return;
		}
		switch (type){
		case CREATIVE:
			if(!plugin.settings().trackedCreative.has(material)){
				break;
			}
			creativeEntities.add(manager.entityToString(location, entity));
			break;
		case SURVIVAL:
			if(!plugin.settings().trackedSurvival.has(material)){
				break;
			}
			survivalEntities.add(manager.entityToString(location, entity));
			break;
		case ADVENTURE:
			if(!plugin.settings().trackedAdventure.has(material)){
				break;
			}
			adventureEntities.add(manager.entityToString(location, entity));
			break;
		default:
			break;
		}
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
			switch (type){
			case CREATIVE:
				creativeEntities.remove(manager.entityToString(entity));
				break;
			case SURVIVAL:
				survivalEntities.remove(manager.entityToString(entity));
				break;
			case ADVENTURE:
				adventureEntities.remove(manager.entityToString(entity));
				break;
			default:
				break;
			}
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
			switch (type){
			case CREATIVE:
				creativeBlocks.remove(manager.blockToString(block));
				break;
			case SURVIVAL:
				survivalBlocks.remove(manager.blockToString(block));
				break;
			case ADVENTURE:
				adventureBlocks.remove(manager.blockToString(block));
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Gets the game mode of an entity
	 * 
	 * @param entity the entity
	 * @return the gamemode, or null if not found, of the entity
	 */
	public GameMode getType(Entity entity){
		if(creativeEntities.contains(manager.entityToString(entity))){
			return GameMode.CREATIVE;
		}else if(survivalEntities.contains(manager.entityToString(entity))){
			return GameMode.SURVIVAL;
		}else if(adventureEntities.contains(manager.entityToString(entity))){
			return GameMode.ADVENTURE;
		}
		return null;
	}

	/**
	 * Gets the game mode of a block
	 * 
	 * @param block the entity
	 * @return the gamemode, or null if not found, of the block
	 */
	public GameMode getType(Block block){
		if(creativeBlocks.contains(manager.blockToString(block))){
			return GameMode.CREATIVE;
		}else if(survivalBlocks.contains(manager.blockToString(block))){
			return GameMode.SURVIVAL;
		}else if(adventureBlocks.contains(manager.blockToString(block))){
			return GameMode.ADVENTURE;
		}
		return null;
	}

	/**
	 * Saves the chunk information
	 * 
	 * @param load set to true to load data after saving
	 * @param clear set to true to clear self after saving
	 * @param blocksDir the blocks data directory
	 * @param entitiesDir the entities data directory
	 */
	public void save(boolean load, boolean clear){
		File blockFile = new File(blocksDir, chunkX + "." + chunkZ + "." + world + ".asr");
		File entityFile = new File(entitiesDir, chunkX + "." + chunkZ + "." + world + ".asr");
		// Used for sane file creation
		boolean noBlockFile = false, noEntityFile = false;
		if(this.adventureBlocks.size() <= 0 && this.survivalBlocks.size() <= 0 && this.creativeBlocks.size() <= 0){
			if(blockFile.exists()){
				blockFile.delete();
			}
			noBlockFile = true;
		}
		if(this.adventureEntities.size() <= 0 && this.survivalEntities.size() <= 0 && this.creativeEntities.size() <= 0){
			if(entityFile.exists()){
				entityFile.delete();
			}
			noEntityFile = true;
		}
		World world = plugin.getServer().getWorld(this.world);
		if(!noBlockFile){
			ASRegion region = new ASRegion(false);
			try{
				region.prepare(blockFile, true);
				for(String string : creativeBlocks){
					Location location = LegacyBlockIO.locationFromString(world, string);
					region.write(location, GameMode.CREATIVE);
				}
				for(String string : survivalBlocks){
					Location location = LegacyBlockIO.locationFromString(world, string);
					region.write(location, GameMode.SURVIVAL);
				}
				for(String string : adventureBlocks){
					Location location = LegacyBlockIO.locationFromString(world, string);
					region.write(location, GameMode.ADVENTURE);
				}
				region.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		if(!noEntityFile){
			ASRegion region = new ASRegion(true);
			try{
				region.prepare(entityFile, true);
				for(String s : this.adventureEntities){
					Location location = LegacyBlockIO.locationFromString(world, s);
					EntityType type = LegacyBlockIO.entityFromString(s);
					region.write(location, GameMode.ADVENTURE, type);
				}
				for(String s : this.creativeEntities){
					Location location = LegacyBlockIO.locationFromString(world, s);
					EntityType type = LegacyBlockIO.entityFromString(s);
					region.write(location, GameMode.CREATIVE, type);
				}
				for(String s : this.survivalEntities){
					Location location = LegacyBlockIO.locationFromString(world, s);
					EntityType type = LegacyBlockIO.entityFromString(s);
					region.write(location, GameMode.SURVIVAL, type);
				}
			}catch(IOException e){
				e.printStackTrace();
			}
			if(clear){
				this.adventureBlocks.clear();
				this.adventureEntities.clear();
				this.creativeBlocks.clear();
				this.creativeEntities.clear();
				this.survivalBlocks.clear();
				this.survivalEntities.clear();
			}
		}
		if(load){
			load(true);
			load(false);
		}
	}

	/**
	 * Loads data for this chunk
	 * 
	 * @param blocks the blocks data directory
	 * @param entity the entities data directory
	 */
	public void load(){
		load(true);
		load(false);
	}

	/**
	 * Loads a specific directory
	 * 
	 * @param isBlock set to true if loading block information
	 * @param dir the directory to load
	 */
	public void load(boolean isBlock){
		File file = new File(isBlock ? blocksDir : entitiesDir, chunkX + "." + chunkZ + "." + world + ".asr");
		if(!file.exists()){
			return;
		}
		String[] fileParts = file.getName().split("\\.");
		if(fileParts.length < 3){
			plugin.getLogger().severe(plugin.getMessages().getMessage("bad-file", file.getAbsolutePath()));
			return;
		}
		String w = fileParts[2]; // To see if world == file name world
		World bWorld = Bukkit.getWorld(w);
		if(bWorld == null){
			plugin.getLogger().warning(plugin.getMessages().getMessage("unknown-world", w));
			return;
		}
		if(!w.equals(world)){
			plugin.getLogger().warning(plugin.getMessages().getMessage("unknown-world", w));
			return;
		}
		ASRegion region = new ASRegion(!isBlock);
		try{
			region.prepare(file, false);
			if(isBlock){
				EntryInfo info = null;
				while((info = region.getNext(bWorld)) != null){
					Block block = info.location.getBlock();
					if(block == null){
						info.location.getChunk().load();
						block = info.location.getBlock();
					}
					addBlock(info.gamemode, block);
				}
			}else{
				EntryInfo info = null;
				while((info = region.getNext(bWorld)) != null){
					addEntity(info.gamemode, info.location, info.entity);
				}
			}
			region.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
