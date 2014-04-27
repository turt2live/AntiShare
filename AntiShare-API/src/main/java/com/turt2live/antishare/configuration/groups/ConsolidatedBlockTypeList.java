package com.turt2live.antishare.configuration.groups;

import com.turt2live.antishare.ASLocation;
import com.turt2live.antishare.collections.ArrayArrayList;
import com.turt2live.antishare.engine.BlockTypeList;

import java.util.ArrayList;
import java.util.List;

/**
 * A block type list consisting of many block type lists. This uses a
 * voting-like system to determine what is tracked and what is not.
 * <p/>
 * Internally when {@link #getState(com.turt2live.antishare.ASLocation)}
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
// TODO: Unit test
public class ConsolidatedBlockTypeList implements BlockTypeList {

    private List<BlockTypeList> lists = new ArrayList<BlockTypeList>();

    /**
     * Creates a new consolidated block type list
     *
     * @param lists the lists to include. Cannot be null and must have at least one record
     */
    public ConsolidatedBlockTypeList(List<BlockTypeList> lists) {
        if (lists == null || lists.isEmpty()) throw new IllegalArgumentException("lists cannot be null or empty");
        this.lists.addAll(lists);
    }

    /**
     * Creates a new consolidated block type list
     *
     * @param lists the lists to include. Cannot be null and must have at least one record
     */
    public ConsolidatedBlockTypeList(BlockTypeList... lists) {
        this(new ArrayArrayList(lists));
    }

    @Override
    public TrackedState getState(ASLocation location) {
        if (location == null) throw new IllegalArgumentException("location cannot be null");

        int tracked = 0;
        int negated = 0;
        boolean included = false;

        for (BlockTypeList list : lists) {
            TrackedState state = list.getState(location);
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
    public boolean isTracked(ASLocation location) {
        return getState(location) == TrackedState.INCLUDED; // The one time it is okay to do this...
    }
}
