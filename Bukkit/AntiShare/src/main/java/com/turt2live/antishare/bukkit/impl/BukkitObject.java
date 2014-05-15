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

package com.turt2live.antishare.bukkit.impl;

import com.turt2live.antishare.APermission;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.object.APlayer;
import com.turt2live.antishare.object.attribute.TrackedState;

/**
 * A common set of methods for Bukkit objects
 *
 * @author turt2live
 */
public abstract class BukkitObject {

    protected abstract String getFriendlyName();

    protected TrackedState permissionCheck(RejectionList.ListType type, APlayer player) {
        // Stage One: Check general permissions
        boolean allow = player.hasPermission(APermission.getPermissionNode(true, type));
        boolean deny = player.hasPermission(APermission.getPermissionNode(false, type));
        TrackedState stageOne = TrackedState.NOT_PRESENT;

        if (allow == deny) stageOne = TrackedState.NOT_PRESENT;
        else if (allow) stageOne = TrackedState.INCLUDED;
        else if (deny) stageOne = TrackedState.NEGATED;

        // Stage Two: Check specific permissions
        allow = player.hasPermission(APermission.getPermissionNode(true, type) + "." + getFriendlyName());
        deny = player.hasPermission(APermission.getPermissionNode(false, type) + "." + getFriendlyName());
        TrackedState stageTwo = TrackedState.NOT_PRESENT;

        if (allow == deny) stageTwo = TrackedState.NOT_PRESENT;
        else if (allow) stageTwo = TrackedState.INCLUDED;
        else if (deny) stageTwo = TrackedState.NEGATED;

        /*
        Stage Three: Combination logic for merging stages one and two
        Logic:

        G = stageOne, general scope
        S = stageTwo, specific scope

        if(G[allow] && S[allow])    [allow]  // Favour: G || S      [C2] <-- Covered by return, doesn't matter
        if(G[allow] && S[deny])     [deny]   // Favour: S           [RE]
        if(G[allow] && S[none])     [allow]  // Favour: G           [C1]

        if(G[deny] && S[allow])     [allow]  // Favour: S           [RE]
        if(G[deny] && S[deny])      [deny]   // Favour: G || S      [C2] <-- Covered by return, doesn't matter
        if(G[deny] && S[none])      [deny]   // Favour: G           [C1]

        if(G[none] && S[allow])     [allow]  // Favour: S           [RE]
        if(G[none] && S[deny])      [deny]   // Favour: S           [RE]
        if(G[none] && S[none])      [none]   // Favour: G || S      [C2] <-- Covered by return, doesn't matter
         */

        if (stageTwo == TrackedState.NOT_PRESENT) return stageOne; // [C1] In all cases, stageOne is favoured
        //if (stageTwo == stageOne) return stageOne; // [C2] Doesn't matter
        return stageTwo; // [RE] Remaining cases are all stageTwo favoured
    }
}