package com.turt2live.antishare.io.memory;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.inventory.ASInventoryCollection;
import com.turt2live.antishare.inventory.ASItem;
import com.turt2live.antishare.io.InventoryStore;
import com.turt2live.antishare.io.generics.GenericInventoryManager;
import com.turt2live.hurtle.utils.ArrayArrayList;

import java.util.List;
import java.util.UUID;

/**
 * Represents an inventory manager that is not persistent in any way. Any changes to this
 * inventory manager are done in memory and are not saved to any form of persistent storage.
 * <br/>
 * There are no errors for saving/loading the inventory manager. This would silently fail.
 *
 * @param <T> the type of inventory item
 * @author turt2live
 * @deprecated This should only be used for testing or temporary purposes
 */
@Deprecated
// TODO: Unit test
public class MemoryInventoryManager<T extends ASItem> extends GenericInventoryManager<T> {

    /**
     * Creates a new inventory manager
     *
     * @param world the world to use, cannot be null
     * @throws java.lang.IllegalArgumentException thrown for bad arguments
     */
    public MemoryInventoryManager(String world) {
        super(world);
    }

    /**
     * @deprecated Returns an empty {@link com.turt2live.antishare.io.memory.MemoryInventoryStore} every time
     */
    @Override
    @Deprecated
    protected InventoryStore<T> loadStore(UUID uuid) {
        return new MemoryInventoryStore<T>(uuid, getWorld());
    }

    @Override
    protected InventoryStore<T> toStore(ASInventoryCollection<T> collection) {
        MemoryInventoryStore<T> store = new MemoryInventoryStore<T>(collection.getPlayer(), getWorld());
        for (ASGameMode gamemode : ASGameMode.values()) {
            store.setInventory(gamemode, collection.getInventory(gamemode));
        }
        return store;
    }

    /**
     * @deprecated returns an empty list every time
     */
    @Override
    @Deprecated
    public List<InventoryStore> loadAll() {
        return new ArrayArrayList<InventoryStore>();
    }
}
