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
public abstract class WitherMobPattern implements BlockPattern, MobPattern {

    protected enum WitherBlock {
        SOUL_SAND,
        SKULL
    }

    @Override
    public boolean isCompleting(ABlock block) {
        return getInvolvedBlocks(block).size() == 7; // 3 heads, 3 head mounts, and 1 foot
    }

    @Override
    public List<ABlock> getInvolvedBlocks(ABlock block) {
        if (block == null) throw new IllegalArgumentException();

        List<ABlock> applicable = new ArrayList<ABlock>();

        // Assuming the block is in a row of 3
        ABlock n = block.getRelative(Facing.NORTH);
        ABlock nn = n.getRelative(Facing.NORTH);
        ABlock s = block.getRelative(Facing.SOUTH);
        ABlock ss = block.getRelative(Facing.SOUTH);
        ABlock e = block.getRelative(Facing.EAST);
        ABlock ee = block.getRelative(Facing.EAST);
        ABlock w = block.getRelative(Facing.WEST);
        ABlock ww = block.getRelative(Facing.WEST);
        ABlock[] row = new ABlock[3];

        WitherBlock type = getType(block);
        if (type != WitherBlock.SKULL) return applicable;

        // Attempt to find the block in a row of 3
        if (getType(n) == type && getType(s) == type) {
            row[0] = n;
            row[1] = block;
            row[2] = s;
        } else if (getType(w) == type && getType(e) == type) {
            row[0] = e;
            row[1] = block;
            row[2] = w;
        } else if (getType(n) == type && getType(nn) == type) {
            row[0] = block;
            row[1] = n;
            row[2] = nn;
        } else if (getType(s) == type && getType(ss) == type) {
            row[0] = block;
            row[1] = s;
            row[2] = ss;
        } else if (getType(e) == type && getType(ee) == type) {
            row[0] = block;
            row[1] = e;
            row[2] = ee;
        } else if (getType(w) == type && getType(ww) == type) {
            row[0] = block;
            row[1] = w;
            row[2] = ww;
        } else {
            // No pattern match, not a row of three
            return applicable;
        }

        // If we're here, we are in a row of three
        ABlock[] otherRow = new ABlock[3];

        WitherBlock opposite = WitherBlock.SOUL_SAND;
        for (int i = 0; i < otherRow.length; i++) {
            otherRow[i] = row[i].getRelative(Facing.DOWN);
            if (getType(otherRow[i]) != opposite) return applicable; // No match
        }

        // Alright, we have 2 rows by now. Do we have a foot?
        ABlock foot = (otherRow[1]).getRelative(Facing.DOWN);
        if (getType(foot) == WitherBlock.SOUL_SAND) {
            for (int i = 0; i < otherRow.length * 2; i++) {
                applicable.add(i > 2 ? row[i - 3] : otherRow[i]);
            }
            applicable.add(foot);
        }

        return applicable;
    }

    @Override
    public AEntity.RelevantEntityType getEntityType() {
        return AEntity.RelevantEntityType.WITHER;
    }

    /**
     * Gets the type of the passed block
     *
     * @param block the block, never null
     *
     * @return the block type, or null if no matches
     */
    protected abstract WitherBlock getType(ABlock block);
}
