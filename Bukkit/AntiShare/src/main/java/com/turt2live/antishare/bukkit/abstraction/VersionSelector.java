package com.turt2live.antishare.bukkit.abstraction;

import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.engine.DevEngine;
import org.bukkit.event.Listener;

/**
 * Used for abstracting the Minecraft version
 *
 * @author turt2live
 */
public final class VersionSelector {

    private static final String DEFAULT_VERSION = "v1_7_R3";
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
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);

        DevEngine.log("[Abstraction] Parsed package name: " + version);

        // Get the last element of the package
        if (version.equals("craftbukkit")) { // If the last element of the package was "craftbukkit" we are now pre-refactor
            version = "pre";
        }

        DevEngine.log("[Abstraction] Attempting to load: " + version);

        MinecraftVersion mc = init(version);
        if (mc == null) {
            AntiShare.getInstance().getLogger().severe("Could not find support for this CraftBukkit version.");
            DevEngine.log("[Abstraction] Attempt 1 failed, trying to load: " + version);
            mc = init(DEFAULT_VERSION);
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
            AntiShare.getInstance().getServer().getPluginManager().registerEvents((Listener) minecraft, AntiShare.getInstance());
        }
    }

    private static MinecraftVersion init(String version) {
        try {
            String lookup = "com.turt2live.antishare.bukkit.abstraction." + version + ".Minecraft";
            final Class<?> clazz = Class.forName(lookup);

            DevEngine.log("[Abstraction] Lookup class: " + lookup);

            // Check if we have a NMSHandler class at that location.
            if (MinecraftVersion.class.isAssignableFrom(clazz)) { // Make sure it actually implements NMS
                return (MinecraftVersion) clazz.getConstructor().newInstance(); // Set our handler
            }
        } catch (final Exception e) {
        }
        return null;
    }

}
