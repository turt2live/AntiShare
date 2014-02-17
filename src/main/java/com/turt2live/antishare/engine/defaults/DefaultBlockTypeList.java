package com.turt2live.antishare.engine.defaults;

import com.turt2live.antishare.ASLocation;
import com.turt2live.antishare.engine.BlockTypeList;

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

}
