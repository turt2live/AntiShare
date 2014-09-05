/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.bukkit.groups;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.bukkit.lists.*;
import com.turt2live.antishare.bukkit.util.BukkitUtils;
import com.turt2live.antishare.configuration.Configuration;
import com.turt2live.antishare.configuration.groups.Group;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.engine.list.TrackedTypeList;
import com.turt2live.antishare.object.ABlock;
import com.turt2live.antishare.object.AEntity;
import com.turt2live.antishare.object.Rejectable;

import java.util.ArrayList;
import java.util.List;

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
    public TrackedTypeList<ABlock> getBlockTrackedList(ASGameMode gameMode) {
        return getBlockTrackedList(gameMode, super.configuration);
    }

    @Override
    public TrackedTypeList<AEntity> getEntityTrackedList(ASGameMode gameMode) {
        return getEntityTrackedList(gameMode, super.configuration);
    }

    @Override
    public <T extends Rejectable> RejectionList<T> getRejectionList(RejectionList.ListType type) {
        return getRejectionList(type, super.configuration);
    }

    static TrackedTypeList<ABlock> getBlockTrackedList(ASGameMode gameMode, Configuration configuration) {
        if (gameMode == null) throw new IllegalArgumentException("gamemode cannot be null");

        List<String> strings = configuration.getStringList("blocks." + gameMode.name().toLowerCase(), new ArrayList<String>());

        BukkitList<ABlock> blocks = new BukkitList<>();
        new PopulatorBlockList<ABlock>().populateList(blocks, strings);

        return blocks;
    }

    static TrackedTypeList<AEntity> getEntityTrackedList(ASGameMode gameMode, Configuration configuration) {
        if (gameMode == null) throw new IllegalArgumentException("gamemode cannot be null");

        List<String> strings = configuration.getStringList("entities." + gameMode.name().toLowerCase(), new ArrayList<String>());

        BukkitList<AEntity> entities = new BukkitList<>();
        new PopulatorEntityList<AEntity>().populateList(entities, strings);

        return entities;
    }

    static <T extends Rejectable> RejectionList<T> getRejectionList(RejectionList.ListType type, Configuration configuration) {
        if (type == null) throw new IllegalArgumentException("list type cannot be null");

        String configKey = BukkitUtils.getStringName(type);
        if (configKey == null)
            throw new NullPointerException("Yell at turt2live to add this: " + type.name());

        List<String> strings = configuration.getStringList("lists." + configKey, new ArrayList<String>());
        Populator<T> populator = null;

        // Determine derived type
        switch (type) {
            case ENTITY_BREAK:
            case ENTITY_INTERACT:
            case ENTITY_PLACE:
            case ENTITY_ATTACK:
                populator = new PopulatorEntityList<>();
                break;
            case BLOCK_PLACE:
            case BLOCK_BREAK:
            case INTERACTION:
                populator = new PopulatorBlockList<>();
                break;
            case ITEM_PICKUP:
            case ITEM_USE:
            case ITEM_DROP:
            case DEATH:
                populator = new PopulatorItemList<>();
                break;
            case MOB_CREATE:
                break;
            case COMMANDS:
                populator = new PopulatorCommandList<>();
                break;
            default:
                populator = null;
                break;
        }

        if (populator == null)
            throw new NullPointerException("List not implemented: " + type.name());

        BukkitList<T> list = new BukkitList<>(type);
        populator.populateList(list, strings);

        return list;
    }
}
