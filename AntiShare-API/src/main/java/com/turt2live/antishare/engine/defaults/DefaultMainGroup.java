package com.turt2live.antishare.engine.defaults;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.configuration.BreakSettings;
import com.turt2live.antishare.configuration.Configuration;
import com.turt2live.antishare.configuration.groups.MainGroup;
import com.turt2live.antishare.engine.BlockTypeList;

/**
 * Default main group
 *
 * @author turt2live
 */
// TODO: Unit test
public class DefaultMainGroup extends MainGroup {

    public DefaultMainGroup(Configuration configuration) {
        super(configuration);
    }

    @Override
    public BlockTypeList getTrackedList(ASGameMode gameMode) {
        return new DefaultBlockTypeList();
    }

    @Override
    public ASGameMode getActingMode(ASGameMode gameMode) {
        return gameMode;
    }

    @Override
    public BreakSettings getBreakSettings(ASGameMode gamemode, ASGameMode breaking) {
        return new BreakSettings(false, gamemode);
    }
}