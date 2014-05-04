package com.turt2live.antishare.engine;

import com.turt2live.antishare.*;
import com.turt2live.antishare.configuration.groups.ConsolidatedGroup;
import com.turt2live.antishare.configuration.groups.Group;
import com.turt2live.antishare.events.EventDispatcher;
import com.turt2live.antishare.events.worldengine.WorldEngineShutdownEvent;
import com.turt2live.antishare.io.BlockManager;
import com.turt2live.antishare.io.memory.MemoryBlockManager;
import com.turt2live.antishare.utils.ASUtils;

import java.util.List;
import java.util.Map;

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
        RejectionList reject = new DefaultRejectionList(RejectionList.ListType.BLOCK_PLACE);

        if (groups != null && groups.size() > 0) {
            ConsolidatedGroup consolidatedGroup = new ConsolidatedGroup(groups);

            list = consolidatedGroup.getTrackedList(placeAs);
            reject = consolidatedGroup.getRejectionList(reject.getType());
            placeAs = consolidatedGroup.getActingMode(placeAs);
        }

        if (placeAs == ASGameMode.CREATIVE) { // TODO: Possible implementation of 'affect'?
            TrackedState playerReaction = block.canPlace(player); // See javadocs
            if (playerReaction == TrackedState.NEGATED) return true; // Straight up deny
            if (reject.isBlocked(block) && playerReaction == TrackedState.NOT_PRESENT) { // No allow permission & is denied
                return true;
            }
        }

        if (list.isTracked(block) && !player.hasPermission(PermissionNodes.FREE_PLACE)) {
            blockManager.setBlockType(block.getLocation(), ASUtils.toBlockType(placeAs));
        }

        return false;
    }

    /**
     * Processes a block grow event. This will internally determine what needs to be done
     * to maintain plugin settings as well as systems operation. There is no need for
     * a cancel flag as this event is purely informational.
     *
     * @param parent the parent block, cannot be null
     * @param child  the child block, cannot be null
     */
    // TODO: Unit test
    public void processBlockGrow(ABlock parent, ABlock child) {
        if (parent == null || child == null) throw new IllegalArgumentException();

        if (Engine.getInstance().isPhysicsGrowWithGamemode()) {
            BlockType current = blockManager.getBlockType(parent.getLocation());
            blockManager.setBlockType(child.getLocation(), current);
        }
    }

    /**
     * Processes a 'random' block break due to physics (such as water breaking seeds
     * or sand breaking torches). Internally this will determine whether or not drops
     * should be dropped (returning the appropriate return value) and update the system
     * to maintain accuracy.
     *
     * @param block the block being broken, cannot be null
     * @return true if there should be block drops, false otherwise
     */
    // TODO: Unit test
    public boolean processBlockPhysicsBreak(ABlock block) {
        if (block == null) throw new IllegalArgumentException();

        BlockType current = blockManager.getBlockType(block.getLocation());
        if (Engine.getInstance().isPhysicsBreakAsGamemode()) {
            return current != BlockType.CREATIVE;
        }
        return true;
    }

    /**
     * Processes an explosion from the world. This takes in a map of blocks which
     * are affected by the blast with boolean flags of whether or not they should
     * be exposed to the blast's effects (IE: Drop items). The flag will be set to
     * FALSE if a particular block should NOT drop items due to the blast.
     *
     * @param blocks the blocks affected by the blast, cannot be null
     */
    // TODO: Unit test
    public void processExplosion(Map<ABlock, Boolean> blocks) {
        if (blocks == null) throw new IllegalArgumentException();

        if (!Engine.getInstance().isPhysicsBreakAsGamemode()) return; // Don't handle this if we aren't supposed to

        for (Map.Entry<ABlock, Boolean> entry : blocks.entrySet()) {
            BlockType current = blockManager.getBlockType(entry.getKey().getLocation());
            blockManager.setBlockType(entry.getKey().getLocation(), BlockType.UNKNOWN);
            if (current == BlockType.CREATIVE) {
                entry.setValue(false); // Set flag off
            }
        }
    }
}
