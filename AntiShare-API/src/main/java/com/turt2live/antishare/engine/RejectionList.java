package com.turt2live.antishare.engine;

import com.turt2live.antishare.ABlock;
import com.turt2live.antishare.TrackedState;

/**
 * A rejection list for items and/or blocks
 *
 * @author turt2live
 */
public interface RejectionList {

    /**
     * Various rejection lists used by the plugin
     */
    public static enum ListType {
        BLOCK_PLACE,

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
     * Determines if a block is allowed to be used.
     *
     * @param block the block, cannot be null
     * @return true if denied, false otherwise
     */
    public boolean isBlocked(ABlock block);

    /**
     * Gets the tracking state of a block in this list
     *
     * @param block the block, cannot be null
     * @return the tracking state, never null
     */
    public TrackedState getState(ABlock block);

    /**
     * Gets the list type of this list
     *
     * @return the list type
     */
    public ListType getType();

    // TODO: Items

}
