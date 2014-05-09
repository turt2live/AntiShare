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

package com.turt2live.antishare.engine.list;

import com.turt2live.antishare.object.ABlock;
import com.turt2live.antishare.object.attribute.TrackedState;

/**
 * A block type list, used for tracking
 *
 * @author turt2live
 */
public interface BlockTypeList {

    /**
     * Determines if a specified block is tracked
     *
     * @param block the block to lookup
     *
     * @return true if this block should be tracked, false otherwise
     */
    public boolean isTracked(ABlock block);

    /**
     * Gets the tracking state of a specified block. This is generally
     * used by plugin operations for determining what data this list has rather
     * than being used to indicate whether or not a block should be tracked. A
     * better alternative for determining tracking status would be {@link #isTracked(com.turt2live.antishare.object.ABlock)}.
     *
     * @param block the block to lookup
     *
     * @return the tracking state
     */
    public TrackedState getState(ABlock block);

}
