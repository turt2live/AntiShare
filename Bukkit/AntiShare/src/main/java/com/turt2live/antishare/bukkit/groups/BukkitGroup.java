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

import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.BukkitUtils;
import com.turt2live.antishare.bukkit.MaterialProvider;
import com.turt2live.antishare.bukkit.lists.BukkitBlockList;
import com.turt2live.antishare.configuration.Configuration;
import com.turt2live.antishare.configuration.groups.Group;
import com.turt2live.antishare.engine.list.BlockTypeList;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.object.attribute.ASGameMode;

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
        MaterialProvider provider = AntiShare.getInstance().getMaterialProvider();
        BukkitBlockList list = new BukkitBlockList(provider);
        list.populateBlocks(super.configuration.getStringList("blocks." + gameMode.name().toLowerCase(), new ArrayList<String>()));
        return list;
    }

    @Override
    public RejectionList getRejectionList(RejectionList.ListType type) {
        if (type == null) throw new IllegalArgumentException("list type cannot be null");
        String configKey = BukkitUtils.getStringName(type);
        if (configKey == null)
            throw new NullPointerException("Yell at turt2live to add this: " + type.name());
        MaterialProvider provider = AntiShare.getInstance().getMaterialProvider();
        BukkitBlockList list = new BukkitBlockList(provider, type);
        list.populateBlocks(super.configuration.getStringList("lists." + configKey, new ArrayList<String>()));
        return list;
    }
}
