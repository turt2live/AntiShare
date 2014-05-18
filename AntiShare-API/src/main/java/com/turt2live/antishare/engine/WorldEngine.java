/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.engine;

import com.turt2live.antishare.APermission;
import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.configuration.groups.ConsolidatedGroup;
import com.turt2live.antishare.configuration.groups.Group;
import com.turt2live.antishare.engine.list.*;
import com.turt2live.antishare.events.EventDispatcher;
import com.turt2live.antishare.events.worldengine.WorldEngineShutdownEvent;
import com.turt2live.antishare.io.BlockManager;
import com.turt2live.antishare.io.EntityManager;
import com.turt2live.antishare.io.memory.MemoryBlockManager;
import com.turt2live.antishare.io.memory.MemoryEntityManager;
import com.turt2live.antishare.object.*;
import com.turt2live.antishare.object.attribute.Facing;
import com.turt2live.antishare.object.attribute.ObjectType;
import com.turt2live.antishare.object.attribute.TrackedState;
import com.turt2live.antishare.utils.ASUtils;
import com.turt2live.antishare.utils.BlockTypeTransaction;
import com.turt2live.antishare.utils.OutputParameter;

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
    private EntityManager entityManager = new MemoryEntityManager();

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
        DevEngine.log("[WorldEngine:" + worldName + "] Shutting down");
        EventDispatcher.dispatch(new WorldEngineShutdownEvent(this));
        blockManager.saveAll();
        entityManager.save();
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

        DevEngine.log("[WorldEngine:" + worldName + "] New block manager: " + blockManager.getClass().getName());
        this.blockManager = blockManager;
    }

    /**
     * Gets the entity manager for this engine
     *
     * @return the entity manager
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Sets the entity manager to use. This will overwrite the previous one and NOT perform
     * any save routines on the previous manager, nor will this perform any load operations
     * on the new manager.
     *
     * @param manager the new manager, cannot be null
     */
    public void setEntityManager(EntityManager manager) {
        if (manager == null) throw new IllegalArgumentException("entity manager cannot be null");

        DevEngine.log("[WorldEngine:" + worldName + "] New entity manager: " + manager.getClass().getName());
        this.entityManager = manager;
    }

    /**
     * Gets the tracking list for a specified gamemode
     *
     * @param gamemode the gamemode, cannot be null
     *
     * @return the list
     */
    public TrackedTypeList getTrackedBlocks(ASGameMode gamemode) {
        List<Group> allGroups = Engine.getInstance().getGroupManager().getGroupsForWorld(getWorldName(), false);
        if (allGroups == null || allGroups.size() <= 0) return new DefaultTrackedTypeList();
        ConsolidatedGroup consolidatedGroup = new ConsolidatedGroup(allGroups);
        return consolidatedGroup.getBlockTrackedList(gamemode);
    }

    /**
     * Processes a block placement, ensuring the player is allowed to place the block as well
     * as running the tracking logic.
     *
     * @param player  the player placing the block, cannot be null
     * @param block   the block being placed, cannot be null
     * @param placeAs the gamemode placing the block
     *
     * @return returns true if the block placement was rejected, false otherwise
     */
    // TODO: Unit test
    public boolean processBlockPlace(APlayer player, ABlock block, ASGameMode placeAs) {
        if (player == null || block == null || placeAs == null) throw new IllegalArgumentException();

        DevEngine.log("[WorldEngine:" + worldName + "] Processing block place",
                "[WorldEngine:" + worldName + "] \t\tplayer = " + player,
                "[WorldEngine:" + worldName + "] \t\tblock = " + block,
                "[WorldEngine:" + worldName + "] \t\tplaceAs = " + placeAs);

        List<Group> groups = Engine.getInstance().getGroupManager().getGroupsForPlayer(player, false);
        TrackedTypeList list = new DefaultTrackedTypeList();
        RejectionList reject = new DefaultRejectionList(RejectionList.ListType.BLOCK_PLACE);

        if (groups != null && groups.size() > 0) {
            ConsolidatedGroup consolidatedGroup = new ConsolidatedGroup(groups);

            list = consolidatedGroup.getBlockTrackedList(placeAs);
            reject = consolidatedGroup.getRejectionList(reject.getType());
            placeAs = consolidatedGroup.getActingMode(placeAs);
        }

        ObjectType objectType = ASUtils.toBlockType(placeAs);

        // Check rejection lists
        if (placeAs == ASGameMode.CREATIVE) { // TODO: Possible implementation of 'affect'?
            TrackedState playerReaction = block.canPlace(player); // See javadocs
            if (playerReaction == TrackedState.NEGATED) return true; // Straight up deny
            if (reject.isBlocked(block) && playerReaction == TrackedState.NOT_PRESENT) { // No allow permission & is denied
                return true;
            }
        }

        // Check for block type insertion
        if (player.hasPermission(APermission.FREE_PLACE)) objectType = ObjectType.UNKNOWN;

        // Check for double chests
        ABlock.ChestType blockChest = block.getChestType();
        ABlock additional = null; // Additional block to be assigned the ObjectType
        switch (blockChest) {
            // They are double chests at this moment in time
            case DOUBLE_NORMAL:
            case DOUBLE_TRAPPED:
                // We only need to check these two for combination states


                List<ABlock> blocks = new ArrayList<ABlock>();
                blocks.add(block.getWorld().getBlock(block.getLocation().add(1, 0, 0)));
                blocks.add(block.getWorld().getBlock(block.getLocation().add(-1, 0, 0)));
                blocks.add(block.getWorld().getBlock(block.getLocation().add(0, 0, 1)));
                blocks.add(block.getWorld().getBlock(block.getLocation().add(0, 0, -1)));

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
                        ObjectType type = blockManager.getBlockType(block1.getLocation());
                        if (type != objectType && type != ObjectType.UNKNOWN) {
                            // Override deny action for mismatch gamemode on 'op-place'
                            if (objectType == ObjectType.UNKNOWN) {
                                additional = block1;
                                break;
                            }
                            return true; // Mismatch gamemode
                        } else if (type == ObjectType.UNKNOWN) {
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
            blockManager.setBlockType(block.getLocation(), objectType);
            if (additional != null) blockManager.setBlockType(additional.getLocation(), objectType);
        }

        return false;
    }

    /**
     * Processes the block break, running the required logic to break blocks as the player. This will
     * also, if the player is permitted, check attached blocks to be sure that the player is capable
     * of breaking the block as well. This will run several internal checks. The output parameter, if
     * supplied, will always be populated.
     *
     * @param player          the player breaking the block, cannot be null
     * @param block           the block being broken, cannot be null
     * @param breakAs         the gamemode the player is breaking the block as, cannot be null
     * @param additionalBreak the optional output for additional blocks to break with NO DROPS, can be null
     * @param eventBreakAs    the optional output for what type the block was before it was removed from the system
     *
     * @return returns true if the block break was rejected, false otherwise
     */
    // TODO: Unit test
    public boolean processBlockBreak(APlayer player, ABlock block, ASGameMode breakAs, OutputParameter<List<ABlock>> additionalBreak, OutputParameter<ObjectType> eventBreakAs) {
        if (player == null || block == null || breakAs == null) throw new IllegalArgumentException();

        DevEngine.log("[WorldEngine:" + worldName + "] Processing block break",
                "[WorldEngine:" + worldName + "] \t\tplayer = " + player,
                "[WorldEngine:" + worldName + "] \t\tblock = " + block,
                "[WorldEngine:" + worldName + "] \t\tbreakAs = " + breakAs,
                "[WorldEngine:" + worldName + "] \t\tadditionalBreak = " + additionalBreak,
                "[WorldEngine:" + worldName + "] \t\teventBreakAs = " + eventBreakAs);

        List<ABlock> additional = new ArrayList<ABlock>();
        if (additionalBreak != null) additionalBreak.setValue(additional);

        List<Group> groups = Engine.getInstance().getGroupManager().getGroupsForPlayer(player, false);
        RejectionList reject = new DefaultRejectionList(RejectionList.ListType.BLOCK_BREAK);

        if (groups != null && groups.size() > 0) {
            ConsolidatedGroup consolidatedGroup = new ConsolidatedGroup(groups);

            reject = consolidatedGroup.getRejectionList(reject.getType());
            breakAs = consolidatedGroup.getActingMode(breakAs);
        }

        ObjectType objectType = ASUtils.toBlockType(breakAs);

        // Check rejection lists
        if (breakAs == ASGameMode.CREATIVE) { // TODO: Possible implementation of 'affect'?
            TrackedState playerReaction = block.canBreak(player); // See javadocs
            if (playerReaction == TrackedState.NEGATED) return true; // Straight up deny
            if (reject.isBlocked(block) && playerReaction == TrackedState.NOT_PRESENT) { // No allow permission & is denied
                return true;
            }
        }

        ObjectType objectType1 = blockManager.getBlockType(block.getLocation());
        if (!player.hasPermission(APermission.FREE_BREAK)) {
            if (objectType1 != objectType && objectType1 != ObjectType.UNKNOWN)
                return true; // Mixed gamemode

            List<ABlock> possibleAttachments = new ArrayList<ABlock>();
            possibleAttachments.add(block.getWorld().getBlock(block.getLocation().add(1, 0, 0)));
            possibleAttachments.add(block.getWorld().getBlock(block.getLocation().add(-1, 0, 0)));
            possibleAttachments.add(block.getWorld().getBlock(block.getLocation().add(0, 0, 1)));
            possibleAttachments.add(block.getWorld().getBlock(block.getLocation().add(0, 0, -1)));
            possibleAttachments.add(block.getWorld().getBlock(block.getLocation().add(0, 1, 0))); // Above

            for (ABlock possibleAttachment : possibleAttachments) {
                if (possibleAttachment.isAttached(block)) {
                    ObjectType attachType = blockManager.getBlockType(possibleAttachment.getLocation());
                    if (attachType == ObjectType.UNKNOWN) continue; // Let it break normally

                    if (Engine.getInstance().getFlag(Engine.CONFIG_MISMATCHED_ATTACHMENTS_DENY, true) && attachType != objectType1 && objectType1 != ObjectType.UNKNOWN) {
                        return true; // As the owner wishes...
                    }
                    if (Engine.getInstance().getFlag(Engine.CONFIG_BREAK_ATTACHMENTS_AS_PLACED, true) && attachType == ObjectType.CREATIVE) { // TODO: Possible 'affect'?
                        additional.add(possibleAttachment);
                    }// Implementation has to handle the removal. IE: Fade or 'disappear'
                }
            }
        }

        if (eventBreakAs != null) {
            eventBreakAs.setValue(objectType1);
        }

        blockManager.setBlockType(block.getLocation(), ObjectType.UNKNOWN);
        return false; // Break was processed
    }

    /**
     * Processes a block 'disappearing' or 'fading'
     *
     * @param block the block, cannot be null
     */
    // TODO: Unit test
    public void processFade(ABlock block) {
        if (block == null) throw new IllegalArgumentException();

        DevEngine.log("[WorldEngine:" + worldName + "] Processing block fade",
                "[WorldEngine:" + worldName + "] \t\tblock = " + block);

        blockManager.setBlockType(block.getLocation(), ObjectType.UNKNOWN);
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

        DevEngine.log("[WorldEngine:" + worldName + "] Processing block grow",
                "[WorldEngine:" + worldName + "] \t\tparent = " + parent,
                "[WorldEngine:" + worldName + "] \t\tchild = " + child);

        if (Engine.getInstance().getFlag(Engine.CONFIG_PHYSICS_GROW_WITH_GAMEMODE, true)) {
            ObjectType current = blockManager.getBlockType(parent.getLocation());
            blockManager.setBlockType(child.getLocation(), current);
        }
    }

    /**
     * Processes a multi-block possibility of stems. This is generally used for
     * when (example) pumpkins are spawned with a pumpkin seed stem. This will
     * handle a single stem as expected but will perform a merge-like process
     * on multiple stem scenarios (such as multiple possible stems). This means
     * that if there is a majority of a particular ObjectType of stems, that block
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

        DevEngine.log("[WorldEngine:" + worldName + "] Processing block stems",
                "[WorldEngine:" + worldName + "] \t\tspawned = " + spawned,
                "[WorldEngine:" + worldName + "] \t\tstems = " + stems);

        if (!Engine.getInstance().getFlag(Engine.CONFIG_PHYSICS_GROW_WITH_GAMEMODE, true)) return;

        Map<ObjectType, Integer> votes = new HashMap<ObjectType, Integer>();
        for (ABlock stem : stems) {
            ObjectType type = blockManager.getBlockType(stem.getLocation());
            int amount = votes.containsKey(type) ? votes.get(type) : 0;
            amount++;
            votes.put(type, amount);
        }

        // First check for all equal
        if (votes.size() == 0) {
            blockManager.setBlockType(spawned.getLocation(), ObjectType.UNKNOWN); // For sanity
        } else if (votes.size() == 1) {
            blockManager.setBlockType(spawned.getLocation(), votes.keySet().iterator().next());
        } else {
            ObjectType highest = null;
            int count = 0;
            boolean allSame = true;
            for (Map.Entry<ObjectType, Integer> vote : votes.entrySet()) {
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
                blockManager.setBlockType(spawned.getLocation(), ObjectType.UNKNOWN); // For sanity
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
     *
     * @return true if there should be block drops, false otherwise
     */
    // TODO: Unit test
    public boolean processBlockPhysicsBreak(ABlock block) {
        if (block == null) throw new IllegalArgumentException();

        DevEngine.log("[WorldEngine:" + worldName + "] Processing block physics (break)",
                "[WorldEngine:" + worldName + "] \t\tblock = " + block);

        ObjectType current = blockManager.getBlockType(block.getLocation());
        if (Engine.getInstance().getFlag(Engine.CONFIG_PHYSICS_BREAK_AS_GAMEMODE, true)) {
            return current != ObjectType.CREATIVE;
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

        DevEngine.log("[WorldEngine:" + worldName + "] Processing explosion",
                "[WorldEngine:" + worldName + "] \t\tblocks = " + blocks);

        if (!Engine.getInstance().getFlag(Engine.CONFIG_PHYSICS_BREAK_AS_GAMEMODE, true))
            return; // Don't handle this if we aren't supposed to

        for (Map.Entry<ABlock, Boolean> entry : blocks.entrySet()) {
            ObjectType current = blockManager.getBlockType(entry.getKey().getLocation());
            blockManager.setBlockType(entry.getKey().getLocation(), ObjectType.UNKNOWN);
            if (current == ObjectType.CREATIVE) {
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
     *
     * @return whether or not the resulting falling block should drop items (true for drop, false otherwise)
     */
    // TODO: Unit test
    public boolean processFallingBlockSpawn(ABlock block, OutputParameter<ObjectType> type) {
        if (block == null) throw new IllegalArgumentException();

        DevEngine.log("[WorldEngine:" + worldName + "] Processing falling block spawn",
                "[WorldEngine:" + worldName + "] \t\tblock = " + block,
                "[WorldEngine:" + worldName + "] \t\ttype = " + type);

        ObjectType current = blockManager.getBlockType(block.getLocation());
        blockManager.setBlockType(block.getLocation(), ObjectType.UNKNOWN);
        if (type != null) {
            type.setValue(current);
        }
        if (Engine.getInstance().getFlag(Engine.CONFIG_PHYSICS_BREAK_AS_GAMEMODE, true) && current == ObjectType.CREATIVE) {
            return false;
        }
        return true;
    }

    /**
     * Processes a falling block landing. This will accept the entity's block type to be stored
     * into the block manager. Other checks may be performed.
     *
     * @param block the block location which is landing, cannot be null
     * @param type  the block type, null is interpretted as {@link com.turt2live.antishare.object.attribute.ObjectType#UNKNOWN}
     */
    // TODO: Unit test
    public void processFallingBlockLand(ABlock block, ObjectType type) {
        if (block == null) throw new IllegalArgumentException();
        if (type == null) type = ObjectType.UNKNOWN;

        DevEngine.log("[WorldEngine:" + worldName + "] Processing falling block land",
                "[WorldEngine:" + worldName + "] \t\tblock = " + block,
                "[WorldEngine:" + worldName + "] \t\ttype = " + type);

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

        DevEngine.log("[WorldEngine:" + worldName + "] Processing structure",
                "[WorldEngine:" + worldName + "] \t\tsource = " + source,
                "[WorldEngine:" + worldName + "] \t\tstructure = " + structure);

        ObjectType type = blockManager.getBlockType(source.getLocation());
        if (Engine.getInstance().getFlag(Engine.CONFIG_PHYSICS_GROW_WITH_GAMEMODE, true)) {
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
     * Assuming the {@link com.turt2live.antishare.engine.Engine#CONFIG_HOPPER_MISMATCH_INTERACTION}
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
     *
     * @return the permitted flag; true for allowed, false otherwise
     */
    // TODO: Unit test
    public boolean processBlockInteraction(ABlock block1, ABlock block2) {
        if (block1 == null || block2 == null) throw new IllegalArgumentException();

        DevEngine.log("[WorldEngine:" + worldName + "] Processing block interaction",
                "[WorldEngine:" + worldName + "] \t\tblock1 = " + block1,
                "[WorldEngine:" + worldName + "] \t\tblock2 = " + block2);

        ObjectType type1 = blockManager.getBlockType(block1.getLocation());
        ObjectType type2 = blockManager.getBlockType(block2.getLocation());

        if (!Engine.getInstance().getFlag(Engine.CONFIG_HOPPER_MISMATCH_INTERACTION, true)) return false;

        if (type1 == type2 || type1 == ObjectType.UNKNOWN || type2 == ObjectType.UNKNOWN)
            return true;
        else return false;
    }

    /**
     * Processes a piston move (retract or extend). This will internally check to see which
     * blocks are being affected by the move and adjust accordingly. This will return a true
     * or false value based upon the 'allowance' of the piston move ('false' for denied). If
     * this does not determine that the action is prohibited, the blocks associated with the
     * move will be updated internally as needed.
     * <p/>
     * The list of blocks is assumed to be moving 1 block in the facing direction of the piston.
     * If the 'retract' flag is set to true, then this will check the 'sticky' flag. If the piston
     * is considered 'sticky', then the blocks will be assumed to be moving 1 block in the opposite
     * direction of the piston facing direction. If the piston is retracting and is not sticky, this
     * will do nothing.
     * <p/>
     * This will not verify that the chain will not cause damage to existing blocks. This behaviour
     * is considered the "+1 bug" and is present in CraftBukkit. This will not accomodate for the +1
     * bug.
     *
     * @param piston    the piston that is moving, cannot be null
     * @param blocks    the blocks being moved by the piston, cannot be null. Empty lists are ignored.
     * @param direction the direction the piston is extending/retracting in, cannot be null
     * @param retract   flag for piston retraction
     * @param sticky    flag for a piston being sticky (or not)
     *
     * @return true if this piston is permitted to move, false otherwise
     */
    // TODO: Unit test
    public boolean processPistonMove(ABlock piston, List<ABlock> blocks, Facing direction, boolean retract, boolean sticky) {
        if (piston == null || blocks == null || direction == null) throw new IllegalArgumentException();

        DevEngine.log("[WorldEngine:" + worldName + "] Processing piston move",
                "[WorldEngine:" + worldName + "] \t\tpiston = " + piston,
                "[WorldEngine:" + worldName + "] \t\tblocks = " + blocks,
                "[WorldEngine:" + worldName + "] \t\tdirection = " + direction,
                "[WorldEngine:" + worldName + "] \t\tretract = " + retract,
                "[WorldEngine:" + worldName + "] \t\tsticky = " + sticky);

        if (retract && !sticky) return true;
        if (blocks.isEmpty()) return true;

        BlockTypeTransaction transaction = new BlockTypeTransaction();
        ObjectType pistonType = blockManager.getBlockType(piston.getLocation());
        if (pistonType != ObjectType.UNKNOWN && Engine.getInstance().getFlag(Engine.CONFIG_PISTON_MISMATCH, true)) {
            // Now we have to check all the block types
            for (ABlock block : blocks) {
                ObjectType type = blockManager.getBlockType(block.getLocation());
                if (type != ObjectType.UNKNOWN && type != pistonType)
                    return false;  // Mismatch piston & block, deny
            }
        }

        // If we made it here, all we have to do is update the block types
        for (ABlock block : blocks) {
            ObjectType old = blockManager.getBlockType(block.getLocation());
            ASLocation newLocation = block.getRelative(direction).getLocation();

            if (old == ObjectType.UNKNOWN) continue; // Don't overwrite blocks (duplicate events in Bukkit)

            blockManager.setBlockType(block.getLocation(), ObjectType.UNKNOWN);
            transaction.add(newLocation, old);
        }
        transaction.commit(blockManager);

        return true;
    }

    /**
     * Processes a player executing a command. This will internally determine if the player
     * is permitted to do so. If not, 'true' is returned to indicate 'deny'. 'false' is used
     * to indicate 'all is well'.
     *
     * @param player  the player executing the command, cannot be null
     * @param command the command being executed, cannot be null
     *
     * @return denial status, true being denied
     */
    // TODO: Unit test
    public boolean processCommandExecution(APlayer player, RejectableCommand command) {
        if (player == null || command == null) throw new IllegalArgumentException();

        DevEngine.log("[WorldEngine:" + worldName + "] Processing command",
                "[WorldEngine:" + worldName + "] \t\tplayer = " + player,
                "[WorldEngine:" + worldName + "] \t\tcommand = " + command);

        List<Group> groups = Engine.getInstance().getGroupManager().getGroupsForPlayer(player, false);
        RejectionList reject = new CommandRejectionList();
        ASGameMode playerGM = player.getGameMode();

        if (groups != null && groups.size() > 0) {
            ConsolidatedGroup consolidatedGroup = new ConsolidatedGroup(groups);

            reject = consolidatedGroup.getRejectionList(reject.getType());
            playerGM = consolidatedGroup.getActingMode(playerGM);
        }

        if (playerGM != ASGameMode.CREATIVE) return false; // TODO: Possible implementation of 'affect'?

        // Check lists and permissions
        TrackedState playerReaction = command.canExecute(player);
        if (playerReaction == TrackedState.NEGATED) return true; // Straight up deny
        if (reject.isBlocked(command) && playerReaction == TrackedState.NOT_PRESENT)
            return true; // Rejected & no allow permission
        return false; // Allowed
    }

    /**
     * Processes a player opening a container, such as a chest. This will internally
     * handle the appropriate flags for the event. This is a reactionary event and
     * has no deny/allow nodes. This will assume that the passed container is a
     * container.
     *
     * @param player    the player interacting, cannot be null
     * @param container the container being opened, cannot be null
     */
    // TODO: Unit test
    public void processContainerOpen(APlayer player, ABlock container) {
        if (player == null || container == null) throw new IllegalArgumentException();

        DevEngine.log("[WorldEngine:" + worldName + "] Processing container",
                "[WorldEngine:" + worldName + "] \t\tplayer = " + player,
                "[WorldEngine:" + worldName + "] \t\tcontainer = " + container);

        if (Engine.getInstance().getFlag(Engine.CONFIG_INTERACT_CONTAINER_INHERIT, true)) {
            if (!player.hasPermission(APermission.FREE_TOUCH)) {
                List<Group> groups = Engine.getInstance().getGroupManager().getGroupsForPlayer(player, false);
                ASGameMode playerGM = player.getGameMode();

                if (groups != null && groups.size() > 0) {
                    ConsolidatedGroup consolidatedGroup = new ConsolidatedGroup(groups);

                    playerGM = consolidatedGroup.getActingMode(playerGM);
                }

                if (blockManager.getBlockType(container.getLocation()) == ObjectType.UNKNOWN) {
                    blockManager.setBlockType(container.getLocation(), ASUtils.toBlockType(playerGM));
                    if (container.getOtherChest() != null)
                        blockManager.setBlockType(container.getOtherChest().getLocation(), ASUtils.toBlockType(playerGM));
                }
            }
        }
    }

    /**
     * Processes an interaction between a player and a block. This will determine
     * whether or not the player is permitted to use the block. If the player is
     * NOT allowed to use the block, true is returned. False is returned for all
     * other values.
     *
     * @param player the player interacting, cannot be null
     * @param block  the victimized block, cannot be null
     *
     * @return true if the action is denied, false otherwise
     */
    // TODO: Unit test
    public boolean processInteraction(APlayer player, ABlock block) {
        if (player == null || block == null) throw new IllegalArgumentException();

        DevEngine.log("[WorldEngine:" + worldName + "] Processing interaction",
                "[WorldEngine:" + worldName + "] \t\tplayer = " + player,
                "[WorldEngine:" + worldName + "] \t\tblock = " + block);

        ObjectType otherType = blockManager.getBlockType(block.getLocation());
        ObjectType interactAs = ASUtils.toBlockType(player.getGameMode());
        List<Group> groups = Engine.getInstance().getGroupManager().getGroupsForPlayer(player, false);
        RejectionList reject = new DefaultRejectionList(RejectionList.ListType.INTERACTION);
        ASGameMode playerGM = player.getGameMode();

        if (groups != null && groups.size() > 0) {
            ConsolidatedGroup consolidatedGroup = new ConsolidatedGroup(groups);

            reject = consolidatedGroup.getRejectionList(reject.getType());
            interactAs = ASUtils.toBlockType(consolidatedGroup.getActingMode(player.getGameMode()));
            playerGM = consolidatedGroup.getActingMode(playerGM);
        }

        if (Engine.getInstance().getFlag(Engine.CONFIG_INTERACT_CLASSIC_MODE, false)) {
            // Classic mode - Check the list and exit
            if (playerGM == ASGameMode.CREATIVE) { // TODO: Possible implementation of 'affect'?
                TrackedState playerReaction = block.canInteract(player);
                if (playerReaction == TrackedState.NEGATED) return true; // Straight up deny
                if (reject.isBlocked(block) && playerReaction == TrackedState.NOT_PRESENT)
                    return true; // Rejected & no allow permission
            }
        } else {
            // Non-classic mode - Start handling containers and stuff

            /*
            If the block is a container, then we should see if the container is natural.
            If the container is natural, and natural containers are denied, then we
            should deny. If the container is natural, and natural containers are permitted,
            then we should allow the interaction. We also ensure that the gamemode of the
            container matches the player gamemode.

            If the block is not a container, then we should see if it is on the blacklist.
            If it is on the blacklist, then we deny (if player is in creative mode).

            If the block is not a container and not on the blacklist, then we permit the action.
             */

            if (block.isContainer()) {
                if (!player.hasPermission(APermission.FREE_TOUCH)) {
                    if (interactAs != otherType && otherType != ObjectType.UNKNOWN) {
                        return true; // Inter-gamemode interaction, denied
                    }

                    if (playerGM == ASGameMode.CREATIVE && otherType == ObjectType.UNKNOWN) {
                        if (!Engine.getInstance().getFlag(Engine.CONFIG_INTERACT_NATURAL_CONTAINERS, false)) {
                            return true; // Creative players can't interact with natural containers
                        }
                    }
                }
            } else {
                // Default as 'classic mode'
                if (playerGM == ASGameMode.CREATIVE) { // TODO: Possible implementation of 'affect'?
                    TrackedState playerReaction = block.canInteract(player);
                    if (playerReaction == TrackedState.NEGATED) return true; // Straight up deny
                    if (reject.isBlocked(block) && playerReaction == TrackedState.NOT_PRESENT)
                        return true; // Rejected & no allow permission
                }
            }
        }

        return false; // Allowed
    }

    /**
     * Process an item use initiated by a player. This will internally determine
     * whether or not the player is allowed to use the item and return 'true' to
     * represent denial and 'false' to represent allowance.
     *
     * @param player the player using the item, cannot be null
     * @param item   the item in question, cannot be null
     *
     * @return true for denial, false otherwise
     */
    // TODO: Unit test
    public boolean processItemUse(APlayer player, AItem item) {
        if (player == null || item == null) throw new IllegalArgumentException();

        DevEngine.log("[WorldEngine:" + worldName + "] Processing player use",
                "[WorldEngine:" + worldName + "] \t\tplayer = " + player,
                "[WorldEngine:" + worldName + "] \t\titem = " + item);

        List<Group> groups = Engine.getInstance().getGroupManager().getGroupsForPlayer(player, false);
        RejectionList reject = new DefaultRejectionList(RejectionList.ListType.ITEM_USE);
        ASGameMode playerGM = player.getGameMode();

        if (groups != null && groups.size() > 0) {
            ConsolidatedGroup consolidatedGroup = new ConsolidatedGroup(groups);

            reject = consolidatedGroup.getRejectionList(reject.getType());
            playerGM = consolidatedGroup.getActingMode(playerGM);
        }

        if (playerGM != ASGameMode.CREATIVE) return false; // TODO: Possible implementation of 'affect'?

        // Check lists and permissions
        TrackedState playerReaction = item.canUse(player);
        if (playerReaction == TrackedState.NEGATED) return true; // Straight up deny
        if (reject.isBlocked(item) && playerReaction == TrackedState.NOT_PRESENT)
            return true; // Rejected & no allow permission
        return false;
    }

    /**
     * Process an item drop initiated by a player. This will internally determine
     * whether or not the player is allowed to drop the item and return 'true' to
     * represent denial and 'false' to represent allowance.
     *
     * @param player the player dropping the item, cannot be null
     * @param item   the item in question, cannot be null
     *
     * @return true for denial, false otherwise
     */
    // TODO: Unit test
    public boolean processItemDrop(APlayer player, AItem item) {
        if (player == null || item == null) throw new IllegalArgumentException();

        DevEngine.log("[WorldEngine:" + worldName + "] Processing player drop",
                "[WorldEngine:" + worldName + "] \t\tplayer = " + player,
                "[WorldEngine:" + worldName + "] \t\titem = " + item);

        List<Group> groups = Engine.getInstance().getGroupManager().getGroupsForPlayer(player, false);
        RejectionList reject = new DefaultRejectionList(RejectionList.ListType.ITEM_DROP);
        ASGameMode playerGM = player.getGameMode();

        if (groups != null && groups.size() > 0) {
            ConsolidatedGroup consolidatedGroup = new ConsolidatedGroup(groups);

            reject = consolidatedGroup.getRejectionList(reject.getType());
            playerGM = consolidatedGroup.getActingMode(playerGM);
        }

        if (playerGM != ASGameMode.CREATIVE) return false; // TODO: Possible implementation of 'affect'?

        // Check lists and permissions
        TrackedState playerReaction = item.canDrop(player);
        if (playerReaction == TrackedState.NEGATED) return true; // Straight up deny
        if (reject.isBlocked(item) && playerReaction == TrackedState.NOT_PRESENT)
            return true; // Rejected & no allow permission
        return false;
    }

    /**
     * Process an item throw initiated by a player. This will internally determine
     * whether or not the player is allowed to throw the item and return 'true' to
     * represent denial and 'false' to represent allowance.
     *
     * @param player the player throwing the item, cannot be null
     * @param item   the item in question, cannot be null
     *
     * @return true for denial, false otherwise
     */
    // TODO: Unit test
    public boolean processItemPickup(APlayer player, AItem item) {
        if (player == null || item == null) throw new IllegalArgumentException();

        DevEngine.log("[WorldEngine:" + worldName + "] Processing player pickup",
                "[WorldEngine:" + worldName + "] \t\tplayer = " + player,
                "[WorldEngine:" + worldName + "] \t\titem = " + item);

        List<Group> groups = Engine.getInstance().getGroupManager().getGroupsForPlayer(player, false);
        RejectionList reject = new DefaultRejectionList(RejectionList.ListType.ITEM_PICKUP);
        ASGameMode playerGM = player.getGameMode();

        if (groups != null && groups.size() > 0) {
            ConsolidatedGroup consolidatedGroup = new ConsolidatedGroup(groups);

            reject = consolidatedGroup.getRejectionList(reject.getType());
            playerGM = consolidatedGroup.getActingMode(playerGM);
        }

        if (playerGM != ASGameMode.CREATIVE) return false; // TODO: Possible implementation of 'affect'?

        // Check lists and permissions
        TrackedState playerReaction = item.canPickup(player);
        if (playerReaction == TrackedState.NEGATED) return true; // Straight up deny
        if (reject.isBlocked(item) && playerReaction == TrackedState.NOT_PRESENT)
            return true; // Rejected & no allow permission
        return false;
    }

    /**
     * Process an entity death, internally removing the entity from any
     * applicable tracking systems.
     *
     * @param killed the entity that was killed, cannot be null
     */
    // TODO: Unit test
    public void processEntityDeath(AEntity killed) {
        if (killed == null) throw new IllegalArgumentException();

        DevEngine.log("[WorldEngine:" + worldName + "] Processing entity death",
                "[WorldEngine:" + worldName + "] \t\tkilled = " + killed,
                "[WorldEngine:" + worldName + "] \t\tuid = " + killed.getUUID());

        getEntityManager().setType(killed.getUUID(), ObjectType.UNKNOWN);
    }

    /**
     * Processes an entity placement by a player. This will internally check
     * to see if the player is allowed to do so, and if not return 'true' to
     * represent denial.
     *
     * @param entity the entity being placed, cannot be null
     * @param player the player, cannot be null
     *
     * @returns true for denial, false otherwise
     */
    // TODO: Unit test
    public boolean processEntityPlace(AEntity entity, APlayer player) {
        if (entity == null || player == null) throw new IllegalArgumentException();

        DevEngine.log("[WorldEngine:" + worldName + "] Processing hanging placement",
                "[WorldEngine:" + worldName + "] \t\tentity = " + entity,
                "[WorldEngine:" + worldName + "] \t\tplayer = " + player);

        List<Group> groups = Engine.getInstance().getGroupManager().getGroupsForPlayer(player, false);
        TrackedTypeList list = new DefaultTrackedTypeList();
        RejectionList reject = new DefaultRejectionList(RejectionList.ListType.ENTITY_PLACE);
        ASGameMode playerGM = player.getGameMode();

        if (groups != null && groups.size() > 0) {
            ConsolidatedGroup consolidatedGroup = new ConsolidatedGroup(groups);

            list = consolidatedGroup.getEntityTrackedList(playerGM);
            reject = consolidatedGroup.getRejectionList(reject.getType());
            playerGM = consolidatedGroup.getActingMode(playerGM);
        }

        ObjectType objectType = ASUtils.toBlockType(playerGM);

        // Check rejection lists
        if (playerGM == ASGameMode.CREATIVE) { // TODO: Possible implementation of 'affect'?
            TrackedState playerReaction = entity.canPlace(player); // See javadocs
            if (playerReaction == TrackedState.NEGATED) return true; // Straight up deny
            if (reject.isBlocked(entity) && playerReaction == TrackedState.NOT_PRESENT) { // No allow permission & is denied
                return true;
            }
        }

        // Check for block type insertion
        if (player.hasPermission(APermission.FREE_PLACE)) objectType = ObjectType.UNKNOWN;

        // If we made it this far, the block is OK, so just add it and return
        if (list.isTracked(entity)) {
            entityManager.setType(entity.getUUID(), objectType);
        }

        return false;
    }

    /**
     * Processes a player attacking another entity. Internally this will determine
     * if the action is allowed, returning 'true' to indicate denial.
     *
     * @param player the player  cannot be null
     * @param entity the entity being attacked, cannot be null
     *
     * @return true for denial, false otherwise
     */
    // TODO: Unit test
    public boolean processEntityAttack(APlayer player, AEntity entity) {
        if (player == null || entity == null) throw new IllegalArgumentException();

        DevEngine.log("[WorldEngine:" + worldName + "] Processing player attack entity",
                "[WorldEngine:" + worldName + "] \t\tplayer = " + player,
                "[WorldEngine:" + worldName + "] \t\tentity = " + entity);

        List<Group> groups = Engine.getInstance().getGroupManager().getGroupsForPlayer(player, false);
        RejectionList reject = new DefaultRejectionList(RejectionList.ListType.ENTITY_ATTACK);
        ASGameMode playerGM = player.getGameMode();

        if (groups != null && groups.size() > 0) {
            ConsolidatedGroup consolidatedGroup = new ConsolidatedGroup(groups);

            reject = consolidatedGroup.getRejectionList(reject.getType());
            playerGM = consolidatedGroup.getActingMode(playerGM);
        }

        ASGameMode otherGM = ASUtils.toGamemode(entityManager.getType(entity.getUUID()));
        if (entity instanceof APlayer) {
            APlayer other = (APlayer) entity;
            otherGM = other.getGameMode();
        }

        // Inter-gamemode attacking
        if (otherGM != null && Engine.getInstance().getFlag(Engine.CONFIG_ENTITIES_CROSS_GAMEMODE_ATTACK, true)) {
            if (otherGM != playerGM && !player.hasPermission(APermission.FREE_ATTACK))
                return true;
        }

        if (playerGM != ASGameMode.CREATIVE) return false; // TODO: Possible implementation of 'affect'?

        // Check lists and permissions
        TrackedState playerReaction = entity.canAttack(player);
        if (playerReaction == TrackedState.NEGATED) return true; // Straight up deny
        if (reject.isBlocked(entity) && playerReaction == TrackedState.NOT_PRESENT)
            return true; // Rejected & no allow permission
        return false;
    }

    /**
     * Processes a player interacting with another entity. Internally this will determine
     * if the action is allowed, returning 'true' to indicate denial.
     *
     * @param player the player  cannot be null
     * @param entity the entity being itneracted with, cannot be null
     *
     * @return true for denial, false otherwise
     */
    // TODO: Unit test
    public boolean processEntityInteract(APlayer player, AEntity entity) {
        if (player == null || entity == null) throw new IllegalArgumentException();

        DevEngine.log("[WorldEngine:" + worldName + "] Processing player interact entity",
                "[WorldEngine:" + worldName + "] \t\tplayer = " + player,
                "[WorldEngine:" + worldName + "] \t\tentity = " + entity);

        List<Group> groups = Engine.getInstance().getGroupManager().getGroupsForPlayer(player, false);
        RejectionList reject = new DefaultRejectionList(RejectionList.ListType.ENTITY_INTERACT);
        ASGameMode playerGM = player.getGameMode();

        if (groups != null && groups.size() > 0) {
            ConsolidatedGroup consolidatedGroup = new ConsolidatedGroup(groups);

            reject = consolidatedGroup.getRejectionList(reject.getType());
            playerGM = consolidatedGroup.getActingMode(playerGM);
        }

        ASGameMode otherGM = ASUtils.toGamemode(entityManager.getType(entity.getUUID()));
        if (entity instanceof APlayer) {
            APlayer other = (APlayer) entity;
            otherGM = other.getGameMode();
        }

        // Inter-gamemode interaction
        if (otherGM != null && Engine.getInstance().getFlag(Engine.CONFIG_ENTITIES_CROSS_GAMEMODE_ATTACK, true)) {
            if (otherGM != playerGM && !player.hasPermission(APermission.FREE_TOUCH))
                return true;
        }

        if (playerGM != ASGameMode.CREATIVE) return false; // TODO: Possible implementation of 'affect'?

        // Check lists and permissions
        TrackedState playerReaction = entity.canInteract(player);
        if (playerReaction == TrackedState.NEGATED) return true; // Straight up deny
        if (reject.isBlocked(entity) && playerReaction == TrackedState.NOT_PRESENT)
            return true; // Rejected & no allow permission
        return false;
    }

    /**
     * Processes an entity breaking by a player. This will internally check
     * to see if the player is allowed to do so, and if not return 'true' to
     * represent denial.
     *
     * @param entity the entity being placed, cannot be null
     * @param player the player, cannot be null
     *
     * @returns true for denial, false otherwise
     */
    // TODO: Unit test
    public boolean processEntityBreak(APlayer player, AEntity entity){
        if (player == null || entity == null) throw new IllegalArgumentException();

        DevEngine.log("[WorldEngine:" + worldName + "] Processing entity break",
                "[WorldEngine:" + worldName + "] \t\tplayer = " + player,
                "[WorldEngine:" + worldName + "] \t\tentity = " + entity);

        List<Group> groups = Engine.getInstance().getGroupManager().getGroupsForPlayer(player, false);
        RejectionList reject = new DefaultRejectionList(RejectionList.ListType.ENTITY_BREAK);
        ASGameMode playerGM = player.getGameMode();

        if (groups != null && groups.size() > 0) {
            ConsolidatedGroup consolidatedGroup = new ConsolidatedGroup(groups);

            reject = consolidatedGroup.getRejectionList(reject.getType());
            playerGM = consolidatedGroup.getActingMode(playerGM);
        }

        ObjectType objectType = ASUtils.toBlockType(playerGM);

        // Check rejection lists
        if (playerGM == ASGameMode.CREATIVE) { // TODO: Possible implementation of 'affect'?
            TrackedState playerReaction = entity.canBreak(player); // See javadocs
            if (playerReaction == TrackedState.NEGATED) return true; // Straight up deny
            if (reject.isBlocked(entity) && playerReaction == TrackedState.NOT_PRESENT) { // No allow permission & is denied
                return true;
            }
        }

        ObjectType objectType1 = entityManager.getType(entity.getUUID());
        if (!player.hasPermission(APermission.FREE_BREAK)) {
            if (objectType1 != objectType && objectType1 != ObjectType.UNKNOWN)
                return true; // Mixed gamemode
        }

        entityManager.setType(entity.getUUID(), ObjectType.UNKNOWN);
        return false; // Entity was processed
    }
}
