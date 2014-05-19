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

package com.turt2live.antishare.object.pattern;

import com.turt2live.antishare.object.ABlock;
import com.turt2live.antishare.object.AEntity;
import com.turt2live.antishare.object.attribute.Facing;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the pattern required to create a wither boss.
 *
 * @author turt2live
 */
// TODO: Unit test
public abstract class IronGolemMobPattern implements BlockPattern, MobPattern{

    protected enum IronGolemBlock {
        PUMPKIN,
        IRON
    }

    @Override
    public boolean isCompleting(ABlock block) {
        return getInvolvedBlocks(block).size() == 5; // 1 head, 4 body
    }

    @Override
    public List<ABlock> getInvolvedBlocks(ABlock block) {
        if (block == null) throw new IllegalArgumentException();

        List<ABlock> applicable = new ArrayList<ABlock>();

        if (getType(block) == IronGolemBlock.PUMPKIN) {
            ABlock down = block.getRelative(Facing.DOWN);

            ABlock n = down.getRelative(Facing.NORTH);
            ABlock s = down.getRelative(Facing.SOUTH);
            ABlock e = down.getRelative(Facing.EAST);
            ABlock w = down.getRelative(Facing.WEST);

            ABlock one = null, two = null;
            if (getType(n) == IronGolemBlock.IRON && getType(s) == IronGolemBlock.IRON) {
                one = n;
                two = s;
            } else if (getType(e) == IronGolemBlock.IRON && getType(w) == IronGolemBlock.IRON) {
                one = e;
                two = w;
            }

            if (one != null && two != null) {
                ABlock foot = down.getRelative(Facing.DOWN);

                if (getType(foot) == IronGolemBlock.IRON) {
                    applicable.add(block);
                    applicable.add(down);
                    applicable.add(one);
                    applicable.add(two);
                    applicable.add(foot);
                }
            }
        }

        return applicable;
    }

    @Override
    public AEntity.RelevantEntityType getEntityType() {
        return AEntity.RelevantEntityType.IRON_GOLEM;
    }

    /**
     * Gets the type of the passed block
     *
     * @param block the block, never null
     *
     * @return the block type, or null if no matches
     */
    protected abstract IronGolemBlock getType(ABlock block);
}
