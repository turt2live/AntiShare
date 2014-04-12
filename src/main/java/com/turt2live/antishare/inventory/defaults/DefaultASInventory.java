package com.turt2live.antishare.inventory.defaults;

import com.turt2live.antishare.utils.ASGameMode;
import com.turt2live.antishare.inventory.ASInventory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Represents a default ASInventory to use
 *
 * @author turt2live
 */
public class DefaultASInventory extends ASInventory<DefaultASItem> {

    /**
     * Creates a new inventory
     *
     * @param player   the player's UUID
     * @param world    the world for the inventory
     * @param gamemode the gamemode of the inventory
     */
    public DefaultASInventory(UUID player, String world, ASGameMode gamemode) {
        super(player, world, gamemode);
    }

    @Override
    public ASInventory<DefaultASItem> clone() {
        DefaultASInventory inv = new DefaultASInventory(player, world, gameMode);
        inv.inventory = cloneMap(this.inventory);
        return inv;
    }

    private ConcurrentMap<Integer, DefaultASItem> cloneMap(ConcurrentMap<Integer, DefaultASItem> inventory) {
        ConcurrentMap<Integer, DefaultASItem> map = new ConcurrentHashMap<Integer, DefaultASItem>();
        for (Map.Entry<Integer, DefaultASItem> entry : inventory.entrySet()) {
            map.put(entry.getKey(), (DefaultASItem) entry.getValue().clone());
        }
        return map;
    }
}
