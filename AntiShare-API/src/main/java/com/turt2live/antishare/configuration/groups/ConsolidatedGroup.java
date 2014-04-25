package com.turt2live.antishare.configuration.groups;

import com.turt2live.antishare.collections.ArrayArrayList;
import com.turt2live.antishare.engine.BlockTypeList;
import com.turt2live.antishare.utils.ASGameMode;

import java.util.Collections;
import java.util.List;

/**
 * Represents a ruleset for a collection of groups. The first gamemode
 * in the internal list is considered the "top level" group. Groups
 * in the list are considered to be "in order" for level operations
 * such as "low level" and "top level".
 *
 * @author turt2live
 */
public class ConsolidatedGroup {

    private List<Group> groups;

    public ConsolidatedGroup(List<Group> groups) {
        if (groups == null || groups.isEmpty()) throw new IllegalArgumentException("groups cannot be null or empty");
        this.groups = Collections.unmodifiableList(groups);
    }

    public ConsolidatedGroup(Group... groups) {
        this(new ArrayArrayList<Group>(groups));
    }

    public BlockTypeList getBlockList(ASGameMode gameMode) {
        // TODO: Bottom->Top
        return null;
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

}
