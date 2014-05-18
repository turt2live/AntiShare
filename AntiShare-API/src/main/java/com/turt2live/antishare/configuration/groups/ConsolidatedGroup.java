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

package com.turt2live.antishare.configuration.groups;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.collections.ArrayArrayList;
import com.turt2live.antishare.configuration.BreakSettings;
import com.turt2live.antishare.engine.list.TrackedTypeList;
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
     * Gets the consolidated block tracking list for a specified game mode
     *
     * @param gameMode the gamemode to lookup, cannot be null
     *
     * @return the consolidated block tracking list
     */
    public ConsolidatedTrackedTypeList getTrackedList(ASGameMode gameMode) {
        if (gameMode == null) throw new IllegalArgumentException("arguments cannot be null");

        List<TrackedTypeList> lists = new ArrayList<TrackedTypeList>();
        for (Group group : groups) lists.add(group.getBlockTrackedList(gameMode));

        return new ConsolidatedTrackedTypeList(lists);
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
     *
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
     *
     * @return the applicable break settings
     */
    public BreakSettings getBreakSettings(ASGameMode gamemode, ASGameMode breaking) {
        if (gamemode == null || breaking == null) throw new IllegalArgumentException("arguments cannot be null");
        return groups.get(0).getBreakSettings(gamemode, breaking);
    }

}
