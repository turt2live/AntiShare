package com.turt2live.antishare.engine;

import com.turt2live.antishare.engine.defaults.DefaultBlockTypeList;
import com.turt2live.antishare.io.BlockManager;
import com.turt2live.antishare.io.memory.MemoryBlockManager;
import com.turt2live.antishare.utils.ASGameMode;
import com.turt2live.antishare.utils.ASLocation;
import com.turt2live.antishare.utils.ASUtils;
import com.turt2live.antishare.utils.BlockType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Represents a world engine. This is used by the core engine to handle
 * multi-world actions and functions.
 *
 * @author turt2live
 */
// TODO: Unit test
public final class WorldEngine {

    private String worldName;
    private ConcurrentMap<ASGameMode, BlockTypeList> trackedBlocks = new ConcurrentHashMap<ASGameMode, BlockTypeList>();
    private BlockManager blockManager = new MemoryBlockManager();

    /**
     * Creates a new world engine
     *
     * @param worldName the world name, cannot be null
     */
    public WorldEngine(String worldName) {
        if (worldName == null) throw new IllegalArgumentException("world cannot be null");

        this.worldName = worldName;
    }

    /**
     * Prepares this world engine for shutdown
     */
    public void prepareShutdown() {
        blockManager.saveAll();
    }

    /**
     * Gets the world name for this engine
     *
     * @return the world name
     */
    public String getWorldName() {
        return worldName;
    }

    /**
     * Sets the block manager to use. This will overwrite the previous one and NOT perform
     * any save routines on the previous manager, nor will this perform any load operations
     * on the new manager.
     *
     * @param blockManager the new manager, cannot be null
     */
    public void setBlockManager(BlockManager blockManager) {
        if (blockManager == null) throw new IllegalArgumentException("block manager cannot be null");

        this.blockManager = blockManager;
    }

    /**
     * Gets the block manager for this engine
     *
     * @return the block manager
     */
    public BlockManager getBlockManager() {
        return blockManager;
    }

    /**
     * Sets the tracking list for a specified gamemode
     *
     * @param gamemode the gamemode, cannot be null
     * @param list     the list, cannot be null
     */
    public void setTrackedBlocks(ASGameMode gamemode, BlockTypeList list) {
        if (list == null || gamemode == null) throw new IllegalArgumentException();

        trackedBlocks.put(gamemode, list);
    }

    /**
     * Gets the tracking list for a specified gamemode
     *
     * @param gamemode the gamemode, cannot be null
     * @return the list
     */
    public BlockTypeList getTrackedBlocks(ASGameMode gamemode) {
        if (!trackedBlocks.containsKey(gamemode)) setTrackedBlocks(gamemode, new DefaultBlockTypeList());
        return trackedBlocks.get(gamemode);
    }

    /**
     * Processes a block place action. This method assumes the blocking/restricting
     * portion of the code has been completed.
     *
     * @param location the location of the block, cannot be null
     * @param gamemode the gamemode of the block, cannot be null
     */
    public void processBlockPlace(ASLocation location, BlockType gamemode) {
        if (location == null || gamemode == null) throw new IllegalArgumentException();

        // TODO: Expand to actually do all the processing (permissions, etc)

        if (getTrackedBlocks(ASUtils.toGamemode(gamemode)).isTracked(location)) {
            blockManager.setBlockType(location, gamemode);
        }
    }
}
