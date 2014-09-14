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

package com.turt2live.antishare.engine.list;

import com.turt2live.antishare.object.Rejectable;
import com.turt2live.antishare.object.attribute.TrackedState;

/**
 * A rejection list for items and/or blocks
 *
 * @param <T> the type of rejection
 *
 * @author turt2live
 */
public interface RejectionList<T extends Rejectable> {

    /**
     * Various rejection lists used by the plugin
     */
    public static enum ListType {
        BLOCK_PLACE,
        BLOCK_BREAK,
        COMMANDS,
        INTERACTION,
        ITEM_USE,
        ITEM_DROP,
        ITEM_PICKUP,
        ENTITY_INTERACT,
        ENTITY_ATTACK,
        ENTITY_PLACE,
        ENTITY_BREAK,
        DEATH,
        MOB_CREATE,

        /**
         * CUSTOM should only be used for out-of-plugin operations
         * as this is not used by the AntiShare engines.
         *
         * @deprecated Not for AntiShare Engine use
         */
        @Deprecated
        CUSTOM
    }

    /**
     * Determines if an item is allowed to be used.
     *
     * @param item the item to lookup, cannot be null
     *
     * @return true if denied, false otherwise
     */
    public boolean isBlocked(T item);

    /**
     * Gets the tracking state of an item in this list
     *
     * @param item the item to lookup, cannot be null
     *
     * @return the tracking state, never null
     */
    public TrackedState getState(T item);

    /**
     * Gets the list type of this list
     *
     * @return the list type
     */
    public ListType getType();

}
