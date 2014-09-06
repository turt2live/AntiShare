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

package com.turt2live.antishare.bukkit.dev;

import com.turt2live.antishare.bukkit.impl.BukkitBlock;
import com.turt2live.antishare.engine.DevEngine;
import com.turt2live.antishare.events.EventListener;
import com.turt2live.antishare.events.engine.DevEngineStateChangeEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class DevListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand() != null) {

            // Chest type check
            if (player.getItemInHand().getType() == Material.COMPASS) {
                Block clicked = event.getClickedBlock();
                if (clicked != null) {
                    player.sendMessage(new BukkitBlock(clicked).getChestType().name());
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventListener
    public void onDevEngineEnable(DevEngineStateChangeEvent event) {
        if (DevEngine.isEnabled()) {
            DevEngine.log("[Bukkit] Server version: " + com.turt2live.antishare.bukkit.AntiShare.getInstance().getServer().getVersion());
            DevEngine.log("[Bukkit] Bukkit version: " + com.turt2live.antishare.bukkit.AntiShare.getInstance().getServer().getBukkitVersion());
            DevEngine.log("[Bukkit] Online mode: " + com.turt2live.antishare.bukkit.AntiShare.getInstance().getServer().getOnlineMode());
        }
    }
}
