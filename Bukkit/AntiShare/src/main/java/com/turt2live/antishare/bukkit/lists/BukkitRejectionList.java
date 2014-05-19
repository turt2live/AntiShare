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

import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.object.Rejectable;
import com.turt2live.antishare.object.attribute.TrackedState;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic Bukkit rejection list
 *
 * @author turt2live
 */
public class BukkitRejectionList<T extends Rejectable> implements RejectionList<T> {

    private List<T> included = new ArrayList<T>();
    private List<T> rejected = new ArrayList<T>();
    private ListType type;

    /**
     * Creates a new Bukkit rejection list
     *
     * @param type the list type, null is interpreted as CUSTOM
     */
    public BukkitRejectionList(ListType type) {
        if (type == null) type = ListType.CUSTOM;

        this.type = type;
    }

    public void setTrackedState(T item, TrackedState state) {
        if (item == null || state == null) throw new IllegalArgumentException();

        switch (state) {
            case INCLUDED:
                rejected.remove(item);
                included.add(item);
                break;
            case NEGATED:
                rejected.add(item);
                included.remove(item);
                break;
            case NOT_PRESENT:
                rejected.remove(item);
                included.remove(item);
                break;
        }
    }

    @Override
    public boolean isBlocked(T item) {
        return getState(item) == TrackedState.INCLUDED;
    }

    @Override
    public TrackedState getState(T item) {
        if (item == null) throw new IllegalArgumentException();

        boolean included = this.included.contains(item);
        boolean negated = this.rejected.contains(item);

        if (included == negated) return TrackedState.NOT_PRESENT;
        else if (included) return TrackedState.INCLUDED;
        else if (negated) return TrackedState.NEGATED;

        return TrackedState.NOT_PRESENT;
    }

    @Override
    public ListType getType() {
        return type;
    }
}
