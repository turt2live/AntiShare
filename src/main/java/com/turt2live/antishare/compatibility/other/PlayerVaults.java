/*******************************************************************************
 * Copyright (c) 2013 Travis Ralston.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.compatibility.other;

import com.drtshock.playervaults.util.VaultHolder;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.PermissionNodes;
import com.turt2live.antishare.util.GamemodeAbstraction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

/**
 * PlayerVaults hook
 *
 * @author turt2live
 */
public class PlayerVaults implements Listener {

    private boolean enabled = false;

    public PlayerVaults() {
        enabled = AntiShare.p.settings().playerVaults;
    }

    @EventHandler
    public void onTarget(InventoryOpenEvent event) {
        if (event.isCancelled() || !enabled || !(event.getPlayer() instanceof Player)) {
            return;
        }
        if (event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof VaultHolder) {
            Player player = (Player) event.getPlayer();
            if (!player.hasPermission(PermissionNodes.PLUGIN_PLAYER_VAULTS) && GamemodeAbstraction.isCreative(player.getGameMode())) {
                event.setCancelled(true);
            }
        }
    }

}
