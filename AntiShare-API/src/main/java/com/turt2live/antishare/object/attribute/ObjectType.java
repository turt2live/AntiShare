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

package com.turt2live.antishare.object.attribute;

/**
 * Represents the various block types
 *
 * @author turt2live
 */
public enum ObjectType {
    /**
     * Creative mode block
     */
    CREATIVE,
    /**
     * Survival mode block
     */
    SURVIVAL,
    /**
     * Adventure mode block
     */
    ADVENTURE,
    /**
     * Spectator mode block
     */
    SPECTATOR,
    /**
     * No associated type
     */
    UNKNOWN;
}
