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

import com.turt2live.antishare.object.Rejectable;
import com.turt2live.antishare.object.attribute.TrackedState;

/**
 * A tracked type list, used for tracking objects
 *
 * @author turt2live
 */
public interface TrackedTypeList<T extends Rejectable> {

    /**
     * Determines if a specified object is tracked
     *
     * @param object the object to lookup
     *
     * @return true if this object should be tracked, false otherwise
     */
    public boolean isTracked(T object);

    /**
     * Gets the tracking state of a specified object. This is generally
     * used by plugin operations for determining what data this list has rather
     * than being used to indicate whether or not a object should be tracked. A
     * better alternative for determining tracking status would be {@link #isTracked(com.turt2live.antishare.object.Rejectable)}.
     *
     * @param object the object to lookup
     *
     * @return the tracking state
     */
    public TrackedState getState(T object);

}
