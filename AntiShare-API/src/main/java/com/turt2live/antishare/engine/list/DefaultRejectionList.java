package com.turt2live.antishare.engine.list;

import com.turt2live.antishare.object.attribute.TrackedState;

/**
 * Default implementation of a rejection list. This rejects nothing.
 *
 * @param <T> the type of rejection
 *
 * @author turt2live
 */
public class DefaultRejectionList<T extends Rejectable> implements RejectionList<T> {

    private ListType type;

    /**
     * Creates a new default rejection list
     *
     * @param type the type to use, null routes to {@link com.turt2live.antishare.engine.list.RejectionList.ListType#CUSTOM}
     */
    public DefaultRejectionList(ListType type) {
        this.type = type == null ? ListType.CUSTOM : type;
    }

    @Override
    public boolean isBlocked(T item) {
        return false;
    }

    @Override
    public TrackedState getState(T item) {
        return TrackedState.NOT_PRESENT;
    }

    @Override
    public ListType getType() {
        return type;
    }
}
