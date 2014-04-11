package com.turt2live.antishare.io;

import com.turt2live.antishare.inventory.ASInventoryCollection;
import com.turt2live.antishare.inventory.ASItem;
import com.turt2live.antishare.inventory.InventorySerializer;

import java.util.List;
import java.util.UUID;

/**
 * Handles inventory information
 *
 * @param <T> the type of inventory item
 * @author turt2live
 */
public interface InventoryManager<T extends ASItem> {

    /**
     * Sets the serializer to use for this inventory manager instance
     *
     * @param serializer the serializer to use, cannot be null
     * @throws java.lang.IllegalArgumentException thrown for bad arguments
     */
    public void setSerializer(InventorySerializer serializer);

    /**
     * Gets an ASInventoryCollection of inventories for the specified UUID. If the
     * UUID cannot be found, null is returned
     *
     * @param uuid the UUID to lookup. Null returns null.
     * @return the inventory collection, or null
     * @throws java.lang.IllegalArgumentException thrown for invalid arguments
     */
    public ASInventoryCollection<T> getInventories(UUID uuid);

    /**
     * Adds an inventory collection to the manager, overwriting any previous data. The
     * collection must contain a UUID which is used to match the collection to the backend
     * system(s).
     *
     * @param collection the collection to save, cannot be null
     * @throws java.lang.IllegalArgumentException thrown for invalid arguments
     */
    public void addInventory(ASInventoryCollection<T> collection);

    /**
     * Saves all the known inventory stores
     */
    public void saveAll();

    /**
     * Loads all the known inventory stores. The implementing manager will assume a
     * save has been completed and may wipe the previous entries from memory.
     *
     * @return a list of inventory stores loaded because of this operation
     */
    public List<InventoryStore> loadAll();

    /**
     * Runs a cleanup (on the current thread) on the InventoryManager. This will remove
     * any excess objects which have not been touched from the manager by cleanly
     * unloading them.
     */
    public void cleanup();

}
