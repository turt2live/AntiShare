package com.turt2live.antishare.blocks;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.blocks.BlockManager.ASMaterial;
import com.turt2live.antishare.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.tekkitcompat.ServerHas;
import com.turt2live.antishare.util.generic.ChunkLocation;

class ChunkWrapper {

	private BlockManager manager;
	private AntiShare plugin = AntiShare.getInstance();
	CopyOnWriteArrayList<String> creative_blocks = new CopyOnWriteArrayList<String>();
	CopyOnWriteArrayList<String> survival_blocks = new CopyOnWriteArrayList<String>();
	CopyOnWriteArrayList<String> adventure_blocks = new CopyOnWriteArrayList<String>();
	CopyOnWriteArrayList<String> creative_entities = new CopyOnWriteArrayList<String>();
	CopyOnWriteArrayList<String> survival_entities = new CopyOnWriteArrayList<String>();
	CopyOnWriteArrayList<String> adventure_entities = new CopyOnWriteArrayList<String>();
	private int cx, cz;
	private String world;

	ChunkWrapper(BlockManager manager, Chunk chunk){
		this.manager = manager;
		this.cx = chunk.getX();
		this.cz = chunk.getZ();
		this.world = chunk.getWorld().getName();
	}

	public String getWorldName(){
		return world;
	}

