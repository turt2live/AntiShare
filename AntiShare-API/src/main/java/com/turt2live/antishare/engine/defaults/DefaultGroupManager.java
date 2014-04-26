package com.turt2live.antishare.engine.defaults;

import com.turt2live.antishare.configuration.BreakSettings;
import com.turt2live.antishare.configuration.Configuration;
import com.turt2live.antishare.configuration.groups.GroupManager;
import com.turt2live.antishare.configuration.groups.MainGroup;
import com.turt2live.antishare.engine.BlockTypeList;
import com.turt2live.antishare.utils.ASGameMode;

/**
 * The default group manager
 *
 * @author turt2live
 */
// TODO: Unit test
public class DefaultGroupManager extends GroupManager {

    @Override
    public void loadAll() {
        super.mainGroup = new DefaultMainGroup(null, null);
    }
}
