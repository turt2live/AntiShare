package com.turt2live.antishare.manager;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.lang.LocaleMessage;
import com.turt2live.antishare.lang.Localization;
import com.turt2live.antishare.manager.BlockManager.ASMaterial;
import com.turt2live.antishare.util.WrappedEnhancedConfiguration;

class ChunkWrapper {

	private final BlockManager manager;
	private final AntiShare plugin = AntiShare.getInstance();
	CopyOnWriteArrayList<String> creativeBlocks = new CopyOnWriteArrayList<String>();
	CopyOnWriteArrayList<String> survivalBlocks = new CopyOnWriteArrayList<String>();
	CopyOnWriteArrayList<String> adventureBlocks = new CopyOnWriteArrayList<String>();
	CopyOnWriteArrayList<String> creativeEntities = new CopyOnWriteArrayList<String>();
	CopyOnWriteArrayList<String> survivalEntities = new CopyOnWriteArrayList<String>();
	CopyOnWriteArrayList<String> adventureEntities = new CopyOnWriteArrayList<String>();
	private final int chunkX, chunkZ;
	private final String world;

	ChunkWrapper(BlockManager manager, Chunk chunk){
		this.manager = manager;
		this.chunkX = chunk.getX();
		this.chunkZ = chunk.getZ();
		this.world = chunk.getWorld().getName();
	}

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
			if(!manager.trackedCreative.isTracked(block)){
				break;
			}
			creativeBlocks.add(manager.blockToString(block));
			break;
		case SURVIVAL:
			if(!manager.trackedSurvival.isTracked(block)){
				break;
			}
			survivalBlocks.add(manager.blockToString(block));
			break;
		case ADVENTURE:
			if(!manager.trackedAdventure.isTracked(block)){
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
		switch (type){
		case CREATIVE:
			if(!manager.trackedCreative.isTracked(entity)){
				break;
			}
			creativeEntities.add(manager.entityToString(entity));
			break;
		case SURVIVAL:
			if(!manager.trackedSurvival.isTracked(entity)){
				break;
			}
			survivalEntities.add(manager.entityToString(entity));
			break;
		case ADVENTURE:
			if(!manager.trackedAdventure.isTracked(entity)){
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
	 * @param type the entity gamemode
	 * @param entityType the entity type
	 * @param entity the entity
	 */
	public void addEntity(GameMode type, Location entity, EntityType entityType){
		switch (type){
		case CREATIVE:
			if(!manager.trackedCreative.isTracked(entityType)){
				break;
			}
			creativeEntities.add(manager.entityToString(entity, entityType));
			break;
		case SURVIVAL:
			if(!manager.trackedSurvival.isTracked(entityType)){
				break;
			}
			survivalEntities.add(manager.entityToString(entity, entityType));
			break;
		case ADVENTURE:
			if(!manager.trackedAdventure.isTracked(entityType)){
				break;
			}
			adventureEntities.add(manager.entityToString(entity, entityType));
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

	public void save(boolean load, boolean clear, File blocksDir, File entitiesDir){
		File blockFile = new File(blocksDir, chunkX + "." + chunkZ + "." + world + ".yml");
		File entityFile = new File(entitiesDir, chunkX + "." + chunkZ + "." + world + ".yml");
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
		if(!noBlockFile){
			WrappedEnhancedConfiguration blocks = new WrappedEnhancedConfiguration(blockFile, plugin);
			blocks.load();
			blocks.clearFile();
			for(String s : this.adventureBlocks){
				save(s, GameMode.ADVENTURE, blocks, true);
			}
			for(String s : this.creativeBlocks){
				save(s, GameMode.CREATIVE, blocks, true);
			}
			for(String s : this.survivalBlocks){
				save(s, GameMode.SURVIVAL, blocks, true);
			}
			blocks.save();
		}
		if(!noEntityFile){
			WrappedEnhancedConfiguration entities = new WrappedEnhancedConfiguration(entityFile, plugin);
			entities.load();
			entities.clearFile();
			for(String s : this.adventureEntities){
				save(s, GameMode.ADVENTURE, entities, false);
			}
			for(String s : this.creativeEntities){
				save(s, GameMode.CREATIVE, entities, false);
			}
			for(String s : this.survivalEntities){
				save(s, GameMode.SURVIVAL, entities, false);
			}
			entities.save();
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
			load(true, blocksDir);
			load(false, entitiesDir);
		}
	}

	private void save(String rawString, GameMode gamemode, EnhancedConfiguration configuration, boolean isBlock){
		/*
		 * 0 = chunkX
		 * 1 = chunkZ
		 * 2 = world name
		 * 3 = block x
		 * 4 = block y
		 * 5 = block z
		 * 6 = (if provided) entity type as string
		 */
		String[] parts = rawString.split(";");
		if(parts.length < (isBlock ? 6 : 7) || parts.length > (isBlock ? 6 : 7)){
			plugin.getLogger().warning(Localization.getMessage(LocaleMessage.ERROR_BAD_KEY, rawString, "gm=" + gamemode.name() + ":isB=" + isBlock));
		}else{
			String key = parts[3] + ";" + parts[4] + ";" + parts[5] + ";" + parts[2] + (isBlock ? "" : ";" + parts[6]);
			String value = gamemode.name();
			configuration.set(key, value);
		}
	}

	public void load(File blocks, File entity){
		load(true, blocks);
		load(false, entity);
	}

	public void load(boolean isBlock, File dir){
		File file = new File(dir, chunkX + "." + chunkZ + "." + world + ".yml");
		if(!file.exists()){
			return;
		}
		String[] fileParts = file.getName().split("\\.");
		if(fileParts.length < 3){
			plugin.getLogger().warning(Localization.getMessage(LocaleMessage.ERROR_BAD_FILE, file.getAbsolutePath()));
			return;
		}
		String w = fileParts[2]; // To see if world == file name world
		if(Bukkit.getWorld(w) == null){
			plugin.getLogger().warning(Localization.getMessage(LocaleMessage.ERROR_UNKNOWN, Localization.getMessage(LocaleMessage.DICT_WORLD), w));
			return;
		}
		if(!w.equals(world)){
			plugin.getLogger().warning(Localization.getMessage(LocaleMessage.ERROR_UNKNOWN, Localization.getMessage(LocaleMessage.DICT_WORLD), w));
			return;
		}
		EnhancedConfiguration blocks = new EnhancedConfiguration(file, plugin);
		blocks.load();
		for(String key : blocks.getKeys(false)){
			String[] keyParts = key.split(";");
			if(keyParts.length < (isBlock ? 3 : 4)){
				plugin.getLogger().warning(Localization.getMessage(LocaleMessage.ERROR_BAD_FILE, file.getAbsolutePath()));
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
				addBlock(gamemode, block);
			}else{
				if(entityType == null){
					plugin.getLogger().warning(Localization.getMessage(LocaleMessage.ERROR_BAD_KEY, key, file.getAbsolutePath()));
					continue;
				}
				addEntity(gamemode, location, entityType);
			}
		}
	}

}
