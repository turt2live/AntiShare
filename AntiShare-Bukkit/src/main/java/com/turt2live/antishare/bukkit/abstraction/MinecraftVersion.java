package com.turt2live.antishare.bukkit.abstraction;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

/**
 * Minecraft version abstraction layer representation
 *
 * @author turt2live
 */
public interface MinecraftVersion {

    /**
     * Gets the UUID for the specified name
     *
     * @param name the player name to lookup, cannot be null
     * @return the player's UUID, or null if not found
     */
    public UUID getUUID(String name);

    /**
     * Gets the UUID for the specified player
     *
     * @param player the player to lookup, cannot be null
     * @return the player's UUID, or null if not found
     */
    public UUID getUUID(OfflinePlayer player);

    /**
     * Gets the player name for the specified UUID
     *
     * @param uuid the UUID to lookup, cannot be null
     * @return the player's name, or null if not found
     */
    public String getName(UUID uuid);

    /**
     * Gets the player name for the specified player
     *
     * @param player the player to lookup, cannot be null
     * @return the player's name, or null if not found
     */
    public String getName(OfflinePlayer player);

}
