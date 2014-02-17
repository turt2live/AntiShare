package com.turt2live.antishare;

/**
 * Various AntiShare utilities
 *
 * @author turt2live
 */
public class ASUtils {

    /**
     * Converts an AntiShare GameMode to a BlockType. If the passed gamemode
     * is null, this returns {@link com.turt2live.antishare.BlockType#UNKNOWN}.
     *
     * @param gamemode the gamemode
     * @return the block type
     */
    public static BlockType toBlockType(ASGameMode gamemode) {
        switch (gamemode) {
            case ADVENTURE:
                return BlockType.ADVENTURE;
            case SURVIVAL:
                return BlockType.SURVIVAL;
            case CREATIVE:
                return BlockType.CREATIVE;
            // TODO: 1.8
            default:
                return BlockType.UNKNOWN;
        }
    }

    /**
     * Converts an AntiShare BlockType to a GameMode. If the passed
     * block type is null or {@link com.turt2live.antishare.BlockType#UNKNOWN}, this
     * returns null.
     *
     * @param type the block type
     * @return the gamemode
     */
    public static ASGameMode toGamemode(BlockType type) {
        switch (type) {
            case ADVENTURE:
                return ASGameMode.ADVENTURE;
            case SURVIVAL:
                return ASGameMode.SURVIVAL;
            case CREATIVE:
                return ASGameMode.CREATIVE;
            // TODO: 1.8
            default:
                return null;
        }
    }

}
