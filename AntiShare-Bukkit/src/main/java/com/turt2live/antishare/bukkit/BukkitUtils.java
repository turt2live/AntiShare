package com.turt2live.antishare.bukkit;

import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.engine.WorldEngine;
import com.turt2live.antishare.io.BlockManager;
import com.turt2live.antishare.utils.ASLocation;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * AntiShare to/from Bukkit Utilities
 *
 * @author turt2live
 */
public final class BukkitUtils {

    /**
     * Converts a Bukkit location to an AntiShare location
     *
     * @param location the bukkit location
     * @return the AntiShare location. If the passed location was null, this will be null
     */
    public static ASLocation toLocation(Location location) {
        if (location == null) return null;

        return new ASLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    /**
     * Converts an AntiShare location to a Bukkit location. This will NOT apply a world to the location.
     *
     * @param location the AntiShare location
     * @return the Bukkit location. If the passed location was null, this will be null
     */
    public static Location toLocation(ASLocation location) {
        if (location == null) return null;

        return new Location(null, location.X, location.Y, location.Z);
    }

    /**
     * Gets the world engine for the supplied world
     *
     * @param world the world, cannot be null
     * @return the world engine
     */
    public static WorldEngine getWorldEngine(World world) {
        if (world == null) throw new IllegalArgumentException("world cannot be null");

        return Engine.getInstance().getEngine(world.getName());
    }

    /**
     * Gets the block manager for the specified world
     *
     * @param world the world, cannot be null
     * @return the block manager
     */
    public static BlockManager getBlockManager(World world) {
        return getWorldEngine(world).getBlockManager();
    }

}
