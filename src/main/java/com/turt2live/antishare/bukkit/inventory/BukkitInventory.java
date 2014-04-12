package com.turt2live.antishare.bukkit.inventory;

import com.turt2live.antishare.utils.ASGameMode;
import com.turt2live.antishare.inventory.ASInventory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A Bukkit inventory wrapper for AntiShare
 *
 * @author turt2live
 */
public class BukkitInventory extends ASInventory<ItemWrapper> {

    /**
     * Creates a new inventory
     *
     * @param player   the player's UUID
     * @param world    the world for the inventory
     * @param gamemode the gamemode of the inventory
     */
    public BukkitInventory(UUID player, String world, ASGameMode gamemode) {
        super(player, world, gamemode);
    }

    @Override
    public ASInventory<ItemWrapper> clone() {
        BukkitInventory inventory1 = new BukkitInventory(player, world, gameMode);
        ConcurrentMap<Integer, ItemWrapper> slots = new ConcurrentHashMap<Integer, ItemWrapper>();
        for (int slot : this.inventory.keySet()) {
            slots.put(slot, this.inventory.get(slot));
        }
        inventory1.inventory = slots;
        return inventory1;
    }
}
