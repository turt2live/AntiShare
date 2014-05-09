package com.turt2live.antishare.configuration.groups;

import com.turt2live.antishare.ABlock;
import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.collections.ArrayArrayList;
import com.turt2live.antishare.configuration.BreakSettings;
import com.turt2live.antishare.engine.list.BlockTypeList;
import com.turt2live.antishare.engine.list.RejectionList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a ruleset for a collection of groups. The ruleset applied
 * to the operation of this consolidation is that a "merge" is applied
 * between all applicable groups where possible. If no operations can
 * be merged for an action, the top level (index 0) is used as a representation
 * of the group.
 *
 * @author turt2live
 */
// TODO: Unit test
public class ConsolidatedGroup {

    private List<Group> groups;

    /**
     * Creates a new consolidated group
     *
     * @param groups the groups to use, cannot be null and must have at least one entry
     */
    public ConsolidatedGroup(List<Group> groups) {
        if (groups == null || groups.isEmpty()) throw new IllegalArgumentException("groups cannot be null or empty");
        this.groups = Collections.unmodifiableList(groups);
    }

    /**
     * Creates a new consolidated group
     *
     * @param groups the groups to use, cannot be null and must have at least one entry
     */
    public ConsolidatedGroup(Group... groups) {
        this(new ArrayArrayList<Group>(groups));
    }

    /**
     * Determines if a specified location is tracked under a GameMode
     *
     * @param gameMode the gamemode to lookup, cannot be null
     * @param block    the block to lookup, cannot be null
     * @return true if tracked, false otherwise
     */
    public boolean isTracked(ASGameMode gameMode, ABlock block) {
        if (gameMode == null || block == null) throw new IllegalArgumentException("arguments cannot be null");

        return getTrackedList(gameMode).isTracked(block);
    }

    /**
     * Gets the consolidated block tracking list for a specified game mode
     *
     * @param gameMode the gamemode to lookup, cannot be null
     * @return the consolidated block tracking list
     */
    public ConsolidatedBlockTypeList getTrackedList(ASGameMode gameMode) {
        if (gameMode == null) throw new IllegalArgumentException("arguments cannot be null");

        List<BlockTypeList> lists = new ArrayList<BlockTypeList>();
        for (Group group : groups) lists.add(group.getTrackedList(gameMode));

        return new ConsolidatedBlockTypeList(lists);
    }

    public ConsolidatedRejectionList getRejectionList(RejectionList.ListType type) {
        if (type == null) throw new IllegalArgumentException();

        List<RejectionList> lists = new ArrayList<RejectionList>();
        for (Group group : groups) {
            RejectionList list = group.getRejectionList(type);
            if (list != null) lists.add(list);
        }

        return new ConsolidatedRejectionList(lists);
    }

    /**
     * Gets the acting gamemode for the top level (index 0) group
     *
     * @param gameMode the gamemode to lookup
     * @return the acting gamemode
     */
    public ASGameMode getActingMode(ASGameMode gameMode) {
        return groups.get(0).getActingMode(gameMode);
    }

    /**
     * Gets the break settings from the top level (index 0) group
     * for a specified gamemode breaking a specified gamemode's block.
     *
     * @param gamemode the gamemode breaking the block, cannot be null
     * @param breaking the gamemode of the block, cannot be null
     * @return the applicable break settings
     */
    public BreakSettings getBreakSettings(ASGameMode gamemode, ASGameMode breaking) {
        if (gamemode == null || breaking == null) throw new IllegalArgumentException("arguments cannot be null");
        return groups.get(0).getBreakSettings(gamemode, breaking);
    }

}
