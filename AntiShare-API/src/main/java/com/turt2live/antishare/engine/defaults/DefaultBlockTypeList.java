package com.turt2live.antishare.engine.defaults;

import com.turt2live.antishare.engine.BlockTypeList;
import com.turt2live.antishare.utils.ASLocation;

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
