package com.turt2live.antishare.bukkit;

import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.engine.WorldEngine;
import com.turt2live.antishare.io.BlockManager;
import com.turt2live.antishare.utils.ASGameMode;
import com.turt2live.antishare.utils.ASLocation;
import com.turt2live.antishare.utils.ASUtils;
import com.turt2live.antishare.utils.BlockType;
import org.bukkit.GameMode;
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
     * Converts a Bukkit Game Mode to an AntiShare Game Mode
     *
     * @param gamemode the gamemode to convert
     * @return the AntiShare gamemode. If the passed gamemode was null, this will be null
     */
    public static ASGameMode toGameMode(GameMode gamemode) {
        switch (gamemode) {
            case CREATIVE:
                return ASGameMode.CREATIVE;
            case ADVENTURE:
                return ASGameMode.ADVENTURE;
            case SURVIVAL:
                return ASGameMode.SURVIVAL;
            // TODO: 1.8
            default:
                return null;
        }
    }

    /**
     * Converts an AntiShare Game Mode to a Bukkit Game Mode
     *
     * @param gamemode the gamemode to convert
     * @return the Bukkit gamemode. If the passed gamemode was null, this will be null
     */
    public static GameMode toGamemode(ASGameMode gamemode) {
        switch (gamemode) {
            case CREATIVE:
                return GameMode.CREATIVE;
            case ADVENTURE:
                return GameMode.ADVENTURE;
            case SURVIVAL:
                return GameMode.SURVIVAL;
            // TODO: 1.8
            default:
                return null;
        }
    }

    /**
     * Converts a Bukkit Game Mode to an AntiShare Block Type
     *
     * @param gamemode the gamemode to convert
     * @return the block type
     */
    public static BlockType toBlockType(GameMode gamemode) {
        if (gamemode == null) throw new IllegalArgumentException("gamemode cannot be null");

        return ASUtils.toBlockType(toGameMode(gamemode));
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
