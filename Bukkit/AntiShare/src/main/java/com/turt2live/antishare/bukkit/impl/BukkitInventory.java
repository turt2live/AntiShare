/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.bukkit.impl;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.io.memory.MemoryInventoryManager;
import com.turt2live.antishare.lib.items.AbstractedItem;
import com.turt2live.antishare.lib.items.bukkit.BukkitAbstractItem;
import com.turt2live.antishare.object.AWorld;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an inventory for Bukkit items
 *
 * @author turt2live
 */
public class BukkitInventory extends MemoryInventoryManager.MemoryInventory {

    /**
     * Creates a new memory inventory
     *
     * @param world    the world, cannot be null
     * @param gamemode the gamemode, cannot be null
     */
    public BukkitInventory(AWorld world, ASGameMode gamemode) {
        super(world, gamemode);
    }

    /**
     * Sets the contents of this inventory to the supplied inventory source
     *
     * @param inventory the inventory source, cannot be null
     */
    public void setContents(Inventory inventory) {
        if (inventory == null) throw new IllegalArgumentException();

        this.setContents((Map<Integer, AbstractedItem>) null);

        Map<Integer, AbstractedItem> itemMap = new HashMap<>();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null) {
                itemMap.put(i, new BukkitAbstractItem(item));
            }
        }

        this.setContents(itemMap);
    }

    /**
     * Sets the supplied inventory to be a mirror of this
     *
     * @param inventory the inventory to populate, cannot be null
     */
    public void setTo(Inventory inventory) {
        if (inventory == null) throw new IllegalArgumentException();

        inventory.clear();

        for (Map.Entry<Integer, AbstractedItem> entry : this.getContents().entrySet()) {
            if (entry.getValue() instanceof BukkitAbstractItem) {
                ItemStack stack = ((BukkitAbstractItem) entry.getValue()).getItemStack();
                inventory.setItem(entry.getKey(), stack);
            }
        }
    }
}
