package com.turt2live.antishare.engine;

import com.turt2live.antishare.*;
import com.turt2live.antishare.configuration.groups.ConsolidatedGroup;
import com.turt2live.antishare.configuration.groups.Group;
import com.turt2live.antishare.events.EventDispatcher;
import com.turt2live.antishare.events.worldengine.WorldEngineShutdownEvent;
import com.turt2live.antishare.io.BlockManager;
import com.turt2live.antishare.io.memory.MemoryBlockManager;
import com.turt2live.antishare.utils.ASUtils;

import java.util.ArrayList;
import java.util.HashMap;
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
    // TODO: Update unit test
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

        BlockType blockType = ASUtils.toBlockType(placeAs);

        // Check rejection lists
        if (placeAs == ASGameMode.CREATIVE) { // TODO: Possible implementation of 'affect'?
            TrackedState playerReaction = block.canPlace(player); // See javadocs
            if (playerReaction == TrackedState.NEGATED) return true; // Straight up deny
            if (reject.isBlocked(block) && playerReaction == TrackedState.NOT_PRESENT) { // No allow permission & is denied
                return true;
            }
        }

        // Check for block type insertion
        if (player.hasPermission(PermissionNodes.FREE_PLACE)) blockType = BlockType.UNKNOWN;

        // Check for double chests
        ABlock.ChestType blockChest = block.getChestType();
        ABlock additional = null; // Additional block to be assigned the BlockType
        switch (blockChest) {
            // They are double chests at this moment in time
            case DOUBLE_NORMAL:
            case DOUBLE_TRAPPED:
                // We only need to check these two for combination states

                ASLocation location1 = block.getLocation().add(1, 0, 0);
                ASLocation location2 = block.getLocation().add(-1, 0, 0);
                ASLocation location3 = block.getLocation().add(0, 0, 1);
                ASLocation location4 = block.getLocation().add(0, 0, -1);

                List<ABlock> blocks = new ArrayList<ABlock>();
                blocks.add(block.getWorld().getBlock(location1));
                blocks.add(block.getWorld().getBlock(location2));
                blocks.add(block.getWorld().getBlock(location3));
                blocks.add(block.getWorld().getBlock(location4));

                /*
                Minecraft will only allow one chest to be linked, and the logic below
                leverages that to ensure that the proper block types are assigned.

                 In the following image, red boxes indicates where Minecraft will deny
                 placement (too many chests) while green indicates where Minecraft will
                 allow placement (just one chest). The code below will therefore test
                 the 'green' conditions.

                 http://imgur.com/Alm3F9i
                 */

                for (ABlock block1 : blocks) {
                    if (block1.getChestType() == blockChest) {
                        BlockType type = blockManager.getBlockType(block1.getLocation());
                        if (type != blockType && type != BlockType.UNKNOWN) {
                            // Override deny action for mismatch gamemode on 'op-place'
                            if (blockType == BlockType.UNKNOWN) {
                                additional = block1;
                                break;
                            }
                            return true; // Mismatch gamemode
                        } else if (type == BlockType.UNKNOWN) {
                            additional = block1;
                            break; // We're done here, green condition check complete
                        }
                    }
                }

                break;
            default:
                break;
        }

        // If we made it this far, the block is OK, so just add it and return
        if (list.isTracked(block) || additional != null) {
            blockManager.setBlockType(block.getLocation(), blockType);
            if (additional != null) blockManager.setBlockType(additional.getLocation(), blockType);
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
     * Processes a multi-block possibility of stems. This is generally used for
     * when (example) pumpkins are spawned with a pumpkin seed stem. This will
     * handle a single stem as expected but will perform a merge-like process
     * on multiple stem scenarios (such as multiple possible stems). This means
     * that if there is a majority of a particular BlockType of stems, that block
     * type will be applied to the spawned block. This also means that if all stems
     * are of the same type (being none or otherwise), the spawned block will
     * inherit that type.
     *
     * @param spawned the spawned block, cannot be null
     * @param stems   the possible stems for the spawned block, cannot be null
     */
    // TODO: Unit test
    public void processBlockStems(ABlock spawned, List<ABlock> stems) {
        if (spawned == null || stems == null) throw new IllegalArgumentException();

        if (!Engine.getInstance().isPhysicsGrowWithGamemode()) return;

        Map<BlockType, Integer> votes = new HashMap<BlockType, Integer>();
        for (ABlock stem : stems) {
            BlockType type = blockManager.getBlockType(stem.getLocation());
            int amount = votes.containsKey(type) ? votes.get(type) : 0;
            amount++;
            votes.put(type, amount);
        }

        // First check for all equal
        if (votes.size() == 0) {
            blockManager.setBlockType(spawned.getLocation(), BlockType.UNKNOWN); // For sanity
        } else if (votes.size() == 1) {
            blockManager.setBlockType(spawned.getLocation(), votes.keySet().iterator().next());
        } else {
            BlockType highest = null;
            int count = 0;
            boolean allSame = true;
            for (Map.Entry<BlockType, Integer> vote : votes.entrySet()) {
                if (highest == null) {
                    highest = vote.getKey();
                    count = vote.getValue();
                } else {
                    if (count != vote.getValue()) {
                        allSame = false;
                    }
                    if (count < vote.getValue()) {
                        highest = vote.getKey();
                        count = vote.getValue();
                    }
                }
            }
            if (!allSame) {
                blockManager.setBlockType(spawned.getLocation(), highest);
            } else {
                blockManager.setBlockType(spawned.getLocation(), BlockType.UNKNOWN); // For sanity
            }
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

    /**
     * Processes a falling block spawn. This will internally check the block manager
     * to ensure that the spawn location is cleared of it's old value. The OutputParameter
     * is supplied to permit the return value of the block type.
     *
     * @param block the block being removed (spawning the falling block), cannot be null
     * @param type  the output parameter for the block type, may be null
     * @return whether or not the resulting falling block should drop items (true for drop, false otherwise)
     */
    // TODO: Unit test
    public boolean processFallingBlockSpawn(ABlock block, OutputParameter<BlockType> type) {
        if (block == null) throw new IllegalArgumentException();

        BlockType current = blockManager.getBlockType(block.getLocation());
        blockManager.setBlockType(block.getLocation(), BlockType.UNKNOWN);
        if (type != null) {
            type.setValue(current);
        }
        if (Engine.getInstance().isPhysicsBreakAsGamemode() && current == BlockType.CREATIVE) {
            return false;
        }
        return true;
    }

    /**
     * Processes a falling block landing. This will accept the entity's block type to be stored
     * into the block manager. Other checks may be performed.
     *
     * @param block the block location which is landing, cannot be null
     * @param type  the block type, null is interpretted as {@link com.turt2live.antishare.BlockType#UNKNOWN}
     */
    // TODO: Unit test
    public void processFallingBlockLand(ABlock block, BlockType type) {
        if (block == null) throw new IllegalArgumentException();
        if (type == null) type = BlockType.UNKNOWN;

        blockManager.setBlockType(block.getLocation(), type);
    }

    /**
     * Processes a structure growth (such as a tree) by taking the source block
     * and all applicable blocks into consideration.
     *
     * @param source    the source block, cannot be null
     * @param structure the structure blocks, cannot be null
     */
    // TODO: Unit test
    public void processStructure(ABlock source, List<ABlock> structure) {
        if (source == null || structure == null) throw new IllegalArgumentException();

        BlockType type = blockManager.getBlockType(source.getLocation());
        if (Engine.getInstance().isPhysicsGrowWithGamemode()) {
            for (ABlock block : structure) {
                blockManager.setBlockType(block.getLocation(), type);
            }
        }
    }

    /**
     * Processes block interaction between two blocks. This may be two hoppers (for
     * example) where items are being transferred between the two. Assuming the setting
     * in the Engine is set, this will run through various checks (as outlined below) to
     * determine if the interaction should be blocked. This will return 'false' to indicate
     * the action should be BLOCKED and 'true' to indicate the action is permitted.
     * <p/>
     * The order of the arguments is not important as the cases are applied regardless of
     * the 'source'. Order dependency must be determined, if needed, by the implementation.
     * <p/>
     * Assuming the {@link com.turt2live.antishare.engine.Engine#isHopperMixedInteractionDenied()}
     * flag is set...
     * <ul>
     * <li>Case: Non-natural to Non-natural of different types: Denied</li>
     * <li>Case: Non-natural to Non-natural of same types: Allowed</li>
     * <li>Case: Non-natural to Natural: Allowed</li>
     * <li>Case: Natural to Non-natural: Allowed</li>
     * <li>Case: Natural to Natural: Allowed</li>
     * </ul>
     * <p/>
     * Assuming the flag is false...
     * <ul>
     * <li>All cases allowed</li>
     * </ul>
     *
     * @param block1 the first block, cannot be null
     * @param block2 the second block, cannot be null
     * @return the permitted flag; true for allowed, false otherwise
     */
    // TODO: Unit test
    public boolean processBlockInteraction(ABlock block1, ABlock block2) {
        if (block1 == null || block2 == null) throw new IllegalArgumentException();

        BlockType type1 = blockManager.getBlockType(block1.getLocation());
        BlockType type2 = blockManager.getBlockType(block2.getLocation());

        if (type1 == type2 || type1 == BlockType.UNKNOWN || type2 == BlockType.UNKNOWN)
            return true;
        else return false;
    }
}
