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

package com.turt2live.antishare.bukkit.lists;

import com.turt2live.antishare.bukkit.impl.derived.DerivedBukkitObject;
import com.turt2live.antishare.bukkit.impl.derived.DerivedItemStack;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.engine.list.TrackedTypeList;
import com.turt2live.antishare.object.DerivableRejectable;
import com.turt2live.antishare.object.DerivedRejectable;
import com.turt2live.antishare.object.Rejectable;
import com.turt2live.antishare.object.RejectableCommand;
import com.turt2live.antishare.object.attribute.TrackedState;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic Bukkit rejection list
 *
 * @author turt2live
 */
public class BukkitList<T extends Rejectable> implements RejectionList<T>, TrackedTypeList<T> {

    protected List<DerivedRejectable> included = new ArrayList<>();
    protected List<DerivedRejectable> negated = new ArrayList<>();
    protected List<DerivedRejectable> includedGeneric = new ArrayList<>();
    protected List<DerivedRejectable> negatedGeneric = new ArrayList<>();
    private ListType type;

    /**
     * Creates a new Bukkit tracking list
     */
    public BukkitList() {
        this(null);
    }

    /**
     * Creates a new Bukkit rejection list
     *
     * @param type the list type, null is interpreted as CUSTOM
     */
    @SuppressWarnings("deprecation")
    public BukkitList(ListType type) {
        if (type == null) type = ListType.CUSTOM;

        this.type = type;
    }

    @Override
    public boolean isBlocked(T item) {
        return getState(item) == TrackedState.INCLUDED;
    }

    @Override
    public boolean isTracked(T object) {
        return getState(object) == TrackedState.INCLUDED;
    }

    @Override
    public TrackedState getState(T item) {
        if (item == null) throw new IllegalArgumentException();

        DerivedRejectable specific, generic;

        if (item instanceof DerivableRejectable) {
            specific = ((DerivableRejectable) item).getSpecific();
            generic = ((DerivableRejectable) item).getGeneric();
        } else {
            specific = new DerivedBukkitObject(item);
            generic = null;
        }

        TrackedState spec;
        TrackedState genr;

        boolean included = getState(this.included, specific, false);
        boolean negated = getState(this.negated, specific, false);

        if (included == negated) spec = TrackedState.NOT_PRESENT;
        else if (included) spec = TrackedState.INCLUDED;
        else spec = TrackedState.NEGATED;

        if (generic != null) {
            included = getState(this.includedGeneric, generic, true);
            negated = getState(this.negatedGeneric, generic, true);

            if (included == negated) genr = TrackedState.NOT_PRESENT;
            else if (included) genr = TrackedState.INCLUDED;
            else genr = TrackedState.NEGATED;

             /*
            Stage Three: Combination logic for merging stages one and two
            Logic:

            G = general scope
            S = specific scope

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

            if (spec == TrackedState.NOT_PRESENT) return genr; // [C1] In all cases, stageOne is favoured
            //if (stage2 == stage1) return stage1; // [C2] Doesn't matter
            return spec; // [RE] Remaining cases are all stage2 favoured
        }

        return spec;
    }

    private boolean getState(List<DerivedRejectable> list, DerivedRejectable item, boolean generic) {
        if (item instanceof DerivedItemStack) return getItemState(list, ((DerivedItemStack) item).getStack(), generic);
        if (item instanceof RejectableCommand) return getCommandState(list, (RejectableCommand) item, generic);

        return list.contains(item);
    }

    private boolean getCommandState(List<DerivedRejectable> list, RejectableCommand item, boolean generic) {
        byte flags = RejectableCommand.FLAG_STARTS_WITH | RejectableCommand.FLAG_EXACTLY;

        for (DerivedRejectable i : list) {
            if (i instanceof RejectableCommand) {
                if (item.matches((RejectableCommand) i, flags)) return true;
            }
        }

        return false;
    }

    private boolean getItemState(List<DerivedRejectable> list, ItemStack item, boolean generic) {
        for (DerivedRejectable i : list) {
            if (i instanceof DerivedItemStack) {
                ItemStack other = ((DerivedItemStack) i).getStack();

                if (generic && other.getType() == item.getType()) return true;
                else if (!generic && other.isSimilar(item)) return true;
            }
        }

        return false;
    }

    @Override
    public ListType getType() {
        return type;
    }
}
