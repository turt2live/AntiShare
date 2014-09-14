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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an array list that takes an array
 *
 * @param <T> the type of list
 *
 * @author turt2live
 */
public class ArrayArrayList<T> extends ArrayList<T> {

    /**
     * Creates a new ArrayArrayList
     *
     * @param items the array items to add
     */
    public ArrayArrayList(T... items) {
        if (items != null) {
            for (T item : items) {
                add(item);
            }
        }
    }

    /**
     * Creates a new ArrayArrayList
     *
     * @param items the array items to add
     */
    public ArrayArrayList(List<T> items) {
        if (items != null) {
            addAll(items);
        }
    }

}
