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

package com.turt2live.antishare.engine;

import com.turt2live.antishare.object.AWorld;

/**
 * Represents a simple world provider
 *
 * @author turt2live
 */
public interface WorldProvider {

    /**
     * Gets a world for the specified name
     *
     * @param name the world name, cannot be null
     *
     * @return the world, or null if not found
     */
    public AWorld getWorld(String name);
}
