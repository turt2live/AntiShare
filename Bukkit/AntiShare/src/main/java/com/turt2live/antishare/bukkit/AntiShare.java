package com.turt2live.antishare.bukkit;

import com.turt2live.antishare.bukkit.commands.CommandHandler;
import com.turt2live.antishare.bukkit.commands.command.ToolsCommand;
import com.turt2live.antishare.bukkit.groups.BukkitGroupManager;
import com.turt2live.antishare.bukkit.lang.Lang;
import com.turt2live.antishare.bukkit.listener.EngineListener;
import com.turt2live.antishare.bukkit.listener.ToolListener;
import com.turt2live.antishare.bukkit.listener.antishare.AlertListener;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.engine.WorldEngine;
import com.turt2live.antishare.events.EventDispatcher;
import com.turt2live.antishare.events.EventListener;
import com.turt2live.antishare.events.worldengine.WorldEngineCreateEvent;
import com.turt2live.antishare.io.flatfile.FileBlockManager;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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
    private AlertListener alerts;

    @Override
    public void onLoad() {
        instance = this;

        dataFolder = new File(getDataFolder(), "data");
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    @Override
    public void onDisable() {
        // Save everything
        Engine.getInstance().prepareShutdown();

        // Remove AntiShare listeners
        EventDispatcher.deregister(alerts);

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

        // Setup listeners
        getServer().getPluginManager().registerEvents(new EngineListener(), this);
        getServer().getPluginManager().registerEvents(new ToolListener(), this);

        // Setup AntiShare listeners
        alerts = new AlertListener();
        EventDispatcher.register(alerts);

        // Setup commands
        CommandHandler handler = new CommandHandler();
        getCommand("antishare").setExecutor(handler);

        // Register commands
        handler.registerCommand(new ToolsCommand());

        // Probe all currently loaded worlds
        for (World world : getServer().getWorlds()) {
            Engine.getInstance().createWorldEngine(world.getName());
        }

        // Load economy hook
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            Engine.getInstance().setEconomy(new VaultEconomy());
            materialProvider = new VaultMaterialProvider();
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
