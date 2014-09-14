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

package com.turt2live.antishare.bukkit.abstraction.v1_7_R2;

import com.turt2live.antishare.bukkit.abstraction.v1_7_R2.uuid.BukkitUuidCache;
import com.turt2live.antishare.uuid.UuidService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class Minecraft extends com.turt2live.antishare.bukkit.abstraction.v1_7_R1.Minecraft {

    @Override
    public void initialize() {
        super.initialize(); // Just in case
        UuidService.getInstance().addSource(new BukkitUuidCache());
    }

    @Override
    @SuppressWarnings("deprecation")
    public String getName(UUID uuid) {
        if (uuid == null) throw new IllegalArgumentException("uuid cannot be null");

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if (player.getName().equalsIgnoreCase("InvalidUUID")) {
            return null; // This is Bukkit's bad code
        }
        return player.getName();
    }
}
