package com.turt2live.antishare.io;

import com.turt2live.antishare.inventory.ASInventory;
import com.turt2live.antishare.inventory.ASItem;
import com.turt2live.antishare.inventory.InventorySerializer;

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
     * Saves an inventory
     *
     * @param inventory the inventory to save, cannot be null
     * @throws java.lang.IllegalArgumentException thrown for bad arguments
     */
    public void save(ASInventory<T> inventory);

    /**
     * Loads an inventory for a specified UUID
     *
     * @param uuid the UUID, cannot be null
     * @return the inventory, or null if not found (or null arguments)
     */
    public ASInventory<T> load(UUID uuid);

}
