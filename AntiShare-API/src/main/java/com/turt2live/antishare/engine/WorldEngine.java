package com.turt2live.antishare.engine;

import com.turt2live.antishare.ABlock;
import com.turt2live.antishare.APlayer;
import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.PermissionNodes;
import com.turt2live.antishare.configuration.groups.ConsolidatedGroup;
import com.turt2live.antishare.configuration.groups.Group;
import com.turt2live.antishare.events.EventDispatcher;
import com.turt2live.antishare.events.worldengine.WorldEngineShutdownEvent;
import com.turt2live.antishare.io.BlockManager;
import com.turt2live.antishare.io.memory.MemoryBlockManager;
import com.turt2live.antishare.utils.ASUtils;

import java.util.List;

/**
 * Represents a world engine. This is used by the core engine to handle
 * multi-world actions and functions.
 *
 * @author turt2live
 */
public final class WorldEngine {

    private String worldName;
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
        EventDispatcher.dispatch(new WorldEngineShutdownEvent(this));
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
     * Gets the block manager for this engine
     *
     * @return the block manager
     */
    public BlockManager getBlockManager() {
        return blockManager;
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
     * Gets the tracking list for a specified gamemode
     *
     * @param gamemode the gamemode, cannot be null
     * @return the list
     */
    public BlockTypeList getTrackedBlocks(ASGameMode gamemode) {
        List<Group> allGroups = Engine.getInstance().getGroupManager().getGroupsForWorld(getWorldName(), false);
        if (allGroups == null || allGroups.size() <= 0) return new DefaultBlockTypeList();
        ConsolidatedGroup consolidatedGroup = new ConsolidatedGroup(allGroups);
        return consolidatedGroup.getTrackedList(gamemode);
    }

    /**
     * Processes a block placement, ensuring the player is allowed to place the block as well
     * as running the tracking logic.
     *
     * @param player  the player placing the block, cannot be null
     * @param block   the block being placed, cannot be null
     * @param placeAs the gamemode placing the block
     * @return returns true if the block placement was rejected, false otherwise
     */
    public boolean processBlockPlace(APlayer player, ABlock block, ASGameMode placeAs) {
        if (player == null || block == null || placeAs == null) throw new IllegalArgumentException();

        List<Group> groups = Engine.getInstance().getGroupManager().getGroupsForPlayer(player, false);
        BlockTypeList list = new DefaultBlockTypeList();
        if (groups != null && groups.size() > 0) {
            ConsolidatedGroup consolidatedGroup = new ConsolidatedGroup(groups);
            list = consolidatedGroup.getTrackedList(placeAs);
        }

        // TODO: Placement permissions, logic, etc

        if (list.isTracked(block.getLocation()) && !player.hasPermission(PermissionNodes.FREE_PLACE)) {
            blockManager.setBlockType(block.getLocation(), ASUtils.toBlockType(placeAs));
        }

        return false;
    }
}
