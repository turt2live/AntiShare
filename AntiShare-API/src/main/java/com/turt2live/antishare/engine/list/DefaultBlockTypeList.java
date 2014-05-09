package com.turt2live.antishare.engine.list;

import com.turt2live.antishare.object.ABlock;
import com.turt2live.antishare.object.attribute.TrackedState;

/**
 * Default block list
 *
 * @author turt2live
 */
public class DefaultBlockTypeList implements BlockTypeList {

    @Override
    public boolean isTracked(ABlock block) {
        return false;
    }

    @Override
    public TrackedState getState(ABlock block) {
        return TrackedState.NOT_PRESENT;
    }

}