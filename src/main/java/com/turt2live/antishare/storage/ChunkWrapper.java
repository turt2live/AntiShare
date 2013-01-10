package com.turt2live.antishare.storage;

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
import com.turt2live.antishare.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.storage.BlockManager.ASMaterial;
import com.turt2live.antishare.tekkitcompat.ServerHas;
import com.turt2live.antishare.util.generic.ChunkLocation;

class ChunkWrapper {

	private BlockManager manager;
	private AntiShare plugin = AntiShare.getInstance();
	private CopyOnWriteArrayList<String> creative_blocks = new CopyOnWriteArrayList<String>();
	private CopyOnWriteArrayList<String> survival_blocks = new CopyOnWriteArrayList<String>();
	private CopyOnWriteArrayList<String> adventure_blocks = new CopyOnWriteArrayList<String>();
	private CopyOnWriteArrayList<String> creative_entities = new CopyOnWriteArrayList<String>();
	private CopyOnWriteArrayList<String> survival_entities = new CopyOnWriteArrayList<String>();
	private CopyOnWriteArrayList<String> adventure_entities = new CopyOnWriteArrayList<String>();
	private int cx, cz;
	private String world;
	private ObjectSaver saveCreativeBlocks, saveSurvivalBlocks, saveAdventureBlocks, saveCreativeEntities, saveSurvivalEntities, saveAdventureEntities;

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
		return null;
	}

	public GameMode getType(Block block){
		return null;
	}

	public void save(String[] names, boolean load, boolean clear, File blocksDir, File entitiesDir){
		if(names.length < 6){
			throw new IllegalArgumentException("6 names are required");
		}
		saveCreativeBlocks = new ObjectSaver(creative_blocks, GameMode.CREATIVE, blocksDir, names[0], true);
		saveSurvivalBlocks = new ObjectSaver(survival_blocks, GameMode.SURVIVAL, blocksDir, names[1], true);
		saveCreativeEntities = new ObjectSaver(creative_entities, GameMode.CREATIVE, entitiesDir, names[2], false);
		saveSurvivalEntities = new ObjectSaver(survival_entities, GameMode.SURVIVAL, entitiesDir, names[3], false);

		saveCreativeBlocks.setClear(clear);
		saveSurvivalBlocks.setClear(clear);
		saveCreativeBlocks.setLoad(load);
		saveSurvivalBlocks.setLoad(load);
		saveCreativeEntities.setClear(clear);
		saveSurvivalEntities.setClear(clear);
		saveCreativeEntities.setLoad(load);
		saveSurvivalEntities.setLoad(load);

		// Treat adventure on it's own
		if(ServerHas.adventureMode()){
			saveAdventureBlocks = new ObjectSaver(adventure_blocks, GameMode.ADVENTURE, blocksDir, names[4], true);
			saveAdventureBlocks.setClear(clear);
			saveAdventureBlocks.setLoad(load);
			saveAdventureEntities = new ObjectSaver(adventure_entities, GameMode.ADVENTURE, entitiesDir, names[5], false);
			saveAdventureEntities.setClear(clear);
			saveAdventureEntities.setLoad(load);
		}else{
			saveAdventureBlocks = null;
			saveAdventureEntities = null;
			NullObjectSaver nullObject = new NullObjectSaver();
			nullObject.setClear(clear);
			nullObject.setLoad(load);
			manager.markSaveAsDone(names[4], nullObject);
			manager.markSaveAsDone(names[5], nullObject);
		}

		// Schedule saves

		/*
		 * Because of how the scheduler works, we have to use the java Thread class.
		 */

		Thread creativeBlocksThread = new Thread(saveCreativeBlocks);
		Thread survivalBlocksThread = new Thread(saveSurvivalBlocks);
		Thread creativeEntitiesThread = new Thread(saveCreativeEntities);
		Thread survivalEntitiesThread = new Thread(saveSurvivalEntities);

		// Set names, in case there is a bug
		creativeBlocksThread.setName("ANTISHARE-Save Creative Blocks");
		survivalBlocksThread.setName("ANTISHARE-Save Survival Blocks");
		creativeEntitiesThread.setName("ANTISHARE-Save Creative Entities");
		survivalEntitiesThread.setName("ANTISHARE-Save Survival Entities");

		// Run
		creativeBlocksThread.start();
		survivalBlocksThread.start();
		creativeEntitiesThread.start();
		survivalEntitiesThread.start();

		// Treat adventure on it's own
		ObjectSaver nullSaver = new NullObjectSaver();
		nullSaver.setClear(clear);
		nullSaver.setLoad(load);
		if(saveAdventureBlocks != null){
			Thread adventureBlocksThread = new Thread(saveAdventureBlocks);
			adventureBlocksThread.setName("ANTISHARE-Save Adventure Blocks");
			adventureBlocksThread.start();
		}
		if(saveAdventureEntities != null){
			Thread adventureEntitiesThread = new Thread(saveAdventureEntities);
			adventureEntitiesThread.setName("ANTISHARE-Save Adventure Entities");
			adventureEntitiesThread.start();
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

	/**
	 * Gets the percentage done save
	 * 
	 * @return the percent done
	 */
	public double percentDoneSave(){
		double percentCreative = saveCreativeBlocks.getPercent() + saveCreativeEntities.getPercent();
		double percentSurvival = saveSurvivalBlocks.getPercent() + saveSurvivalEntities.getPercent();
		double percentAdventure = (saveAdventureBlocks != null ? saveAdventureBlocks.getPercent() : 0)
				+ (saveAdventureEntities != null ? saveAdventureEntities.getPercent() : 0);
		double divisible = 6 - (saveAdventureBlocks == null ? 1 : 0) - (saveAdventureEntities == null ? 1 : 0);
		Double avg = (percentCreative + percentAdventure + percentSurvival) / divisible;
		return avg.intValue();
	}

}
