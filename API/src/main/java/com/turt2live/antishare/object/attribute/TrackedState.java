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

package com.turt2live.antishare.object.attribute;

/**
 * Tracking states for lists. This is also used by the can* checks
 * in {@link com.turt2live.antishare.object.ABlock} for tri-state purposes. See
 * ABlock for more information.
 *
 * @author turt2live
 */
public enum TrackedState {
    /**
     * Indicates the record was negated from the list
     */
    NEGATED,
    /**
     * Indicates the record is 'tracked'
     */
    INCLUDED,
    /**
     * Indicates the record is not present in a list
     */
    NOT_PRESENT
}
