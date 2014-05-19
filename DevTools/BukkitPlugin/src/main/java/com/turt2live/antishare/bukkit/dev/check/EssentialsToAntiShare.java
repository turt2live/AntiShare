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

package com.turt2live.antishare.bukkit.dev.check;

import com.turt2live.antishare.bukkit.dev.AntiShare;
import com.turt2live.antishare.bukkit.dev.CheckBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.io.*;

@SuppressWarnings("deprecation")
public class EssentialsToAntiShare extends CheckBase {

    public EssentialsToAntiShare(AntiShare plugin) {
        super(plugin);
    }

    @Override
    public void begin() {
        Bukkit.broadcastMessage(ChatColor.GREEN + "Translating items.csv from Essentials format...");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(plugin.getResource("items.csv")));
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(plugin.getDataFolder(), "item_aliases.csv"), false));

            writer.write("#version: AntiShare\n" +
                    "#This file is used for mapping common names to Material enum entries\n" +
                    "#item,material name");
            writer.newLine();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("*")) continue;

                String[] parts = line.split(",");
                if (parts.length != 3) continue;

                String name = parts[0];
                String id = parts[1];

                try {
                    int intId = Integer.parseInt(id);
                    Material material = Material.getMaterial(intId);

                    if (material == null) continue;

                    writer.write(name + "," + material.name());
                    writer.newLine();
                } catch (NumberFormatException ignored) {
                }
            }

            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        plugin.getServer().broadcastMessage(ChatColor.GREEN + "Done item_aliases.csv!");

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(plugin.getResource("lang.txt")));
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(plugin.getDataFolder(), "item_lang.csv"), false));

            writer.write("#version: AntiShare\n" +
                    "#This file is used for mapping data values to player-friendly names\n" +
                    "#name:data,player-friendly");
            writer.newLine();

            String id = null;
            String line;
            while ((line = reader.readLine()) != null) {
                if (id == null) {
                    if (line.startsWith("#")) continue;
                    String[] split = line.split("-");

                    Material material = Material.AIR;
                    try {
                        int intId = Integer.parseInt(split[0]);
                        material = Material.getMaterial(intId);

                        if (material == null) continue;
                    } catch (NumberFormatException ignored) {
                    }

                    id = material.name() + ":" + (split.length > 1 ? split[1] : "0");
                } else {
                    writer.write(id + "," + line);
                    writer.newLine();
                    id = null;
                }
            }

            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        plugin.getServer().broadcastMessage(ChatColor.GREEN + "Done item_lang.csv!");
        plugin.getServer().broadcastMessage(ChatColor.GREEN + "Done!");

    }
}
