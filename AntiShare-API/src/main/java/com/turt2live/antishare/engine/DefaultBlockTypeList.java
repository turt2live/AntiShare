package com.turt2live.antishare.engine;

import com.turt2live.antishare.ASLocation;

/**
 * Default block list
 *
 * @author turt2live
 */
public class DefaultBlockTypeList implements BlockTypeList {

    @Override
    public boolean isTracked(ASLocation location) {
        return false;
    }

    @Override
    // TODO: Unit test
    public TrackedState getState(ASLocation location) {
        return TrackedState.NOT_PRESENT;
    }

}