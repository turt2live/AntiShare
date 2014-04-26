package com.turt2live.antishare.io.memory;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.inventory.ASItem;
import com.turt2live.antishare.inventory.defaults.DefaultASInventory;
import com.turt2live.antishare.inventory.defaults.DefaultInventorySerializer;
import com.turt2live.antishare.io.generics.GenericInventoryStore;

import java.util.UUID;

/**
 * Represents an inventory store that is not persistent in any way. Any changes to this
 * inventory store are done in memory and are not saved to any form of persistent storage.
 * <br/>
 * There are no errors for saving/loading the inventory store. This would silently fail.
 *
 * @param <T> the type of inventory item
 * @author turt2live
 */
public class MemoryInventoryStore<T extends ASItem> extends GenericInventoryStore<T> {

    /**
     * Creates a new memory inventory store
     *
     * @param uuid  the UUID this store represents, cannot be null
     * @param world the world this store is for, cannot be null
     * @throws IllegalArgumentException thrown for invalid arguments
     */
    public MemoryInventoryStore(UUID uuid, String world) {
        super(uuid, world);
    }

    @Override
    protected String getDefaultSerializerClass() {
        return DefaultInventorySerializer.class.getCanonicalName();
    }

    @Override
    protected void fillEmpty() {
        for (ASGameMode gamemode : ASGameMode.values()) {
            if (getInventory(gamemode) == null) {
                setInventory(gamemode, new DefaultASInventory(this.uuid, this.world, gamemode));
            }
        }
    }

    /**
     * @deprecated Does nothing
     */
    @Override
    @Deprecated
    protected void loadAll() {
    }

    /**
     * @deprecated Does nothing
     */
    @Override
    @Deprecated
    public void save() {
    }
}
