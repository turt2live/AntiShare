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
import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.MaterialProvider;
import com.turt2live.antishare.bukkit.lists.BukkitBlockTrackedList;
import com.turt2live.antishare.bukkit.lists.BukkitEntityTrackedList;
import com.turt2live.antishare.bukkit.lists.BukkitItemList;
import com.turt2live.antishare.bukkit.util.BukkitUtils;
import com.turt2live.antishare.configuration.Configuration;
import com.turt2live.antishare.configuration.groups.Group;
import com.turt2live.antishare.engine.list.CommandRejectionList;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.engine.list.TrackedTypeList;
import com.turt2live.antishare.object.ABlock;
import com.turt2live.antishare.object.AEntity;
import com.turt2live.antishare.object.RejectableCommand;

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
    public RejectionList getRejectionList(RejectionList.ListType type) {
        return getRejectionList(type, super.configuration);
    }

    static TrackedTypeList getBlockTrackedList(ASGameMode gameMode, Configuration configuration) {
        if (gameMode == null) throw new IllegalArgumentException("gamemode cannot be null");

        MaterialProvider provider = AntiShare.getInstance().getMaterialProvider();
        BukkitBlockTrackedList list = new BukkitBlockTrackedList(provider);
        list.populateBlocks(configuration.getStringList("blocks." + gameMode.name().toLowerCase(), new ArrayList<String>()));

        return list;
    }

    static TrackedTypeList getEntityTrackedList(ASGameMode gameMode, Configuration configuration) {
        if (gameMode == null) throw new IllegalArgumentException("gamemode cannot be null");

        List<String> strings = configuration.getStringList("entities." + gameMode.name().toLowerCase(), new ArrayList<String>());

        return new BukkitEntityTrackedList(strings);
    }

    static RejectionList getRejectionList(RejectionList.ListType type, Configuration configuration) {
        if (type == null) throw new IllegalArgumentException("list type cannot be null");

        String configKey = BukkitUtils.getStringName(type);
        if (configKey == null)
            throw new NullPointerException("Yell at turt2live to add this: " + type.name());

        RejectionList list;
        switch (type) {
            case BLOCK_BREAK:
            case BLOCK_PLACE:
            case INTERACTION:
                MaterialProvider provider = AntiShare.getInstance().getMaterialProvider();
                list = new BukkitBlockTrackedList(provider, type);
                ((BukkitBlockTrackedList) list).populateBlocks(configuration.getStringList("lists." + configKey, new ArrayList<String>()));
                break;
            case COMMANDS:
                list = new CommandRejectionList();
                List<String> commands = configuration.getStringList("lists." + configKey, new ArrayList<String>());
                List<RejectableCommand> rejectableCommands = new ArrayList<RejectableCommand>();
                for (String command : commands) {
                    rejectableCommands.add(new RejectableCommand(command));
                }
                ((CommandRejectionList) list).populate(rejectableCommands);
                break;
            case ITEM_USE:
            case ITEM_DROP:
            case ITEM_PICKUP:
                list = new BukkitItemList(type);
                List<String> items = configuration.getStringList("lists." + configKey, new ArrayList<String>());
                ((BukkitItemList) list).load(items);
                break;
            default:
                list = null;
        }

        if (list == null)
            throw new NullPointerException("List not implemented: " + type.name());

        return list;
    }
}
