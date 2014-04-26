package com.turt2live.antishare.utils;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.BlockType;

/**
 * Various AntiShare utilities
 *
 * @author turt2live
 */
public final class ASUtils {

    /**
     * Converts an AntiShare GameMode to a BlockType. If the passed gamemode
     * is null, this returns {@link com.turt2live.antishare.utils.BlockType#UNKNOWN}.
     *
     * @param gamemode the gamemode
     * @return the block type
     */
    public static BlockType toBlockType(ASGameMode gamemode) {
        if (gamemode == null) return BlockType.UNKNOWN;

        switch (gamemode) {
            case ADVENTURE:
                return BlockType.ADVENTURE;
            case SURVIVAL:
                return BlockType.SURVIVAL;
            case CREATIVE:
                return BlockType.CREATIVE;
            case SPECTATOR:
                return BlockType.SPECTATOR;
            default:
                return BlockType.UNKNOWN;
        }
    }

    /**
     * Converts an AntiShare BlockType to a GameMode. If the passed
     * block type is null or {@link com.turt2live.antishare.utils.BlockType#UNKNOWN}, this
     * returns null.
     *
     * @param type the block type
     * @return the gamemode
     */
    public static ASGameMode toGamemode(BlockType type) {
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
