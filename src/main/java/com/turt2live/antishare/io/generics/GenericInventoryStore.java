package com.turt2live.antishare.io.generics;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.inventory.ASInventory;
import com.turt2live.antishare.inventory.ASItem;
import com.turt2live.antishare.inventory.InventorySerializer;
import com.turt2live.antishare.io.InventoryStore;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Generic inventory store
 *
 * @param <T> the type of item to store
 * @author turt2live
 */
public abstract class GenericInventoryStore<T extends ASItem> implements InventoryStore<T> {

    private long lastAccess = 0;
    protected final UUID uuid;
    private String loadedSerializerClass;
    protected InventorySerializer serializer;
    private ConcurrentMap<ASGameMode, ASInventory<T>> inventories = new ConcurrentHashMap<ASGameMode, ASInventory<T>>();

    /**
     * Creates a new generic inventory store
     *
     * @param uuid the UUID this store represents, cannot be null
     * @throws java.lang.IllegalArgumentException thrown for invalid arguments
     */
    public GenericInventoryStore(UUID uuid) {
        if (uuid == null) throw new IllegalArgumentException();

        this.uuid = uuid;
        this.loadedSerializerClass = getDefaultSerializerClass();
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

    private void updateLastAccess() {
        lastAccess = System.currentTimeMillis();
    }

    @Override
    public final void load() {
        loadAll();
        fillEmpty();

        for (ASGameMode gamemode : ASGameMode.values()) {
            if (getInventory(gamemode) == null) throw new NullPointerException("Failed to load all gamemodes");
        }
    }

    protected abstract String getDefaultSerializerClass();

    protected abstract void fillEmpty();

    protected abstract void loadAll();
}
