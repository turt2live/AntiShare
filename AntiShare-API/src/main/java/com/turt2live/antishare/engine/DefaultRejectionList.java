package com.turt2live.antishare.engine;

import com.turt2live.antishare.ABlock;
import com.turt2live.antishare.TrackedState;

/**
 * Default implementation of a rejection list. This rejects nothing.
 *
 * @author turt2live
 */
public class DefaultRejectionList implements RejectionList {

    private ListType type;

    /**
     * Creates a new default rejection list
     *
     * @param type the type to use, null routes to {@link com.turt2live.antishare.engine.RejectionList.ListType#CUSTOM}
     */
    public DefaultRejectionList(ListType type) {
        this.type = type == null ? ListType.CUSTOM : type;
    }

    @Override
    public boolean isBlocked(ABlock block) {
        return false;
    }

    @Override
    public TrackedState getState(ABlock block) {
        return TrackedState.NOT_PRESENT;
    }

    @Override
    public ListType getType() {
        return type;
    }
}
