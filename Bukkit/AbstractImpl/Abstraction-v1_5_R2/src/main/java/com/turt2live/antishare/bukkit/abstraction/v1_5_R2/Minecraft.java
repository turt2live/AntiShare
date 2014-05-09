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

package com.turt2live.antishare.bukkit.abstraction.v1_5_R2;

import com.turt2live.antishare.bukkit.abstraction.AntiShareInventoryTransferEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class Minecraft extends com.turt2live.antishare.bukkit.abstraction.v1_5_R1.Minecraft implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        Inventory source = event.getSource();
        Inventory destination = event.getDestination();
        if (source != null && destination != null) {
            Location srcLoc = getLocation(source);
            Location destLoc = getLocation(destination);

            if (srcLoc != null && destLoc != null) {
                AntiShareInventoryTransferEvent asevent = new AntiShareInventoryTransferEvent(srcLoc.getBlock(), destLoc.getBlock());
                Bukkit.getServer().getPluginManager().callEvent(asevent);
                if (asevent.isCancelled()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private Location getLocation(Inventory inventory) {
        InventoryHolder holder = inventory.getHolder();
        if (holder != null) {
            if (holder instanceof DoubleChest)
                return ((DoubleChest) holder).getLocation();
            else if (holder instanceof BlockState) {
                return ((BlockState) holder).getLocation();
            }
        }
        return null;
    }

}
