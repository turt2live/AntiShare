package com.turt2live.antishare.engine.list;

import com.turt2live.antishare.TrackedState;

/**
 * A rejection list for items and/or blocks
 *
 * @param <T> the type of rejection
 * @author turt2live
 */
public interface RejectionList<T extends Rejectable> {

    /**
     * Various rejection lists used by the plugin
     */
    public static enum ListType {
        BLOCK_PLACE,
        BLOCK_BREAK,
        COMMANDS,

        /**
         * CUSTOM should only be used for out-of-plugin operations
         * as this is not used by the AntiShare engines.
         *
         * @deprecated Not for AntiShare Engine use
         */
        @Deprecated
        CUSTOM
    }

    /**
     * Determines if an item is allowed to be used.
     *
     * @param item the item to lookup, cannot be null
     * @return true if denied, false otherwise
     */
    public boolean isBlocked(T item);

    /**
     * Gets the tracking state of an item in this list
     *
     * @param item the item to lookup, cannot be null
     * @return the tracking state, never null
     */
    public TrackedState getState(T item);

    /**
     * Gets the list type of this list
     *
     * @return the list type
     */
    public ListType getType();

}
