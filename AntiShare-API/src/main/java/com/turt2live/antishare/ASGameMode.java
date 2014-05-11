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

package com.turt2live.antishare;

/**
 * Represents an AntiShare GameMode
 *
 * @author turt2live
 */
public enum ASGameMode {
    /**
     * Creative mode
     */
    CREATIVE,
    /**
     * Survival mode
     */
    SURVIVAL,
    /**
     * Adventure mode
     */
    ADVENTURE,
    /**
     * Spectator mode
     */
    SPECTATOR;

    /**
     * Attempts to match a string to an ASGameMode
     *
     * @param s the string to lookup
     *
     * @return the matching gamemode, or null if not found
     */
    public static ASGameMode fromString(String s) {
        if (s == null) return null;
        for (ASGameMode value : values()) {
            if (value.name().equalsIgnoreCase(s)) {
                return value;
            }
        }
        return null;
    }
}
