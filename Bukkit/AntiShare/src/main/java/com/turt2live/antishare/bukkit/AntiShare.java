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

package com.turt2live.antishare.bukkit;

import com.turt2live.antishare.bukkit.commands.CommandHandler;
import com.turt2live.antishare.bukkit.commands.command.ReloadCommand;
import com.turt2live.antishare.bukkit.commands.command.ToolsCommand;
import com.turt2live.antishare.bukkit.configuration.BukkitConfiguration;
import com.turt2live.antishare.bukkit.groups.BukkitGroupManager;
import com.turt2live.antishare.bukkit.lang.Lang;
import com.turt2live.antishare.bukkit.listener.EngineListener;
import com.turt2live.antishare.bukkit.listener.ToolListener;
import com.turt2live.antishare.engine.DevEngine;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.engine.WorldEngine;
import com.turt2live.antishare.events.EventDispatcher;
import com.turt2live.antishare.events.EventListener;
import com.turt2live.antishare.events.worldengine.WorldEngineCreateEvent;
import com.turt2live.antishare.io.flatfile.FileBlockManager;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The AntiShare Bukkit Plugin main class
 *
 * @author turt2live
 */
public class AntiShare extends JavaPlugin {

    /**
     * Color char identifier
     */
    public static final char COLOR_REPLACE_CHAR = '&';

    // The number of reloads recorded
    private static int RELOADS = 0;

    private static AntiShare instance;
    private File dataFolder;
    private int blockSize;
    private MaterialProvider materialProvider = new MaterialProvider();

    @Override
    public void onLoad() {
        instance = this;

        dataFolder = new File(getDataFolder(), "data");
        if (!dataFolder.exists()) dataFolder.mkdirs();

        // Load material defaults
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getResource("item_aliases.csv")));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#")) continue;

                String[] parts = line.split(",");
                if (parts.length != 2) continue;

                String name = parts[0].trim();
                String material = parts[1].trim();

                Material mat = Material.matchMaterial(material);
                if (mat == null) continue;

                materialProvider.insertAlias(name, mat);
            }
            reader.close();
        } catch (IOException e) {
            getLogger().warning("Could not load internal item_aliases.csv, you may have weird errors");
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getResource("item_lang.csv")));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#")) continue;

                String[] parts = line.split(",", 2);

                String idCombo = parts[0];
                String playerName = parts[1];

                parts = idCombo.split(":", 2);
                String materialName = parts[0];
                String dataName = parts[1];

                try {
                    short data = Short.parseShort(dataName);
                    Material material = Material.matchMaterial(materialName);

                    if (material == null) continue;

                    materialProvider.insertPlayerFriendly(material, data, playerName);
                    if (data == 0) materialProvider.insertPlayerFriendly(material, (short) -1, playerName);
                } catch (NumberFormatException e) {
                }
            }
            reader.close();
        } catch (IOException e) {
            getLogger().warning("Could not load internal item_lang.csv, you may have weird errors");
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getResource("item_similars.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#")) continue;
                materialProvider.insertExtra(line);
            }
            reader.close();
        } catch (IOException e) {
            getLogger().warning("Could not load internal item_similars.txt, you may have weird errors");
        }
    }

    @Override
    public void onDisable() {
        // Save everything
        Engine.getInstance().prepareShutdown();

        // Cleanup
        getServer().getScheduler().cancelTasks(this);
        EventDispatcher.deregister(this);

        // Shutdown DevEngine
        if (DevEngine.isEnabled()) {
            getLogger().info("DevEngine shutdown");
            DevEngine.setEnabled(false);
        }
    }

    @Override
    public void onEnable() {
        instance = this; // For reload support

        // Setup configuration
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(getResource("config.yml"));
        getConfig().setDefaults(configuration);
        saveDefaultConfig();

        // Setup AntiShare events
        EventDispatcher.register(this);

        // Start lang
        Lang.getInstance();

        // Start the engines
        initEngine();

        // Setup listeners
        getServer().getPluginManager().registerEvents(new EngineListener(), this);
        getServer().getPluginManager().registerEvents(new ToolListener(), this);

        // Setup commands
        CommandHandler handler = new CommandHandler();
        getCommand("antishare").setExecutor(handler);

        // Register commands
        handler.registerCommand(new ToolsCommand());
        handler.registerCommand(new ReloadCommand());

        // Check for developer tools
        if (getServer().getPluginManager().getPlugin("AntiShare-DevTools") != null) {
            getLogger().warning("============= ANTISHARE =============");
            getLogger().warning("   -- DEVELOPMENT TOOLS FOUND --");
            getLogger().warning("  **** Enabling Debug Support ****");
            getLogger().warning("============= ANTISHARE =============");
            DevEngine.setEnabled(true);
            DevEngine.setLogDirectory(new File(getDataFolder(), "devlogs"));
        }
    }

    private void initEngine() {
        // Load engine variables
        blockSize = getConfig().getInt("caching.block-size", 256);
        long cacheMax = getConfig().getLong("caching.cache-expiration", 120000);
        long cacheInterval = getConfig().getLong("caching.cache-timer-interval", 60000);
        long periodicSave = getConfig().getLong("caching.periodic-save", 0);

        // Validate engine variables
        if (blockSize <= 0) blockSize = 256;
        if (cacheMax <= 0) cacheMax = 120000;
        if (cacheInterval <= 0) cacheInterval = 60000;

        // Setup engine
        Engine.getInstance().setLogger(this.getLogger());
        Engine.getInstance().setCacheMaximum(cacheMax);
        Engine.getInstance().setCacheIncrement(cacheInterval);
        Engine.getInstance().setSaveInterval(periodicSave);
        Engine.getInstance().setGroupManager(new BukkitGroupManager());
        Engine.getInstance().setConfiguration(new BukkitConfiguration(new File(getDataFolder(), "config.yml")));

        // Probe all currently loaded worlds
        for (World world : getServer().getWorlds()) {
            Engine.getInstance().createWorldEngine(world.getName());
        }
    }

    /**
     * Reloads the AntiShare plugin, saving all data before re-enabling all services.
     *
     * @return returns the number of counted reloads
     */
    public int reloadPlugin() {
        getLogger().info("Reloading plugin...");
        DevEngine.log("[Bukkit Plugin] Reload issued. Reloads until now: " + RELOADS);

        // Restart the engine
        DevEngine.log("[Bukkit Plugin] Reloading engine...");
        Engine.getInstance().prepareShutdown();
        initEngine();

        // Restart language
        DevEngine.log("[Bukkit Plugin] Reloading language...");
        Lang.getInstance().reload();

        RELOADS++;
        DevEngine.log("[Bukkit Plugin] Reload completed. Reloads until now: " + RELOADS);

        return RELOADS;
    }

    @EventListener
    public void onWorldEngineCreate(WorldEngineCreateEvent event) {
        WorldEngine engine = event.getEngine();

        File storeLocation = new File(dataFolder, engine.getWorldName());
        if (!storeLocation.exists()) storeLocation.mkdirs();

        getLogger().info("Indexing '" + engine.getWorldName() + "'...");
        engine.setBlockManager(new FileBlockManager(blockSize, storeLocation));
    }

    /**
     * Gets the active material provider for this plugin instance
     *
     * @return the material provider
     */
    public MaterialProvider getMaterialProvider() {
        return materialProvider;
    }

    /**
     * Gets the AntiShare instance
     *
     * @return the AntiShare instance
     */
    public static AntiShare getInstance() {
        return instance;
    }
}
