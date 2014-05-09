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

package com.turt2live.antishare.bukkit.lists;

import com.turt2live.antishare.bukkit.MaterialProvider;
import com.turt2live.antishare.bukkit.impl.BukkitBlock;
import com.turt2live.antishare.engine.list.BlockTypeList;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.object.ABlock;
import com.turt2live.antishare.object.attribute.TrackedState;
import org.bukkit.block.Block;

import java.util.List;

public class BukkitBlockList extends GenericBlockInformationList implements BlockTypeList, RejectionList<ABlock> {

    private ListType type = ListType.CUSTOM;

    public BukkitBlockList(MaterialProvider provider) {
        this(provider, null);
    }

    public BukkitBlockList(MaterialProvider provider, ListType type) {
        super(provider);
        if (type == null) type = ListType.CUSTOM;
        this.type = type;
    }

    @Override
    public boolean isTracked(ABlock block) {
        return getState(block) == TrackedState.INCLUDED;
    }

    @Override
    public boolean isBlocked(ABlock block) {
        return isTracked(block);
    }

    @Override
    public TrackedState getState(ABlock ablock) {
        if (ablock == null || !(ablock instanceof BukkitBlock)) return TrackedState.NOT_PRESENT;
        Block block = ((BukkitBlock) ablock).getBlock();
        BlockInformation highest = null;
        TrackedState state = TrackedState.NOT_PRESENT;

        List<BlockInformation> informations = generateBlockInfo(block);
        for (BlockInformation information : informations) {
            if (highest != null && state != TrackedState.NOT_PRESENT) {
                if (highest.isHigher(information)) continue; // Highest is still superior
            }
            TrackedState other = getState(information);
            highest = information;
            state = other;
        }

        return state;
    }

    @Override
    public ListType getType() {
        return type;
    }

}
