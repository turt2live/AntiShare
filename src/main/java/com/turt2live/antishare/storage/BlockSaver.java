package com.turt2live.antishare.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.GameMode;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.storage.BlockManager.ListComplete;

class BlockSaver implements Runnable {

	private final Map<String, List<String>> list = new HashMap<String, List<String>>();
	private final AntiShare plugin = AntiShare.getInstance();
	private final BlockManager blockman = plugin.getBlockManager();
	private final File dir;
	private final String gamemode;
	private final ListComplete listType;
	private boolean clear = false, load = false;
	private double completed = 0;
	private final int listSize;

	public BlockSaver(CopyOnWriteArrayList<String> list, GameMode gm, File saveDir, ListComplete type){
		this.dir = saveDir;
		this.gamemode = gm.name();
		this.listType = type;
		for(String block : list){
			/*
			 * 0 = chunkX
			 * 1 = chunkZ
			 * 2 = world name
			 * 3 = block x
			 * 4 = block y
			 * 5 = block z
			 */
			String[] parts = block.split(";");
			if(parts.length < 6 || parts.length > 6){
				plugin.getLogger().warning("INVALID BLOCK: " + block + " (GM=" + gm.name() + "). Report this to Turt2Live.");
				continue;
			}
			String fname = parts[0] + "." + parts[1] + "." + parts[2] + ".yml";
			List<String> blocks = new ArrayList<String>();
			if(this.list.containsKey(fname)){
				blocks = this.list.get(fname);
			}
			blocks.add(parts[4] + ";" + parts[4] + ";" + parts[5] + ";" + parts[2]);
			this.list.put(fname, blocks);
		}
		this.listSize = this.list.keySet().size();
	}

	@Override
	public void run(){
		for(String chunk : list.keySet()){
			List<String> blocks = list.get(chunk);
			for(String block : blocks){
				saveBlock(dir, chunk, block);
				completed++;
			}
		}
		blockman.markSaveAsDone(listType, this);
	}

	void saveBlock(File dir, String fname, String key){
		EnhancedConfiguration blocks = blockman.getFile(dir, fname);
		blocks.set(key, gamemode);
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
		return (completed / listSize) * 100;
	}

}
