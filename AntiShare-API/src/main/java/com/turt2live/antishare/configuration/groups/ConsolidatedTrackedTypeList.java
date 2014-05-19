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

import com.turt2live.antishare.collections.ArrayArrayList;
import com.turt2live.antishare.engine.list.TrackedTypeList;
import com.turt2live.antishare.object.Rejectable;
import com.turt2live.antishare.object.attribute.TrackedState;

import java.util.ArrayList;
import java.util.List;

/**
 * A block type list consisting of many block type lists. This uses a
 * voting-like system to determine what is tracked and what is not.
 * <p/>
 * Internally when {@link #getState(com.turt2live.antishare.object.Rejectable)}
 * is called a poll of all lists is activated to determine how many lists
 * determine a location to be tracked and how many lists determine a list
 * to be not tracked. An additional flag for "is tracked" is kept to ensure
 * the correct return value is sent. If the flag is true and the "tracked"
 * versus "negated" ratio is in favour of the tracked blocks then the return
 * state is TRACKED. In the same scenario with the ratio in favour of negated,
 * NEGATED is returned. In the event that the location is not tracked or that
 * the negated and tracked counts are equal, NOT_PRESENT is returned.
 *
 * @author turt2live
 */
public class ConsolidatedTrackedTypeList<T extends Rejectable> implements TrackedTypeList<T> {

    private List<TrackedTypeList<T>> lists = new ArrayList<TrackedTypeList<T>>();

    /**
     * Creates a new consolidated block type list
     *
     * @param lists the lists to include. Cannot be null and must have at least one record
     */
    public ConsolidatedTrackedTypeList(List<TrackedTypeList<T>> lists) {
        if (lists == null || lists.isEmpty()) throw new IllegalArgumentException("lists cannot be null or empty");
        this.lists.addAll(lists);
    }

    /**
     * Creates a new consolidated object type list
     *
     * @param lists the lists to include. Cannot be null and must have at least one record
     */
    public ConsolidatedTrackedTypeList(TrackedTypeList<T>... lists) {
        this(new ArrayArrayList<TrackedTypeList<T>>(lists));
    }

    @Override
    public TrackedState getState(T object) {
        if (object == null) throw new IllegalArgumentException("object cannot be null");

        int tracked = 0;
        int negated = 0;
        boolean included = false;

        for (TrackedTypeList<T> list : lists) {
            TrackedState state = list.getState(object);
            switch (state) {
                case INCLUDED:
                    tracked++;
                    included = true;
                    break;
                case NEGATED:
                    negated++;
                    included = true;
                    break;
                default:
                    break;
            }
        }

        return included && tracked != negated ? (tracked > negated ? TrackedState.INCLUDED : TrackedState.NEGATED) : TrackedState.NOT_PRESENT;
    }

    @Override
    public boolean isTracked(T object) {
        return getState(object) == TrackedState.INCLUDED; // The one time it is okay to do this...
    }
}
