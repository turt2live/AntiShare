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

package com.turt2live.antishare.object;

/**
 * A rejectable that may be derived.
 *
 * @author turt2live
 * @see DerivedRejectable
 */
public interface DerivableRejectable {

    /**
     * Gets the generic rejectable instance
     *
     * @return the generic instance, or null if not supported
     *
     * @see #hasGeneric()
     */
    public DerivedRejectable getGeneric();

    /**
     * Gets the specific rejectable instance
     *
     * @return the specific instance
     */
    public DerivedRejectable getSpecific();

    /**
     * Determines if this type supports a generic case
     *
     * @return true if supported, false otherwise
     */
    public boolean hasGeneric();

}
