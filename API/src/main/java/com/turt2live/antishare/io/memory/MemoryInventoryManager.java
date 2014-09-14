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

package com.turt2live.antishare.io.memory;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.io.generics.GenericInventoryManager;
import com.turt2live.antishare.object.AInventory;
import com.turt2live.antishare.object.AWorld;
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
// TODO: Unit test
public class MemoryInventoryManager extends GenericInventoryManager {

    /**
     * Represents a simple memory inventory. This type of inventory
     * has no guarantee that it will be saved or persisted in any way.
     */
    public static class MemoryInventory implements AInventory {

        private AWorld world;
        private ASGameMode gamemode;
        private Map<Integer, AbstractedItem> contents = new HashMap<>();

        /**
         * Creates a new memory inventory
         *
         * @param world    the world, cannot be null
         * @param gamemode the gamemode, cannot be null
         */
        public MemoryInventory(AWorld world, ASGameMode gamemode) {
            setWorld(world); // Validates null
            setGameMode(gamemode); // Validates null
        }

        @Override
        public AWorld getWorld() {
            return world;
        }

        @Override
        public void setWorld(AWorld world) {
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
    public List<AInventory> getInventories(UUID player) {
        return new ArrayList<>(); // Do nothing
    }

    @Override
    protected AInventory createEmptyInventory(UUID player, ASGameMode gamemode, AWorld world) {
        return new MemoryInventory(world, gamemode);
    }
}
