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

package com.turt2live.antishare.bukkit.groups;

import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.configuration.BukkitConfiguration;
import com.turt2live.antishare.configuration.Configuration;
import com.turt2live.antishare.configuration.groups.Group;
import com.turt2live.antishare.configuration.groups.MainGroup;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;

/**
 * Bukkit group manager
 */
public class BukkitGroupManager extends com.turt2live.antishare.configuration.groups.GroupManager {

    private AntiShare plugin = AntiShare.getInstance();

    @Override
    public void loadAll() {
        super.groups.clear();
        super.mainGroup = null;
        File[] files = plugin.getDataFolder().listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().startsWith("group_") && file.getName().toLowerCase().endsWith(".yml")) {
                    Configuration groupConfig = new BukkitConfiguration(file);
                    if (file.getName().equalsIgnoreCase("group_main.yml")) {
                        MainGroup main = new BukkitMainGroup(groupConfig);
                        super.mainGroup = main;
                    } else {
                        Group group = new BukkitGroup(groupConfig);
                        super.groups.put(group.getName(), group);
                    }
                }
            }
        }
        if (super.mainGroup == null) {
            File file = new File(plugin.getDataFolder(), "group_main.yml");
            org.bukkit.configuration.file.YamlConfiguration yaml = new org.bukkit.configuration.file.YamlConfiguration();
            try {
                yaml.load(plugin.getResource("group.yml"));
                yaml.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
            Configuration groupConfig = new BukkitConfiguration(file);
            MainGroup main = new BukkitMainGroup(groupConfig);
            super.mainGroup = main;
        }
    }
}
