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
