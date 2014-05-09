package com.turt2live.antishare.object.attribute;

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
