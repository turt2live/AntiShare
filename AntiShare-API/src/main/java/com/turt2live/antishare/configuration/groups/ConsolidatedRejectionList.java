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
import com.turt2live.antishare.object.Rejectable;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.object.attribute.TrackedState;

import java.util.ArrayList;
import java.util.List;

/**
 * A block type list consisting of many rejection lists. This uses a
 * voting-like system to determine what is tracked and what is not.
 * <p/>
 * It should be noted that this assumes all lists are of the same type,
 * therefore a semi-random chosen list is used for {@link #getType()}
 * where only the value of one list is considered for the return value.
 * <p/>
 * Internally when any getState() method is called a poll of all lists
 * is activated to determine how many lists determine a location to be
 * tracked and how many lists determine a list to be not tracked. An
 * additional flag for "is tracked" is kept to ensure the correct return
 * value is sent. If the flag is true and the "tracked" versus "negated"
 * ratio is in favour of the tracked blocks then the return state is
 * TRACKED. In the same scenario with the ratio in favour of negated,
 * NEGATED is returned. In the event that the location is not tracked
 * or that the negated and tracked counts are equal, NOT_PRESENT is
 * returned.
 *
 * @author turt2live
 */
// TODO: Unit test
public class ConsolidatedRejectionList<T extends Rejectable> implements RejectionList<T> {

    private List<RejectionList> lists = new ArrayList<RejectionList>();

    /**
     * Creates a new consolidated rejection list
     *
     * @param lists the lists to include. Cannot be null and must have at least one record
     */
    public ConsolidatedRejectionList(List<RejectionList<T>> lists) {
        if (lists == null || lists.isEmpty()) throw new IllegalArgumentException("lists cannot be null or empty");
        this.lists.addAll(lists);
    }

    /**
     * Creates a new consolidated rejection list
     *
     * @param lists the lists to include. Cannot be null and must have at least one record
     */
    public ConsolidatedRejectionList(RejectionList... lists) {
        this(new ArrayArrayList(lists));
    }

    @Override
    public boolean isBlocked(T item) {
        return getState(item) == TrackedState.INCLUDED; // Tee hee
    }

    @Override
    public TrackedState getState(T item) {
        if (item == null) throw new IllegalArgumentException("location cannot be null");

        int tracked = 0;
        int negated = 0;
        boolean included = false;

        for (RejectionList list : lists) {
            TrackedState state = list.getState(item);
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
    public ListType getType() {
        return lists.get(0).getType();
    }
}
