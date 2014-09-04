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

package com.turt2live.antishare.bukkit.impl;

import com.turt2live.antishare.AColor;
import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.abstraction.VersionSelector;
import com.turt2live.antishare.object.AInventory;
import com.turt2live.antishare.object.APlayer;
import com.turt2live.antishare.object.AWorld;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Bukkit player
 *
 * @author turt2live
 */
public class BukkitPlayer extends BukkitEntity implements APlayer {

    private final Player player;

    public BukkitPlayer(Player player) {
        super(player);
        if (player == null) throw new IllegalArgumentException();

        this.player = player;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public UUID getUuid() {
        return VersionSelector.getMinecraft().getUUID(player);
    }

    @Override
    public UUID getUUID() {
        return VersionSelector.getMinecraft().getUUID(player);
    }

    @Override
    public ASGameMode getGameMode() {
        return VersionSelector.getMinecraft().toGameMode(player.getGameMode());
    }

    @Override
    public void setGameMode(ASGameMode gameMode) {
        GameMode gameMode1 = VersionSelector.getMinecraft().toGamemode(gameMode);
        player.setGameMode(gameMode1);
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void sendMessage(String message) {
        message = AColor.toBukkit(message);
        message = ChatColor.translateAlternateColorCodes(AntiShare.COLOR_REPLACE_CHAR, message);
        player.sendMessage(message);
    }

    @Override
    public AWorld getWorld() {
        return new BukkitWorld(player.getWorld());
    }

    @Override
    public AInventory getInventory() {
        BukkitInventory inventory = new BukkitInventory(player.getWorld().getName(), VersionSelector.getMinecraft().toGameMode(player.getGameMode()));
        inventory.setContents(player.getInventory());
        return inventory;
    }

    @Override
    public void setInventory(AInventory inventory) {
        if (inventory == null) {
            player.getInventory().clear();
            player.updateInventory();
            return;
        }

        BukkitInventory bukkitInventory = new BukkitInventory(inventory.getWorld(), inventory.getGameMode());
        bukkitInventory.setContents(inventory.getContents());
        bukkitInventory.setTo(player.getInventory());
        player.updateInventory();
    }
}
