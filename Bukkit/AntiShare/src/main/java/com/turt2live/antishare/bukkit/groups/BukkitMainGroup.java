/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.bukkit.groups;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.configuration.Configuration;
import com.turt2live.antishare.configuration.groups.MainGroup;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.engine.list.TrackedTypeList;
import com.turt2live.antishare.object.ABlock;
import com.turt2live.antishare.object.AEntity;
import com.turt2live.antishare.object.Rejectable;

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
    public TrackedTypeList<ABlock> getBlockTrackedList(ASGameMode gameMode) {
        return BukkitGroup.getBlockTrackedList(gameMode, super.configuration);
    }

    @Override
    public TrackedTypeList<AEntity> getEntityTrackedList(ASGameMode gameMode) {
        return BukkitGroup.getEntityTrackedList(gameMode, super.configuration);
    }

    @Override
    public <T extends Rejectable> RejectionList<T> getRejectionList(RejectionList.ListType type) {
        return BukkitGroup.getRejectionList(type, super.configuration);
    }
}
