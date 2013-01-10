package com.turt2live.antishare.storage;

import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import com.turt2live.antishare.util.generic.ChunkLocation;

class ChunkWrapper {

	//	private BlockManager manager;
	//	private AntiShare plugin = AntiShare.getInstance();
	//	private CopyOnWriteArrayList<String> creative_blocks = new CopyOnWriteArrayList<String>();
	//	private CopyOnWriteArrayList<String> survival_blocks = new CopyOnWriteArrayList<String>();
	//	private CopyOnWriteArrayList<String> adventure_blocks = new CopyOnWriteArrayList<String>();
	//	private CopyOnWriteArrayList<String> creative_entities = new CopyOnWriteArrayList<String>();
	//	private CopyOnWriteArrayList<String> survival_entities = new CopyOnWriteArrayList<String>();
	//	private CopyOnWriteArrayList<String> adventure_entities = new CopyOnWriteArrayList<String>();
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

	public void remove(Entity entity){

	}

	public void remove(Block block){

	}

	public void add(Entity entity, GameMode gamemode){

	}

	public void add(Block block, GameMode gamemode){

	}

	public GameMode getType(Entity entity){
		return null;
	}

	public GameMode getType(Block block){
		return null;
	}

	public void save(){

	}

}
