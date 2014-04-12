package com.turt2live.antishare.engine.defaults;

import com.turt2live.antishare.engine.BlockTypeList;
import com.turt2live.antishare.utils.ASLocation;

/**
 * Default block list
 *
 * @author turt2live
 */
// TODO: Unit test
public class DefaultBlockTypeList implements BlockTypeList {

    @Override
    public boolean isTracked(ASLocation location) {
        return false;
    }

}
