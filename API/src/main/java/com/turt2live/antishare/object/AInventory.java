/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.object;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.lib.items.AbstractedItem;

import java.util.Map;

/**
 * Represents a simple inventory
 *
 * @author turt2live
 */
public interface AInventory {

    /**
     * Gets the world this inventory is applicable for
     *
     * @return the world
     */
    public AWorld getWorld();

    /**
     * Sets the world this inventory is applicable for
     *
     * @param world the new world, cannot be null
     */
    public void setWorld(AWorld world);

    /**
     * Gets the gamemode this inventory is applicable for
     *
     * @return the gamemode
     */
    public ASGameMode getGameMode();

    /**
     * Sets the gamemode this inventory is applicable for
     *
     * @param gamemode the new gamemode, cannot be null
     */
    public void setGameMode(ASGameMode gamemode);

    /**
     * Gets the contents of this inventory where the key in the map represents
     * a slot number. The collection cannot be modified, but the values can.
     *
     * @return the inventory contents, never null but may be empty
     */
    public Map<Integer, AbstractedItem> getContents();

    /**
     * Sets the contents of this inventory. If this is null or otherwise empty,
     * the inventory is cleared. The key in the map represents the slot number.
     * Slot numbers are not validated, but uses of the collection may validate
     * and/or ignore "out of range" slot numbers.
     *
     * @param items the items in the inventory
     */
    public void setContents(Map<Integer, AbstractedItem> items);

}
