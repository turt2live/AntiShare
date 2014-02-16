package com.turt2live.antishare.inventory;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.collections.SlottedCollection;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Represents a store for inventory information
 *
 * @author turt2live
 */
public abstract class ASInventory<T> implements SlottedCollection<T>, Cloneable {

    protected ConcurrentMap<Integer, T> inventory = new ConcurrentHashMap<Integer, T>();
    protected String world, playerUUID;
    protected ASGameMode gameMode;

    /**
     * Creates a new inventory
     *
     * @param playerUUID the player's UUID string
     * @param world      the world for the inventory
     * @param gamemode   the gamemode of the inventory
     */
    public ASInventory(String playerUUID, String world, ASGameMode gamemode) {
        if (playerUUID == null || world == null || gamemode == null)
            throw new IllegalArgumentException("player UUID, world, and gamemode cannot be null");

        this.playerUUID = playerUUID;
        this.world = world;
        this.gameMode = gamemode;
    }

    @Override
    public void set(int slot, T t) {
        inventory.put(slot, t);
    }

    @Override
    public T get(int slot) {
        return inventory.get(slot);
    }

    @Override
    public Map<Integer, T> getAll() {
        return Collections.unmodifiableMap(inventory);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    /**
     * Gets the game mode of this inventory
     *
     * @return the game mode
     */
    public ASGameMode getGameMode() {
        return gameMode;
    }

    /**
     * Gets the world of this inventory
     *
     * @return the world
     */
    public String getWorld() {
        return world;
    }

    /**
     * Gets the player's UUID for this inventory
     *
     * @return the player's UUID
     */
    public String getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public abstract ASInventory<T> clone();
}
