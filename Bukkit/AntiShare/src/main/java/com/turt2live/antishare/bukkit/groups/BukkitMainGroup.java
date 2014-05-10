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

import com.turt2live.antishare.configuration.Configuration;
import com.turt2live.antishare.configuration.groups.MainGroup;
import com.turt2live.antishare.engine.list.BlockTypeList;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.object.attribute.ASGameMode;

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
        return BukkitGroup.getTrackedList(gameMode, super.configuration);
    }

    @Override
    public RejectionList getRejectionList(RejectionList.ListType type) {
        return BukkitGroup.getRejectionList(type, super.configuration);
    }
}
