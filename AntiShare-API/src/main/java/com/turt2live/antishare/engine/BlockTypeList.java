package com.turt2live.antishare.engine;

import com.turt2live.antishare.ASLocation;

/**
 * A block type list, used for tracking
 *
 * @author turt2live
 */
public interface BlockTypeList {

    public enum TrackedState {
        /**
         * Indicates the record was negated from the list
         */
        NEGATED,
        /**
         * Indicates the record is 'tracked'
         */
        INCLUDED,
        /**
         * Indicates the record is not present in a list
         */
        NOT_PRESENT;
    }

    /**
     * Determines if a specified block at a specified location is tracked
     *
     * @param location the location to lookup
     * @return true if this block should be tracked, false otherwise
     */
    public boolean isTracked(ASLocation location);

    /**
     * Gets the tracking state of a specified block at a location. This is generally
     * used by plugin operations for determining what data this list has rather
     * than being used to indicate whether or not a location should be tracked. A
     * better alternative for determining tracking status would be {@link #isTracked(com.turt2live.antishare.ASLocation)}.
     *
     * @param location the location to lookup
     * @return the tracking state
     */
    public TrackedState getState(ASLocation location);

}
