package com.turt2live.antishare.engine;

import com.turt2live.antishare.utils.ASLocation;

/**
 * A block type list, used for tracking
 *
 * @author turt2live
 */
// TODO: Unit test
public interface BlockTypeList {

    /**
     * Determines if a specified block at a specified location is tracked
     *
     * @param location the location to lookup
     * @return true if this block should be tracked, false otherwise
     */
    public boolean isTracked(ASLocation location);

}
