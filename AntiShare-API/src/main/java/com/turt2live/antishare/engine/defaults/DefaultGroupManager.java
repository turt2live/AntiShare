package com.turt2live.antishare.engine.defaults;

import com.turt2live.antishare.configuration.groups.GroupManager;

/**
 * The default group manager
 *
 * @author turt2live
 */
// TODO: Unit test
public class DefaultGroupManager extends GroupManager {

    @Override
    public void loadAll() {
        super.mainGroup = new DefaultMainGroup(null);
    }
}
