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
 * An AntiShare world
 *
 * @author turt2live
 */
public interface AWorld {

    /**
     * Gets the name of this world
     *
     * @return the world's name
     */
    public String getName();

    /**
     * Gets the block at the specified location
     *
     * @param location the location of the block. If null, "air" is returned
     *
     * @return the block. If not found an "air" block is returned.
     */
    public ABlock getBlock(ASLocation location);

}
