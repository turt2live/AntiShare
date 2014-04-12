package com.turt2live.antishare.collections.inventory;

import com.turt2live.antishare.inventory.ASInventory;
import com.turt2live.antishare.inventory.ASItem;
import com.turt2live.antishare.utils.ASGameMode;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Represents a collection of AntiShare inventories. The collection is designed to
 * store the gamemodes of Minecraft and the associated player inventories. This is
 * not designed to store multiple players. The inventories stored within are
 * considered to be references and therefore can be modified (and modify the underlying
 * objects using those references, such as storage). <b>Changing the GameMode of an ASInventory
 * has NO EFFECT on the storage in the inventory collection!</b> The collection is
 * supposed to be very similar to a result set and therefore does not keep track of the
 * underlying GameMode values.
 *
 * @param <T> the type of inventory
 * @author turt2live
 */
public class ASInventoryCollection<T extends ASItem> {

    private ConcurrentMap<ASGameMode, ASInventory<T>> inventories = new ConcurrentHashMap<ASGameMode, ASInventory<T>>();
    private UUID player;

    /**
     * Creates a new ASInventoryCollection
     *
     * @param player       the player UUID, cannot be null
     * @param inventoryMap the inventory map, cannot be null
     * @throws java.lang.IllegalArgumentException thrown for illegal arguments
     */
    public ASInventoryCollection(UUID player, Map<ASGameMode, ASInventory<T>> inventoryMap) {
        if (player == null || inventoryMap == null) throw new IllegalArgumentException();

        this.player = player;
        this.inventories.putAll(inventoryMap);
    }

    /**
     * Gets the specified ASInventory for the specified Game Mode. If the gamemode is not
     * registered in the underlying collection, null is returned. The ASInventory's game mode
     * property cannot be guaranteed to be that of the passed value.
     *
     * @param gamemode the gamemode to lookup, cannot be null
     * @return the inventory or null if not found
     * @throws java.lang.IllegalArgumentException thrown for illegal arguments
     */
    public ASInventory<T> getInventory(ASGameMode gamemode) {
        if (gamemode == null) throw new IllegalArgumentException();

        return inventories.get(gamemode);
    }

    /**
     * Gets the associated UUID of the player for this inventory collection
     *
     * @return the player's UUID
     */
    public UUID getPlayer() {
        return player;
    }

    /**
     * Re-associates the underlying ASInventories to the gamemodes stored. Multiple ASInventories
     * with the same GameMode will overwrite themselves, resulting in data loss. The 'winner' of
     * the comparison is semi-random and cannot be assumed to be any case. <b>This will NOT refresh
     * the underlying ASInventories and will NOT pull newly created data. This must be done from
     * the other management classes.</b>
     */
    public void reassociate() {
        ConcurrentMap<ASGameMode, ASInventory<T>> newMap = new ConcurrentHashMap<ASGameMode, ASInventory<T>>();

        for (Map.Entry<ASGameMode, ASInventory<T>> entry : inventories.entrySet()) {
            newMap.put(entry.getValue().getGameMode(), entry.getValue());
        }

        inventories = newMap;
    }

}
