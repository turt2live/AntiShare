package com.turt2live.antishare.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.turt2live.antishare.blocks.BlockManager;

public class BlockManagerListener implements Listener {

	private BlockManager blocks;

	public BlockManagerListener(BlockManager manager){
		blocks = manager;
	}

	// ################# Chunk Load

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event){
		blocks.loadChunk(event.getChunk());
	}

	// ################# Chunk Unload

	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event){
		blocks.unloadChunk(event.getChunk());
	}

	// ################# GameMode Block Break

	@EventHandler (priority = EventPriority.MONITOR)
	public void onGameModeBlockBreak(BlockBreakEvent event){
		if(!event.isCancelled()){
			blocks.removeBlock(event.getBlock());
		}
	}
}
