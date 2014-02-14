package com.turt2live.antishare;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Allows the registration of block IDs and strings
 *
 * @author turt2live
 */
public class IDRegistry {

    private static ConcurrentMap<Integer, String> map = new ConcurrentHashMap<Integer, String>();

    /**
     * Gets the name for a block/item ID
     *
     * @param id the id to lookup
     * @return the Minecraft name, or null if not found
     */
    public static String getMinecraftName(int id) {
        return map.get(id);
    }

    /**
     * Sets the name for a block/item ID
     *
     * @param id   the id to set
     * @param name the name to set
     */
    public static void setMinecraftName(int id, String name) {
        if (name != null) map.put(id, name);
    }

}
