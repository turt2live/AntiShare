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
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FallingSandCheck extends CheckBase implements Listener {

    private enum SandState {
        DISSOLVE, // Sand "eats" or "dissolves" the test block
        BREAK, // Sand breaks when landing on the test block
        STOP // Sand is stopped by test block (landing on top)
    }

    private boolean enabled = false;
    private int nextMaterial = 0; // AIR
    private Material[] materials = Material.values();
    private Location blockLocation, sandLocation;
    private Map<SandState, List<Material>> results = new HashMap<>();
    private int nextFall = 0;
    private List<Material> falling = new ArrayList<>();

    public FallingSandCheck(AntiShare plugin) {
        super(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        for (Material material : materials) {
            if (material.hasGravity()) {
                falling.add(material);
            }
        }
    }

    @Override
    public void begin() {
        Bukkit.broadcastMessage(ChatColor.GREEN + "Running falling sand check...");
        plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "WARNING: SERVER LAG INCOMING");

        World world = plugin.getServer().getWorlds().get(0);
        blockLocation = new Location(world, 0, 70, 0);
        sandLocation = blockLocation.clone().add(0, 5, 0);

        prepareCube(10, blockLocation); // Also sets floor

        // Start us off
        enabled = true;
        spawnNextSand();
    }

    private void prepareCube(int radius, Location location) {
        World world = location.getWorld();
        for (int x = 0; x < radius; x++) {
            for (int y = radius; y >= 0; y--) {
                for (int z = 0; z < radius; z++) {
                    // Set ceiling
                    if (y == radius) {
                        Location ceilingBlock = new Location(world, x + location.getBlockX() - 5, y + location.getBlockY() + 1, z + location.getBlockZ() - 5);
                        Block block = ceilingBlock.getBlock();
                        block.setType(Material.STONE);
                    }

                    // Clear area
                    Location possibleBlock = new Location(world, x + location.getBlockX() - 5, y + location.getBlockY(), z + location.getBlockZ() - 5);
                    Block block = possibleBlock.getBlock();
                    block.setType(Material.AIR);

                    // Set floor
                    if (y == 0) {
                        Location floorBlock = new Location(world, x + location.getBlockX() - 5, location.getBlockY() - 1, z + location.getBlockZ() - 5);
                        block = floorBlock.getBlock();
                        block.setType(Material.STONE);
                    }
                }
            }
        }
    }

    private void writeResults() {
        try {
            for (SandState state : SandState.values()) {
                File outputFile = new File(plugin.getDataFolder(), falling.get(nextFall).name() + "_" + state.name() + ".txt");
                if (!outputFile.getParentFile().exists()) outputFile.getParentFile().mkdirs();
                List<Material> materials1 = results.get(state);
                if (materials1 != null) {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, false));
                    for (Material material : materials1) {
                        writer.write(material.name());
                        writer.newLine();
                    }
                    writer.flush();
                    writer.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void spawnNextSand() {
        Material test;
        do {
            nextMaterial++; // Go to next material
            if (nextMaterial >= materials.length) {
                plugin.getServer().broadcastMessage(ChatColor.GREEN + "Done " + falling.get(nextFall).name() + " test");
                writeResults();

                // Reset
                nextFall++;
                if (nextFall >= falling.size()) {
                    plugin.getServer().broadcastMessage(ChatColor.DARK_GREEN + "All tests done");
                    enabled = false;
                    return;
                }

                nextMaterial = 1; // Skip AIR
            }
            test = materials[nextMaterial];
        } while (!test.isBlock() || invalid(test));
        prepareCube(10, blockLocation);
        blockLocation.getBlock().setType(test);
        plugin.getServer().broadcastMessage(ChatColor.AQUA + "Testing " + test.name());
        blockLocation.getWorld().spawnFallingBlock(sandLocation, falling.get(nextFall), (byte) 0x0);
    }

    private boolean invalid(Material material) {
        switch (material) {
            case PISTON_EXTENSION:
            case PISTON_MOVING_PIECE:
            case ENDER_PORTAL:
            case PORTAL:
                return true;
        }
        return false;
    }

    private void addResult(SandState state) {
        Material test = materials[nextMaterial]; // Current test
        if (!results.containsKey(state)) results.put(state, new ArrayList<Material>());
        results.get(state).add(test);
    }

    @EventHandler
    public void onChange(EntityChangeBlockEvent event) {
        if (!enabled) return;
        if (event.getEntity() instanceof FallingBlock) {
            double distanceThreshold = 3 * 3; // 3 blocks
            if (event.getEntity().getLocation().distanceSquared(blockLocation) <= distanceThreshold) {
                if (event.getTo() != null && event.getTo() == falling.get(nextFall)) {
                    Location spawned = event.getBlock().getLocation();
                    SandState state = null;
                    if (spawned.getBlockY() == blockLocation.getBlockY()) {
                        state = SandState.DISSOLVE;
                    } else if (spawned.getBlockY() == blockLocation.getBlockY() + 1) {
                        state = SandState.STOP;
                    } else {
                        plugin.getServer().broadcastMessage(ChatColor.DARK_RED + "WARNING: UNKNOWN STOP LOCATION!");
                    }
                    if (state != null) addResult(state);
                    plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            spawnNextSand();
                        }
                    }, 20L); // 1 second
                } else event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItem(ItemSpawnEvent event) {
        if (!enabled) return;
        double distanceThreshold = 3 * 3; // 3 blocks
        if (event.getEntity().getLocation().distanceSquared(blockLocation) <= distanceThreshold) {
            if (event.getEntity().getItemStack().getType() == falling.get(nextFall)) {
                addResult(SandState.BREAK);
                plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        spawnNextSand();
                    }
                }, 20L); // 1 second
            } else event.setCancelled(true);
        }
    }

}
