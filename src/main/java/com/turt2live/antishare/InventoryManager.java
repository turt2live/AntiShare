package com.turt2live.antishare;

import com.turt2live.antishare.io.InventoryStore;

/**
 * Handles the loading and saving of AntiShare inventories
 *
 * @author turt2live
 */
public class InventoryManager {

    private InventoryStore store;

    /**
     * Creates a new Inventory Manager
     *
     * @param store the store to use, cannot be null
     */
    public InventoryManager(InventoryStore store) {
        if (store == null) throw new IllegalArgumentException();

        this.store = store;
    }

}
