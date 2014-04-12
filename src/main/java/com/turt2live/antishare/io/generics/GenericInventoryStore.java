package com.turt2live.antishare.io.generics;

import com.turt2live.antishare.inventory.ASInventory;
import com.turt2live.antishare.inventory.ASItem;
import com.turt2live.antishare.inventory.InventorySerializer;
import com.turt2live.antishare.io.InventoryStore;
import com.turt2live.antishare.utils.ASGameMode;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Generic inventory store
 *
 * @param <T> the type of item to store
 * @author turt2live
 */
// TODO: Unit test
public abstract class GenericInventoryStore<T extends ASItem> implements InventoryStore<T> {

    /**
     * The UUID that this inventory store is representing
     */
    protected final UUID uuid;
    /**
     * The serializer this inventory store is using
     */
    protected InventorySerializer serializer;
    /**
     * The world this store is being used by
     */
    protected String world;

    private volatile long lastAccess = 0;
    private String loadedSerializerClass;
    private ConcurrentMap<ASGameMode, ASInventory<T>> inventories = new ConcurrentHashMap<ASGameMode, ASInventory<T>>();

    /**
     * Creates a new generic inventory store
     *
     * @param uuid  the UUID this store represents, cannot be null
     * @param world the world this store is for, cannot be null
     * @throws java.lang.IllegalArgumentException thrown for invalid arguments
     */
    public GenericInventoryStore(UUID uuid, String world) {
        if (uuid == null || world == null) throw new IllegalArgumentException();

        this.uuid = uuid;
        this.loadedSerializerClass = getDefaultSerializerClass();
        this.world = world;
    }

    private void updateLastAccess() {
        lastAccess = System.currentTimeMillis();
    }

    @Override
    public final void load() {
        this.inventories.clear(); // Avoid a double call of fillEmpty()
        loadAll();
        fillEmpty();

        for (ASGameMode gamemode : ASGameMode.values()) {
            if (getInventory(gamemode) == null) throw new NullPointerException("Failed to load all gamemodes");
        }
    }

    @Override
    public final void clear() {
        this.inventories.clear();
        fillEmpty();
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public String getSerializerClass() {
        return loadedSerializerClass;
    }

    @Override
    public String getWorld() {
        return world;
    }

    @Override
    public void setSerializer(InventorySerializer serializer) {
        if (serializer == null) throw new IllegalArgumentException();

        this.serializer = serializer;
        updateLastAccess();
    }

    @Override
    public ASInventory getInventory(ASGameMode gamemode) {
        if (gamemode == null) throw new IllegalArgumentException();
        updateLastAccess();
        return inventories.get(gamemode);
    }

    @Override
    public void setInventory(ASGameMode gamemode, ASInventory inventory) {
        if (gamemode == null || inventory == null) throw new IllegalArgumentException();
        this.inventories.put(gamemode, inventory);
        updateLastAccess();
    }

    @Override
    public long getLastAccess() {
        return lastAccess;
    }

    /**
     * Loads the default serializer class from the storage mechanism
     *
     * @return the class of the default serializer
     */
    protected abstract String getDefaultSerializerClass();

    /**
     * Fills the underlying collection with non-null values. This must set
     * an empty inventory to all gamemodes which are not set. This is called
     * by the underlying class and will be validated.
     */
    protected abstract void fillEmpty();

    /**
     * Loads all records from the underlying storage mechanism
     */
    protected abstract void loadAll();
}
