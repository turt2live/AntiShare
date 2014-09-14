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

package com.turt2live.antishare.io;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.object.AInventory;
import com.turt2live.antishare.object.AWorld;

import java.util.List;
import java.util.UUID;

/**
 * Represents a simple inventory manager
 *
 * @author turt2live
 */
public interface InventoryManager {

    /**
     * Gets the inventory for a specified player or creates one in-memory for use if not
     * found already. If created, it is not stored to the disk and may be discarded.
     *
     * @param player   the player to lookup, cannot be null
     * @param gamemode the gamemode to lookup, cannot be null
     * @param world    the world to lookup, cannot be null
     *
     * @return the inventory found. This may be empty if it was created.
     */
    public AInventory getInventory(UUID player, ASGameMode gamemode, AWorld world);

    /**
     * Gets all applicable inventories for a specified player
     *
     * @param player the player to lookup, cannot be null
     *
     * @return the inventories found. Never null but may be empty
     */
    public List<AInventory> getInventories(UUID player);

    /**
     * Sets an inventory for a player
     *
     * @param player    the player to set the inventory for, cannot be null
     * @param inventory the inventory to store, cannot be null
     */
    public void setInventory(UUID player, AInventory inventory);

    /**
     * Saves the inventories to disk and removes the existing records from memory.
     */
    public void save();

    /**
     * Saves the specified player's inventories to disk and removes them from memory.
     *
     * @param player the player's inventories
     */
    public void save(UUID player);

    /**
     * Loads inventories from the storage medium
     */
    public void load();

    /**
     * Removes all inventories from memory without saving
     */
    public void clear();

}
