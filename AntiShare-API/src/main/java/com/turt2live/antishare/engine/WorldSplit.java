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

import com.turt2live.antishare.APermission;
import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.object.APlayer;
import com.turt2live.antishare.object.ASLocation;

/**
 * Represents a world split for players. A world split is a axis
 * split in the world which has two sides, each being a different
 * gamemode. When a player crosses the axis line, they will be
 * switched from gamemodes.
 *
 * @author turt2live
 */
public class WorldSplit {

    /**
     * The possible axises to use for splitting
     */
    public static enum Axis {
        X, Z;
    }

    private Axis axis;
    private ASGameMode positive, negative;
    private int value;

    /**
     * Creates a new world split
     *
     * @param axis     the axis to split on, cannot be null
     * @param value    the axis coordinate value to split on
     * @param positive the positive side of the split, cannot be null
     * @param negative the negative side of the split, cannot be null
     */
    public WorldSplit(Axis axis, int value, ASGameMode positive, ASGameMode negative) {
        if (axis == null || positive == null || negative == null) throw new IllegalArgumentException();

        this.axis = axis;
        this.value = value;
        this.positive = positive;
        this.negative = negative;
    }

    /**
     * Determines if a specified player is affected by this world split
     *
     * @param player the player to check, cannot be null
     *
     * @return true if the player is affected by this split, false otherwise
     */
    public boolean isAffected(APlayer player) {
        if (player == null) throw new IllegalArgumentException();

        return !player.hasPermission(APermission.FREE_ROAM);
    }

    /**
     * Processes a player's movement, handling the gamemode changeover
     *
     * @param player the player that is moving, cannot be null
     * @param from   the start location, cannot be null
     * @param to     the end location, cannot be null
     *
     * @return returns the distance the player is to the world split, returning -1
     * for 'crossed' and -2 for 'not applicable to this player'
     */
    public int processMovement(APlayer player, ASLocation from, ASLocation to) {
        if (player == null || from == null || to == null) throw new IllegalArgumentException();

        int distance = getDistance(to);

        if (isAffected(player)) {
            int axisValue = axis == Axis.X ? to.X : to.Z;
            ASGameMode expectedGamemode = axisValue < value ? negative : positive;

            if (player.getGameMode() != expectedGamemode) {
                player.setGameMode(expectedGamemode);
                distance = -1;
            }
        } else distance = -2;

        return distance;
    }

    /**
     * Gets the absolute distance a location is away from the axis.
     *
     * @param location the location, cannot be null
     *
     * @return the absolute distance, always positive or zero
     */
    public int getDistance(ASLocation location) {
        if (location == null) throw new IllegalArgumentException();

        int axisValue = axis == Axis.X ? location.X : location.Z;

        return Math.abs(value - axisValue);
    }

    /**
     * Determines if the specified location range is 'approaching' this world split
     *
     * @param from the start location, cannot be null
     * @param to   the end location, cannot be null
     *
     * @return true if approaching, false otherwise
     */
    public boolean isApproaching(ASLocation from, ASLocation to) {
        if (from == null || to == null) throw new IllegalArgumentException();

        return getDistance(to) < getDistance(from);
    }

}
