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

package com.turt2live.antishare.collections;

import java.util.Map;

/**
 * Represents a slotted collection. This type of collection is designed to
 * be used similar to an inventory system.
 *
 * @param <T> the collection type
 *
 * @author turt2live
 */
public interface SlottedCollection<T> {

    /**
     * Sets a slot to contain an object
     *
     * @param slot the slot
     * @param t    the object
     */
    public void set(int slot, T t);

    /**
     * Gets the object contained in a slot
     *
     * @param slot the slot to lookup
     *
     * @return the object, or null if not found
     */
    public T get(int slot);

    /**
     * Gets a mapping of all slots and objects
     *
     * @return the mapping, never null but may be empty
     */
    public Map<Integer, T> getAll();

    /**
     * Clears the collection
     */
    public void clear();

}
