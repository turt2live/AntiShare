package com.turt2live.antishare.storage;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.GameMode;
import org.bukkit.block.Block;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.storage.BlockManager.ListComplete;

class BlockSaver implements Runnable {

	private final CopyOnWriteArrayList<Block> list;
	private final AntiShare plugin = AntiShare.getInstance();
	private final BlockManager blockman = plugin.getBlockManager();
	private final File dir;
	private final String gamemode;
	private final ListComplete listType;
	private boolean clear = false, load = false;
	private double completed = 0;

	public BlockSaver(CopyOnWriteArrayList<Block> list, GameMode gm, File saveDir, ListComplete type){
		this.list = list;
		this.dir = saveDir;
		this.gamemode = gm.name();
		this.listType = type;
	}

	@Override
	public void run(){
		for(Block block : list){
			saveBlock(dir, block, gamemode);
			completed++;
		}
		blockman.markSaveAsDone(listType, this);
	}

	void saveBlock(File dir, Block block, String gamemode){
		String fname = block.getChunk().getX() + "." + block.getChunk().getZ() + "." + block.getWorld().getName() + ".yml";
		EnhancedConfiguration blocks = blockman.getFile(dir, fname);
		blocks.set(block.getX() + ";" + block.getY() + ";" + block.getZ() + ";" + block.getWorld().getName(), gamemode);
	}

	boolean getClear(){
		return clear;
	}

	boolean getLoad(){
		return load;
	}

	void setClear(boolean clear){
		this.clear = clear;
	}

	void setLoad(boolean load){
		this.load = load;
	}

	double getPercent(){
		return (completed / list.size()) * 100;
	}

}
