package com.turt2live.antishare.inventory;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.collections.SlottedCollection;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Represents a store for inventory information
 *
 * @param <T> the type of inventory
 * @author turt2live
 */
public abstract class ASInventory<T extends ASItem> implements SlottedCollection<T>, Cloneable {

    protected ConcurrentMap<Integer, T> inventory = new ConcurrentHashMap<Integer, T>();
    protected String world;
    protected UUID player;
    protected ASGameMode gameMode;

    /**
     * Creates a new inventory
     *
     * @param player   the player's UUID
     * @param world    the world for the inventory
     * @param gamemode the gamemode of the inventory
     */
    public ASInventory(UUID player, String world, ASGameMode gamemode) {
        if (player == null || world == null || gamemode == null)
            throw new IllegalArgumentException("player UUID, world, and gamemode cannot be null");

        this.player = player;
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
    public UUID getPlayerUUID() {
        return player;
    }

    /**
     * Determines if the inventories are similar regardless of the inventory content. This
     * will compare everything except the actual inventory contents.
     *
     * @param inventory the inventory to compare to, cannot be null
     * @return true if similar, false otherwise
     */
    public boolean isSimilar(ASInventory<T> inventory) {
        if (gameMode != inventory.gameMode) return false;
        if (!player.equals(inventory.player)) return false;
        if (!world.equals(inventory.world)) return false;

        return true;
    }

    @Override
    public abstract ASInventory<T> clone();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ASInventory that = (ASInventory) o;

        if (gameMode != that.gameMode) return false;
        if (!inventory.equals(that.inventory)) return false;
        if (!player.equals(that.player)) return false;
        if (!world.equals(that.world)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = inventory.hashCode();
        result = 31 * result + world.hashCode();
        result = 31 * result + player.hashCode();
        result = 31 * result + gameMode.hashCode();
        return result;
    }
}
