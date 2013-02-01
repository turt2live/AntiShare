package com.turt2live.antishare.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
import com.turt2live.antishare.listener.BlockListener;
import com.turt2live.antishare.util.events.TrackerList;

public class BlockManager extends AntiShareManager {

	static class ASMaterial {
		public Location location;
		public GameMode gamemode;
		public long added;
	}

	private final File entitiesDir;
	private final File blocksDir;
	TrackerList tracked_creative;
	TrackerList tracked_survival;
	TrackerList tracked_adventure;
	private CopyOnWriteArrayList<ASMaterial> recentlyRemoved = new CopyOnWriteArrayList<ASMaterial>();
	private ConcurrentMap<String, ChunkWrapper> wrappers = new ConcurrentHashMap<String, ChunkWrapper>();
	private boolean doneSave = false;
	private int percent = 0;

	public BlockManager(){
		// Setup files
		entitiesDir = new File(plugin.getDataFolder(), "data" + File.separator + "entities");
		blocksDir = new File(plugin.getDataFolder(), "data" + File.separator + "blocks");

		// Start cleanup
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
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

		// Start listener
		plugin.getServer().getPluginManager().registerEvents(new BlockListener(this), plugin);
	}

	@Override
	public boolean load(){
		// Setup lists
		tracked_creative = new TrackerList("config.yml", "block-tracking.tracked-creative-blocks", plugin.getConfig().getString("block-tracking.tracked-creative-blocks").split(","));
		tracked_survival = new TrackerList("config.yml", "block-tracking.tracked-survival-blocks", plugin.getConfig().getString("block-tracking.tracked-survival-blocks").split(","));
		tracked_adventure = new TrackerList("config.yml", "block-tracking.tracked-adventure-blocks", plugin.getConfig().getString("block-tracking.tracked-adventure-blocks").split(","));

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
		return true;
	}

	@Override
	public boolean save(){
		doneSave = false;
		Double max = ((Integer) wrappers.size()).doubleValue();
		Double done = 0.0;
		for(String key : wrappers.keySet()){
			ChunkWrapper w = wrappers.get(key);
			w.save(false, true, blocksDir, entitiesDir);
			done++;
			this.percent = ((Double) (done / max)).intValue();
		}
		wrappers.clear();
		doneSave = true;
		return true;
	}

	/**
	 * Loads a chunk into the block manager
	 * 
	 * @param chunk the chunk to load
	 */
	public void loadChunk(Chunk chunk){
		String str = chunkToString(chunk);
		ChunkWrapper wrapper = new ChunkWrapper(this, chunk);
		wrapper.load(blocksDir, entitiesDir);
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
			wrapper.save(false, false, blocksDir, entitiesDir);
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
			if(Math.floor(l.getX()) == Math.floor(location.getX())
					&& Math.floor(l.getY()) == Math.floor(location.getY())
					&& Math.floor(l.getZ()) == Math.floor(location.getZ())
					&& l.getWorld().getName().equalsIgnoreCase(location.getWorld().getName())){
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
			if(!tracked_creative.isTracked(block)){
				return;
			}
			break;
		case SURVIVAL:
			if(!tracked_survival.isTracked(block)){
				return;
			}
			break;
		case ADVENTURE:
			if(!tracked_adventure.isTracked(block)){
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
		case ADVENTURE:
			if(!tracked_adventure.isTracked(entity)){
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
		case ADVENTURE:
			if(!tracked_adventure.isTracked(entityType)){
				return;
			}
			break;
		default:
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
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable(){
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

	String chunkToString(Chunk chunk){
		return chunk.getX() + "." + chunk.getZ() + "." + chunk.getWorld().getName();
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

	public int percentSaveDone(){
		if(isSaveDone()){
			return 100;
		}
		return percent;
	}

	public boolean isSaveDone(){
		return this.doneSave;
	}

}
