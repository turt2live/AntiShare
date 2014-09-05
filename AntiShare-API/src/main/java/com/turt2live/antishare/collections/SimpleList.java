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

package com.turt2live.antishare.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a simple list for objects
 *
 * @param <T> the simple list type
 *
 * @author turt2live
 */
public class SimpleList<T> {

    private List<T> listing = new ArrayList<>();

    /**
     * Adds an item to the simple list
     *
     * @param t the item to add
     */
    public void addItem(T t) {
        if (t == null) throw new IllegalArgumentException("item cannot be null");

        listing.add(t);
    }

    /**
     * Removes an item from the simple list
     *
     * @param t the item to remove
     */
    public void removeItem(T t) {
        if (t == null) throw new IllegalArgumentException("item cannot be null");

        listing.remove(t);
    }

    /**
     * Gets whether or not this simple list contains the specified item
     *
     * @param t the item to lookup
     *
     * @return true if contained, false otherwise
     */
    public boolean hasItem(T t) {
        if (t == null) throw new IllegalArgumentException("item cannot be null");

        return listing.contains(t);
    }

    /**
     * Gets a listing of all items on the simple list
     *
     * @return the item listing
     */
    public List<T> getListing() {
        return Collections.unmodifiableList(listing);
    }

}
