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

import com.turt2live.antishare.collections.ArrayArrayList;
import com.turt2live.antishare.object.ABlock;

/**
 * Represents the manager of all patterns.
 *
 * @author turt2live
 */
// TODO: Unit test
public class PatternManager extends ArrayArrayList<BlockPattern> {

    /**
     * Finds a pattern which is completed by this block
     *
     * @param block the block, cannot be null
     *
     * @return the pattern which can be completed, null if none
     */
    public BlockPattern findPattern(ABlock block) {
        return findPattern(block, BlockPattern.class);
    }

    /**
     * Finds a pattern which is completed by this block
     *
     * @param block the block, cannot be null
     * @param type  the type of mob pattern to lookup, cannot be null
     *
     * @return the pattern which can be completed, null if none
     */
    public BlockPattern findPattern(ABlock block, Class<? extends BlockPattern> type) {
        if (block == null || type == null) throw new IllegalArgumentException();

        for (BlockPattern pattern : this) {
            if (type.isInstance(pattern) && pattern.isCompleting(block)) return pattern;
        }

        return null;
    }

}
