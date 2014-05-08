package com.turt2live.antishare.bukkit.dev;

import com.turt2live.antishare.bukkit.impl.BukkitBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
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

}
