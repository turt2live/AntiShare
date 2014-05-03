package com.turt2live.antishare.bukkit.groups;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.bukkit.BlockListGenerator;
import com.turt2live.antishare.bukkit.BukkitUtils;
import com.turt2live.antishare.bukkit.RejectionListGenerator;
import com.turt2live.antishare.configuration.Configuration;
import com.turt2live.antishare.configuration.groups.Group;
import com.turt2live.antishare.engine.BlockTypeList;
import com.turt2live.antishare.engine.RejectionList;

import java.util.ArrayList;

/**
 * Bukkit group
 *
 * @author turt2live
 */
public class BukkitGroup extends Group {

    /**
     * Creates a new group from the configuration and manager supplied
     *
     * @param configuration the configuration to be used, cannot be null
     */
    public BukkitGroup(Configuration configuration) {
        super(configuration);
    }

    @Override
    public BlockTypeList getTrackedList(ASGameMode gameMode) {
        if (gameMode == null) throw new IllegalArgumentException("gamemode cannot be null");
        return BlockListGenerator.fromList(super.configuration.getStringList("blocks." + gameMode.name().toLowerCase(), new ArrayList<String>()), "world");
    }

    @Override
    public RejectionList getRejectionList(RejectionList.ListType list) {
        if (list == null) throw new IllegalArgumentException("list type cannot be null");
        String configKey = BukkitUtils.getStringName(list);
        return RejectionListGenerator.fromList(super.configuration.getStringList("lists." + configKey, new ArrayList<String>()), "world", list);
    }
}
