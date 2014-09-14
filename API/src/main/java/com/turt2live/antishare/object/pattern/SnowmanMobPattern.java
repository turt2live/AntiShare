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

package com.turt2live.antishare.object.pattern;

import com.turt2live.antishare.object.ABlock;
import com.turt2live.antishare.object.AEntity;
import com.turt2live.antishare.object.attribute.Facing;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the pattern required to build a snowman.
 *
 * @author turt2live
 */
// TODO: Unit test
public abstract class SnowmanMobPattern implements BlockPattern, MobPattern {

    @Override
    public boolean isCompleting(ABlock block) {
        return getInvolvedBlocks(block).size() == 3; // 1 head, 2 body
    }

    @Override
    public List<ABlock> getInvolvedBlocks(ABlock block) {
        if (block == null) throw new IllegalArgumentException();

        List<ABlock> applicable = new ArrayList<>();

        if (isPumpkin(block)) {
            ABlock down1 = block.getRelative(Facing.DOWN);
            ABlock down2 = down1.getRelative(Facing.DOWN);

            if (isSnow(down1) && isSnow(down2)) {
                applicable.add(block);
                applicable.add(down1);
                applicable.add(down2);
            }
        }

        return applicable;
    }

    @Override
    public AEntity.RelevantEntityType getEntityType() {
        return AEntity.RelevantEntityType.SNOWMAN;
    }

    protected abstract boolean isPumpkin(ABlock block);

    protected abstract boolean isSnow(ABlock block);
}
