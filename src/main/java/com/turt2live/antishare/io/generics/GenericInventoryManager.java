package com.turt2live.antishare.io.generics;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.inventory.ASInventory;
import com.turt2live.antishare.inventory.ASInventoryCollection;
import com.turt2live.antishare.inventory.ASItem;
import com.turt2live.antishare.inventory.InventorySerializer;
import com.turt2live.antishare.io.InventoryManager;
import com.turt2live.antishare.io.InventoryStore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Generic inventory manager class
 *
 * @param <T> the inventory storage type
 * @author turt2live
 */
public abstract class GenericInventoryManager<T extends ASItem> implements InventoryManager<T> {

    protected InventorySerializer serializer;
    private ConcurrentMap<UUID, InventoryStore<T>> inventories = new ConcurrentHashMap<UUID, InventoryStore<T>>();

    @Override
    public void setSerializer(InventorySerializer serializer) {
        if (serializer == null) throw new IllegalArgumentException("serializer cannot be null");

        this.serializer = serializer;

        for (InventoryStore<T> store : inventories.values()) {
            store.setSerializer(serializer);
        }
    }

    @Override
    public ASInventoryCollection<T> getInventories(UUID uuid) {
        if (uuid == null) return null;

        if (inventories.containsKey(uuid)) {
            return buildCollection(uuid);
        }

        loadStore(uuid);

        if (inventories.containsKey(uuid)) {
            return buildCollection(uuid);
        }

        return null;
    }

    @Override
    public void addInventory(ASInventoryCollection<T> collection) {
        if (collection == null) throw new IllegalArgumentException("collection cannot be null");

        InventoryStore<T> store = toStore(collection);
        store.setSerializer(serializer);

        inventories.put(collection.getPlayer(), store);
    }

    @Override
    public void saveAll() {
        for (InventoryStore store : inventories.values()) {
            store.save();
        }
    }

    @Override
    public void cleanup() {
        long now = System.currentTimeMillis();
        for (Map.Entry<UUID, InventoryStore<T>> storeEntry : inventories.entrySet()) {
            UUID uuid = storeEntry.getKey();
            InventoryStore<T> store = storeEntry.getValue();

            long lastAccess = store.getLastAccess();
            if (now - lastAccess > Engine.getInstance().getCacheMaximum()) {
                store.save();
                inventories.remove(uuid);
            }
        }
    }

    protected ASInventoryCollection<T> buildCollection(UUID uuid) {
        InventoryStore<T> store = inventories.get(uuid);
        if (store == null) return null;

        Map<ASGameMode, ASInventory<T>> mapping = new HashMap<ASGameMode, ASInventory<T>>();
        for (ASGameMode gamemode : ASGameMode.values()) {
            mapping.put(gamemode, store.getInventory(gamemode));
        }

        return new ASInventoryCollection<T>(store.getUUID(), mapping);
    }

    /**
     * Loads an InventoryStore from a UUID. This should never return null
     * and therefore will create an empty store for a new UUID.
     *
     * @param uuid the UUID to lookup, never null
     * @return the InventoryStore, never null
     */
    protected abstract InventoryStore<T> loadStore(UUID uuid);

    /**
     * Converts an inventory collection to a store
     *
     * @param collection the collection, never null
     * @return the created store, never null
     */
    protected abstract InventoryStore<T> toStore(ASInventoryCollection<T> collection);
}
