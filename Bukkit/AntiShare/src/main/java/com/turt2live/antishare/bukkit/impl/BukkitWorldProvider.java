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

import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.engine.WorldProvider;
import com.turt2live.antishare.object.AWorld;
import org.bukkit.World;

/**
 * Represents a Bukkit world provider
 *
 * @author turt2live
 */
public class BukkitWorldProvider implements WorldProvider {

    @Override
    public AWorld getWorld(String name) {
        if (name == null) throw new IllegalArgumentException();

        AntiShare plugin = AntiShare.getInstance();
        World world = plugin.getServer().getWorld(name);
        if (world == null) return null;

        return new BukkitWorld(world);
    }
}
