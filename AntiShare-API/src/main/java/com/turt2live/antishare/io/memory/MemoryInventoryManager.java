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

package com.turt2live.antishare.io.memory;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.io.generics.GenericInventoryManager;
import com.turt2live.antishare.object.AInventory;
import com.turt2live.lib.items.AbstractedItem;

import java.util.*;

/**
 * Represents an inventory manager that is not persistent in any way. Any changes to this
 * manager are done in memory and are not saved to any form of persistent storage.
 * <br/>
 * There are no errors for saving/loading the manager. This would silently fail.
 *
 * @author turt2live
 */
public class MemoryInventoryManager extends GenericInventoryManager {

    /**
     * Represents a simple memory inventory. This type of inventory
     * has no guarantee that it will be saved or persisted in any way.
     */
    public static class MemoryInventory implements AInventory {

        private String world;
        private ASGameMode gamemode;
        private Map<Integer, AbstractedItem> contents = new HashMap<Integer, AbstractedItem>();

        /**
         * Creates a new memory inventory
         *
         * @param world    the world, cannot be null
         * @param gamemode the gamemode, cannot be null
         */
        public MemoryInventory(String world, ASGameMode gamemode) {
            setWorld(world); // Validates null
            setGameMode(gamemode); // Validates null
        }

        @Override
        public String getWorld() {
            return world;
        }

        @Override
        public void setWorld(String world) {
            if (world == null) throw new IllegalArgumentException();

            this.world = world;
        }

        @Override
        public ASGameMode getGameMode() {
            return gamemode;
        }

        @Override
        public void setGameMode(ASGameMode gamemode) {
            if (gamemode == null) throw new IllegalArgumentException();

            this.gamemode = gamemode;
        }

        @Override
        public Map<Integer, AbstractedItem> getContents() {
            return Collections.unmodifiableMap(contents);
        }

        @Override
        public void setContents(Map<Integer, AbstractedItem> items) {
            contents.clear();

            if (items != null) {
                for (Map.Entry<Integer, AbstractedItem> entry : items.entrySet()) {
                    contents.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    @Override
    protected void saveInventories(Map<UUID, List<AInventory>> inventories) {
        // Do nothing
    }

    @Override
    protected List<AInventory> loadInventories(UUID player) {
        return new ArrayList<AInventory>(); // Do nothing
    }

    @Override
    protected AInventory createEmptyInventory(UUID player, ASGameMode gamemode, String world) {
        return new MemoryInventory(world, gamemode);
    }
}
