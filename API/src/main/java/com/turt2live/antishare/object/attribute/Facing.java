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
 * A facing direction, or block face
 */
public enum Facing {
    /**
     * Facing north
     */
    NORTH,
    /**
     * Facing west
     */
    WEST,
    /**
     * Facing south
     */
    SOUTH,
    /**
     * Facing east
     */
    EAST,
    /**
     * Facing up
     */
    UP,
    /**
     * Facing down
     */
    DOWN;

    /**
     * Gets the opposite facing direction relative to this
     * facing direction.
     *
     * @return the opposite facing direction
     */
    public Facing opposite() {
        switch (this) {
            case NORTH:
                return SOUTH;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
            case EAST:
                return WEST;
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            default:
                return null;
        }
    }
}
