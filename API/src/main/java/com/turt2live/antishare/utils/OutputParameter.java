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

package com.turt2live.antishare.utils;

/**
 * Represents an output parameter which can be used similar to C#'s out keyword
 *
 * @param <T> the type
 */
public class OutputParameter<T> {

    private T value;
    private boolean wasCalled = false;

    /**
     * Creates an output parameter with a default null value
     */
    public OutputParameter() {
    }

    /**
     * Creates an output parameter with a default specified value
     *
     * @param def the default value, can be null
     */
    public OutputParameter(T def) {
        this.value = def;
    }

    /**
     * Sets this output parameter to a value
     *
     * @param value the value to set, can be null
     */
    public void setValue(T value) {
        this.value = value;
        wasCalled = true;
    }

    /**
     * Determines if {@link #getValue()} is a non-null value
     *
     * @return true if not null
     */
    public boolean hasValue() {
        return getValue() != null;
    }

    /**
     * Gets the stored value
     *
     * @return the stored value, may be null
     */
    public T getValue() {
        return value;
    }

    /**
     * Determines if {@link #setValue(Object)} was called at least once
     *
     * @return true if called at least once
     */
    public boolean wasCalled() {
        return wasCalled;
    }

}
