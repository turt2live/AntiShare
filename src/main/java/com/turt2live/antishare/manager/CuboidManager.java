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
package com.turt2live.antishare.manager;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.cuboid.Cuboid;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Cuboid manager
 */
public class CuboidManager {

    /**
     * A cuboid point
     */
    public static enum CuboidPoint {
        POINT1,
        POINT2;
    }

    private final Map<String, Cuboid> cuboids = new HashMap<String, Cuboid>();
    private AntiShare plugin = AntiShare.p;

    /**
     * Gets the cuboid for a player. This will also check WorldEdit if found
     *
     * @param player the player
     * @return the cuboid, or null if not found
     */
    public Cuboid getCuboid(String player) {
        Cuboid cuboid = cuboids.containsKey(player) ? cuboids.get(player).clone() : null;
        Player playerObj = plugin.getServer().getPlayer(player);
        if (cuboid == null && playerObj != null) {
            cuboid = plugin.getHookManager().hasWorldEdit() ? plugin.getHookManager().getWorldEdit().getCuboid(playerObj) : null;
        }
        return cuboid;
    }

    /**
     * Determines if the cuboid in the manager is complete
     *
     * @param player the player
     * @return true if valid and complete
     */
    public boolean isCuboidComplete(String player) {
        Cuboid cuboid = getCuboid(player);
        if (cuboid != null) {
            return cuboid.isValid();
        }
        return false;
    }

    /**
     * Updates a cuboid
     *
     * @param player the player
     * @param point  the point
     * @param value  the value
     */
    public void updateCuboid(String player, CuboidPoint point, Location value) {
        Cuboid cuboid = getCuboid(player);
        if (cuboid == null) {
            cuboid = new Cuboid();
        }
        cuboid.setPoint(point, value);
        cuboid.setWorld(value.getWorld());
        cuboids.put(player, cuboid.clone());
    }

    /**
     * Saves all the cuboids to disk for loading later
     */
    public void save() {
        File file = new File(plugin.getDataFolder(), "data" + File.separator + "cuboids.yml");
        if (file.exists()) {
            file.delete();
        }
        EnhancedConfiguration yamlFile = new EnhancedConfiguration(file, plugin);
        yamlFile.load();
        for (String player : cuboids.keySet()) {
            Cuboid cuboid = cuboids.get(player);
            yamlFile.set(player, cuboid);
        }
        yamlFile.save();
        cuboids.clear();
    }

    /**
     * Loads all cuboids
     */
    public void load() {
        File file = new File(plugin.getDataFolder(), "data" + File.separator + "cuboids.yml");
        if (!file.exists()) {
            return;
        }
        EnhancedConfiguration yamlFile = new EnhancedConfiguration(file, plugin);
        yamlFile.load();
        for (String player : yamlFile.getKeys(false)) {
            Cuboid cuboid = (Cuboid) yamlFile.get(player);
            cuboids.put(player, cuboid);
        }
        if (cuboids.keySet().size() > 0) {
            plugin.getLogger().info(plugin.getMessages().getMessage("cuboids-loaded", String.valueOf(this.cuboids.keySet().size())));
        }
    }

    /**
     * Reloads the cuboid manager
     */
    public void reload() {
        save();
        load();
    }

    /**
     * Removes a player's cuboid
     *
     * @param name the player name
     */
    public void removeCuboid(String name) {
        cuboids.remove(name);
    }

}
