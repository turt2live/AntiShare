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

package com.turt2live.antishare.bukkit.abstraction.v1_7_R2.uuid;

import com.turt2live.antishare.uuid.CacheSource;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

/**
 * Bukkit UUID cache service
 *
 * @author turt2live
 */
public class BukkitUuidCache implements CacheSource {

    @Override
    public UUID get(String playerName) {
        if (playerName == null) throw new IllegalArgumentException();

        OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(playerName);
        if (player != null && player.hasPlayedBefore()) {
            return player.getUniqueId();
        }

        return null;
    }

    @Override
    public String get(UUID player) {
        if (player == null) throw new IllegalArgumentException();

        OfflinePlayer oplayer = Bukkit.getOfflinePlayer(player);
        if (oplayer != null && oplayer.hasPlayedBefore()) {
            return oplayer.getName();
        }

        return null;
    }
}
