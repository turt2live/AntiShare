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

import java.util.List;

/**
 * Represents a block pattern which may be used to ensure
 * the existence of a set form of blocks.
 *
 * @author turt2live
 */
public interface BlockPattern {

    /**
     * Determines if a specified block will complete the pattern
     *
     * @param block the block to determine the completion of the pattern, cannot be null
     *
     * @return true if the passed block would complete this pattern, false otherwise
     */
    public boolean isCompleting(ABlock block);

    /**
     * Gets the blocks involved in this pattern. This is considered unreliable
     * if there is no completed pattern and may return a null or empty list. If
     * the pattern is completed, this will return a full list of applicable blocks.
     *
     * @param block the block to be added to the pattern, cannot be null
     *
     * @return the list of applicable blocks (if completed), otherwise unknown return value
     */
    public List<ABlock> getInvolvedBlocks(ABlock block);

}
