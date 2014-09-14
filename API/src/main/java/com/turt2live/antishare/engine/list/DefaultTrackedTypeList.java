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
 * Default block list
 *
 * @author turt2live
 */
public class DefaultTrackedTypeList<T extends Rejectable> implements TrackedTypeList<T> {

    @Override
    public boolean isTracked(T block) {
        return false;
    }

    @Override
    public TrackedState getState(T block) {
        return TrackedState.NOT_PRESENT;
    }

}