	public ChunkLocation getLocation(){
		return new ChunkLocation(cx, cz);
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
			if(!manager.tracked_creative.isTracked(block)){
				break;
			}
			creative_blocks.add(manager.blockToString(block));
			break;
		case SURVIVAL:
			if(!manager.tracked_survival.isTracked(block)){
				break;
			}
			survival_blocks.add(manager.blockToString(block));
			break;
		default:
			if(ServerHas.adventureMode()){
				if(!manager.tracked_adventure.isTracked(block)){
					break;
				}
				adventure_blocks.add(manager.blockToString(block));
			}
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
		switch (type){
		case CREATIVE:
			if(!manager.tracked_creative.isTracked(entity)){
				break;
			}
			creative_entities.add(manager.entityToString(entity));
			break;
		case SURVIVAL:
			if(!manager.tracked_survival.isTracked(entity)){
				break;
			}
			survival_entities.add(manager.entityToString(entity));
			break;
		default:
			if(ServerHas.adventureMode()){
				if(!manager.tracked_adventure.isTracked(entity)){
					break;
				}
				adventure_entities.add(manager.entityToString(entity));
			}
			break;
		}
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
			if(!manager.tracked_creative.isTracked(entityType)){
				break;
			}
			creative_entities.add(manager.entityToString(entity, entityType));
			break;
		case SURVIVAL:
			if(!manager.tracked_survival.isTracked(entityType)){
				break;
			}
			survival_entities.add(manager.entityToString(entity, entityType));
			break;
		default:
			if(ServerHas.adventureMode()){
				if(!manager.tracked_adventure.isTracked(entityType)){
					break;
				}
				adventure_entities.add(manager.entityToString(entity, entityType));
			}
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
				creative_entities.remove(manager.entityToString(entity));
				break;
			case SURVIVAL:
				survival_entities.remove(manager.entityToString(entity));
				break;
			default:
				if(ServerHas.adventureMode()){
					adventure_entities.remove(manager.entityToString(entity));
				}
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
				creative_blocks.remove(manager.blockToString(block));
				break;
			case SURVIVAL:
				survival_blocks.remove(manager.blockToString(block));
				break;
			default:
				if(ServerHas.adventureMode()){
					adventure_blocks.remove(manager.blockToString(block));
				}
				break;
			}
		}
	}

	public GameMode getType(Entity entity){
		if(creative_entities.contains(manager.entityToString(entity))){
			return GameMode.CREATIVE;
		}else if(survival_entities.contains(manager.entityToString(entity))){
			return GameMode.SURVIVAL;
		}else if(adventure_entities.contains(manager.entityToString(entity))){
			if(ServerHas.adventureMode()){
				return GameMode.ADVENTURE;
			}
		}
		return null;
	}

	public GameMode getType(Block block){
		if(creative_blocks.contains(manager.blockToString(block))){
			return GameMode.CREATIVE;
		}else if(survival_blocks.contains(manager.blockToString(block))){
			return GameMode.SURVIVAL;
		}else if(adventure_blocks.contains(manager.blockToString(block))){
			if(ServerHas.adventureMode()){
				return GameMode.ADVENTURE;
			}
		}
		return null;
	}

	public void save(boolean load, boolean clear, File blocksDir, File entitiesDir){
		File blockFile = new File(blocksDir, cx + "." + cz + "." + world + ".yml");
		File entityFile = new File(entitiesDir, cx + "." + cz + "." + world + ".yml");
		if(blockFile.exists()){
			blockFile.delete();
		}
		if(entityFile.exists()){
			entityFile.delete();
		}
		EnhancedConfiguration blocks = new EnhancedConfiguration(blockFile, plugin);
		EnhancedConfiguration entities = new EnhancedConfiguration(entityFile, plugin);
		blocks.load();
		blocks.clearFile();
		entities.load();
		entities.clearFile();
		for(String s : this.adventure_blocks){
			save(s, GameMode.ADVENTURE, blocks, true);
		}
		for(String s : this.creative_blocks){
			save(s, GameMode.CREATIVE, blocks, true);
		}
		for(String s : this.survival_blocks){
			save(s, GameMode.SURVIVAL, blocks, true);
		}
		for(String s : this.adventure_entities){
			save(s, GameMode.ADVENTURE, entities, false);
		}
		for(String s : this.creative_entities){
			save(s, GameMode.CREATIVE, entities, false);
		}
		for(String s : this.survival_entities){
			save(s, GameMode.SURVIVAL, entities, false);
		}
		blocks.save();
		entities.save();
		if(clear){
			this.adventure_blocks.clear();
			this.adventure_entities.clear();
			this.creative_blocks.clear();
			this.creative_entities.clear();
			this.survival_blocks.clear();
			this.survival_entities.clear();
		}
		if(load){
			load(true, blocksDir);
			load(false, entitiesDir);
		}
	}

	private void save(String s, GameMode gm, EnhancedConfiguration c, boolean isBlock){
		/*
		 * 0 = chunkX
		 * 1 = chunkZ
		 * 2 = world name
		 * 3 = block x
		 * 4 = block y
		 * 5 = block z
		 * 6 = (if provided) entity type as string
		 */
		String[] parts = s.split(";");
		if(parts.length < (isBlock ? 6 : 7) || parts.length > (isBlock ? 6 : 7)){
			plugin.getLogger().warning("INVALID " + (isBlock ? "BLOCK" : "ENTITY") + ": " + s + " (GM=" + gm.name() + "). Report this to Turt2Live.");
		}else{
			String key = parts[3] + ";" + parts[4] + ";" + parts[5] + ";" + parts[2] + (isBlock ? "" : ";" + parts[6]);
			String value = gm.name();
			c.set(key, value);
		}
	}

	public void load(File blocks, File entity){
		load(true, blocks);
		load(false, entity);
	}

	public void load(boolean isBlock, File dir){
		File file = new File(dir, cx + "." + cz + "." + world + ".yml");
		if(!file.exists()){
			return;
		}
		String[] fparts = file.getName().split("\\.");
		if(fparts.length < 3){
			plugin.getLogger().warning("INVALID " + (isBlock ? "BLOCK" : "ENTITY") + " FILE: " + file.getName());
			return;
		}
		String w = fparts[2]; // To see if world == file name world
		if(Bukkit.getWorld(w) == null){
			plugin.getLogger().warning("Failed to load world: " + w);
			return;
		}
		if(!w.equals(world)){
			plugin.getLogger().warning("Worlds do not match: " + world + " | " + w);
			return;
		}
		EnhancedConfiguration blocks = new EnhancedConfiguration(file, plugin);
		blocks.load();
		for(String key : blocks.getKeys(false)){
			String[] keyParts = key.split(";");
			if(keyParts.length < (isBlock ? 3 : 4)){
				plugin.getLogger().warning("INVALID " + (isBlock ? "BLOCK" : "ENTITY") + " FILE: " + file.getName());
				continue;
			}
			Location location = new Location(Bukkit.getWorld(keyParts[3]), Double.parseDouble(keyParts[0]), Double.parseDouble(keyParts[1]), Double.parseDouble(keyParts[2]));
			if(Bukkit.getWorld(keyParts[3]) == null || location == null || location.getWorld() == null){
				continue;
			}
			EntityType etype = null;
			if(keyParts.length > 4){
				try{
					etype = EntityType.fromName(keyParts[4]);
				}catch(Exception e){ // Prevents messy consoles
					etype = null;
				}
			}
			GameMode gm = GameMode.valueOf(blocks.getString(key));
			if(isBlock){
				Block block = location.getBlock();
				if(block == null){
					location.getChunk().load();
					block = location.getBlock();
				}
				addBlock(gm, block);
			}else{
				if(etype == null){
					plugin.getLogger().warning("INVALID " + (isBlock ? "BLOCK" : "ENTITY") + " KEY IN FILE ('" + file.getName() + "'): " + key);
					continue;
				}
				addEntity(gm, location, etype);
			}
		}
	}

}
