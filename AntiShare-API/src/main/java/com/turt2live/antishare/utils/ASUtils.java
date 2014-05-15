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

package com.turt2live.antishare.utils;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.object.attribute.ObjectType;

/**
 * Various AntiShare utilities
 *
 * @author turt2live
 */
public final class ASUtils {

    /**
     * Converts an AntiShare GameMode to a ObjectType. If the passed gamemode
     * is null, this returns {@link com.turt2live.antishare.object.attribute.ObjectType#UNKNOWN}.
     *
     * @param gamemode the gamemode
     *
     * @return the block type
     */
    public static ObjectType toBlockType(ASGameMode gamemode) {
        if (gamemode == null) return ObjectType.UNKNOWN;

        switch (gamemode) {
            case ADVENTURE:
                return ObjectType.ADVENTURE;
            case SURVIVAL:
                return ObjectType.SURVIVAL;
            case CREATIVE:
                return ObjectType.CREATIVE;
            case SPECTATOR:
                return ObjectType.SPECTATOR;
            default:
                return ObjectType.UNKNOWN;
        }
    }

    /**
     * Converts an AntiShare ObjectType to a GameMode. If the passed
     * block type is null or {@link com.turt2live.antishare.object.attribute.ObjectType#UNKNOWN}, this
     * returns null.
     *
     * @param type the block type
     *
     * @return the gamemode
     */
    public static ASGameMode toGamemode(ObjectType type) {
        if (type == null) return null;

        switch (type) {
            case ADVENTURE:
                return ASGameMode.ADVENTURE;
            case SURVIVAL:
                return ASGameMode.SURVIVAL;
            case CREATIVE:
                return ASGameMode.CREATIVE;
            case SPECTATOR:
                return ASGameMode.SPECTATOR;
            default:
                return null;
        }
    }

    /**
     * Converts an input string to have upper case words. Spaces and underscores
     * are considered the same and therefore will create words as such.
     *
     * @param input the input, cannot be null
     *
     * @return the output as upper case words
     */
    public static String toUpperWords(String input) {
        if (input == null) throw new IllegalArgumentException("input cannot be null");

        StringBuilder out = new StringBuilder();
        String[] parts = input.replaceAll("_", " ").split(" ");

        for (String s : parts) {
            out.append(s.substring(0, 1).toUpperCase()).append(s.substring(1).toLowerCase()).append(" ");
        }

        return out.toString().trim();
    }

}
