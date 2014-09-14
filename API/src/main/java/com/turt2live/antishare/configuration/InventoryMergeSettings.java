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

package com.turt2live.antishare.configuration;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.engine.DevEngine;
import com.turt2live.antishare.object.AInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the settings that control how inventories are merged
 *
 * @author turt2live
 */
// TODO: Unit test
public class InventoryMergeSettings {

    private Map<ASGameMode, List<String>> worldMerges = new HashMap<>();
    private Map<ASGameMode, List<ASGameMode>> gamemodeMerges = new HashMap<>();

    public InventoryMergeSettings(Configuration configuration) {
        if (configuration == null) throw new IllegalArgumentException();

        for (ASGameMode gamemode : ASGameMode.values()) {
            List<String> worlds = configuration.getStringList(gamemode.name().toLowerCase() + ".merge-with", new ArrayList<String>());
            List<String> gamemodes = configuration.getStringList(gamemode.name().toLowerCase() + ".gamemode-merge", new ArrayList<String>());

            // Convert all to lowercase
            List<String> newWorlds = new ArrayList<>();
            List<ASGameMode> newGamemodes = new ArrayList<>();

            for (String world : worlds) {
                if (world.equalsIgnoreCase("none")) {
                    newWorlds.clear();
                    break;
                }

                newWorlds.add(world.toLowerCase());
            }

            for (String strGamemode : gamemodes) {
                if (strGamemode.equalsIgnoreCase("none")) {
                    newGamemodes.clear();
                    break;
                }

                ASGameMode gm = ASGameMode.fromString(strGamemode);
                if (gm != null) {
                    newGamemodes.add(gm);
                } else
                    DevEngine.log("Unknown gamemode for inventory merge: " + strGamemode + " (associated with " + gamemode.name() + ")");
            }

            worldMerges.put(gamemode, newWorlds);
            gamemodeMerges.put(gamemode, newGamemodes);
        }

        // Correct possible mismatches found in gamemodeMerges
        for (Map.Entry<ASGameMode, List<ASGameMode>> gamemodeEntry : gamemodeMerges.entrySet()) {
            for (ASGameMode other : gamemodeEntry.getValue()) {
                List<ASGameMode> otherList = gamemodeMerges.get(other);
                if (!otherList.contains(gamemodeEntry.getKey())) {
                    otherList.add(gamemodeEntry.getKey());
                }
            }
        }
    }

    /**
     * Merges an inventory into a collection of other inventories. This means that the "host"
     * inventory will be used as a master and will find any inventories which need changing
     * based upon the merge strategies in the collection. The inventories in the collection
     * may be modified by the time this exits.
     *
     * @param host             the host inventory to use, cannot be null
     * @param otherInventories the other inventories to merge into. Null entries are ignored.
     */
    public void mergeInventories(AInventory host, List<AInventory> otherInventories) {
        if (host == null) throw new IllegalArgumentException();
        if (otherInventories == null) return;

        ASGameMode gamemode = host.getGameMode();
        List<String> worlds = worldMerges.get(gamemode);
        List<ASGameMode> gamemodes = gamemodeMerges.get(gamemode);

        for (AInventory inventory : otherInventories) {
            if (inventory == null || inventory.equals(host)) continue;

            if ((worlds != null && worlds.contains(inventory.getWorld().getName()))
                    || (gamemodes != null && gamemodes.contains(inventory.getGameMode()))) {
                inventory.setContents(host.getContents());
            }
        }
    }

}
