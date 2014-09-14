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

package com.turt2live.antishare.bukkit.abstraction;

import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.engine.DevEngine;
import com.turt2live.antishare.lib.items.bukkit.BukkitProvider;
import com.turt2live.antishare.lib.items.bukkit.GlowstoneValues;
import org.bukkit.event.Listener;

/**
 * Used for abstracting the Minecraft version
 *
 * @author turt2live
 */
public final class VersionSelector {

    private static final String DEFAULT_VERSION = "v1_7_R4";
    private static MinecraftVersion minecraft;

    /**
     * Gets the minecraft version abstraction layer
     *
     * @return the minecraft version abstraction layer
     */
    public static MinecraftVersion getMinecraft() {
        if (minecraft == null) initMinecraft();
        return minecraft;
    }

    private static void initMinecraft() {
        String packageName = AntiShare.getInstance().getServer().getClass().getPackage().getName();
        // Get full package string of CraftServer.
        // org.bukkit.craftbukkit.versionstring (or for pre-refactor, just org.bukkit.craftbukkit
        // net.glowstone for glowstone servers
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);

        DevEngine.log("[Abstraction] Parsed package name: " + version);
        GlowstoneValues.ProviderType type = GlowstoneValues.ProviderType.BUKKIT;

        // Get the last element of the package
        if (version.equals("craftbukkit")) { // If the last element of the package was "craftbukkit" we are now pre-refactor
            version = "vpre";
        } else if (version.equals("glowstone")) {
            // Glowstone is built purely on the Bukkit API, so we can detect it's version through the version string
            // We can also abuse ItemAbstraction's glowstone handling for our purposes (considering it uses the same format)
            version = BukkitProvider.getGlowstoneMappings().getGlowstoneVersion(AntiShare.getInstance().getServer());
            type = BukkitProvider.getGlowstoneMappings().getProviderType(version);
        }

        DevEngine.log("[Abstraction] Attempting to load: " + version);

        MinecraftVersion mc = init(version, type);
        if (mc == null) {
            AntiShare.getInstance().getLogger().severe("Could not find support for this CraftBukkit version.");
            DevEngine.log("[Abstraction] Attempt 1 failed, trying to load: " + version);
            mc = init(DEFAULT_VERSION, GlowstoneValues.ProviderType.BUKKIT);
            version = DEFAULT_VERSION;
            if (mc == null) {
                DevEngine.log("[Abstraction] Failed to find support.");
                AntiShare.getInstance().getLogger().severe("[FAILURE] Could not find support for this CraftBukkit version.");
                AntiShare.getInstance().getServer().getPluginManager().disablePlugin(AntiShare.getInstance());
                return;
            }
        }
        minecraft = mc;
        AntiShare.getInstance().getLogger().info("Loading support for " + (version.equals("pre") ? "v1_4_5_pre" : version));

        // Register per-version events
        if (minecraft instanceof Listener) {
            AntiShare.getInstance().getServer().getPluginManager().registerEvents(minecraft, AntiShare.getInstance());
        }
    }

    private static MinecraftVersion init(String version, GlowstoneValues.ProviderType providerType) {
        String providerStr = providerType == null ? "bukkit" : providerType.name().toLowerCase();

        try {
            String lookup = "com.turt2live.antishare." + providerStr + ".abstraction." + version + ".Minecraft";
            final Class<?> clazz = Class.forName(lookup);

            DevEngine.log("[Abstraction] Lookup class: " + lookup);

            // Check if we have a NMSHandler class at that location.
            if (MinecraftVersion.class.isAssignableFrom(clazz)) { // Make sure it actually implements NMS
                return (MinecraftVersion) clazz.getConstructor().newInstance(); // Set our handler
            }
        } catch (Exception ignored) {
        }
        return null;
    }

}
