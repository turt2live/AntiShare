package com.turt2live.antishare.bukkit;

import com.turt2live.antishare.bukkit.commands.CommandHandler;
import com.turt2live.antishare.bukkit.commands.command.ToolsCommand;
import com.turt2live.antishare.bukkit.groups.BukkitGroupManager;
import com.turt2live.antishare.bukkit.lang.Lang;
import com.turt2live.antishare.bukkit.listener.EngineListener;
import com.turt2live.antishare.bukkit.listener.ToolListener;
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
                    materialProvider.insertPlayerFriendly(material, (short) -1, playerName);
                } catch (NumberFormatException e) {
                }
            }
            reader.close();
        } catch (IOException e) {
            getLogger().warning("Could not load internal item_lang.csv, you may have weird errors");
        }
    }

    @Override
    public void onDisable() {
        // Save everything
        Engine.getInstance().prepareShutdown();

        // Cleanup
        getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public void onEnable() {
        // Setup configuration
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(getResource("config.yml"));
        getConfig().setDefaults(configuration);
        saveDefaultConfig();

        // Setup AntiShare events
        EventDispatcher.register(this);

        // Start lang
        Lang.getInstance();

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
        Engine.getInstance().setPhysicsSettings(getConfig().getBoolean("blocks.physics.grow-with-gamemode", true), getConfig().getBoolean("blocks.physics.block-item-drop", true));
        Engine.getInstance().setAttachmentSettings(getConfig().getBoolean("blocks.attachments.break-as-placed", true), getConfig().getBoolean("blocks.attachments.deny-break", false));
        Engine.getInstance().setHoppersDenyMixed(getConfig().getBoolean("blocks.hoppers.deny-mixed", true));

        // Setup listeners
        getServer().getPluginManager().registerEvents(new EngineListener(), this);
        getServer().getPluginManager().registerEvents(new ToolListener(), this);

        // Setup commands
        CommandHandler handler = new CommandHandler();
        getCommand("antishare").setExecutor(handler);

        // Register commands
        handler.registerCommand(new ToolsCommand());

        // Probe all currently loaded worlds
        for (World world : getServer().getWorlds()) {
            Engine.getInstance().createWorldEngine(world.getName());
        }
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
     * Gets the block size this AntiShare plugin is using
     *
     * @return the block size
     */
    public int getBlockSize() {
        return blockSize;
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
