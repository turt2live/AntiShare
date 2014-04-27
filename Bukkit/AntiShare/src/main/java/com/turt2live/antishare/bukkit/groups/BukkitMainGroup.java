package com.turt2live.antishare.bukkit.groups;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.bukkit.BlockListGenerator;
import com.turt2live.antishare.configuration.Configuration;
import com.turt2live.antishare.configuration.groups.MainGroup;
import com.turt2live.antishare.engine.BlockTypeList;

import java.util.ArrayList;

/**
 * Main group
 *
 * @author turt2live
 */
public class BukkitMainGroup extends MainGroup {

    /**
     * Creates a new main group from the configuration and manager supplied
     *
     * @param configuration the configuration to be used, cannot be null
     */
    public BukkitMainGroup(Configuration configuration) {
        super(configuration);
    }

    @Override
    public BlockTypeList getTrackedList(ASGameMode gameMode) {
        if (gameMode == null) throw new IllegalArgumentException("gamemode cannot be null");
        return BlockListGenerator.fromList(super.configuration.getStringList("blocks." + gameMode.name().toLowerCase(), new ArrayList<String>()), "world");
    }
}
