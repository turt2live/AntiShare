package com.turt2live.antishare.bukkit;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.engine.WorldEngine;
import com.turt2live.antishare.io.flatfile.FileBlockManager;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * The AntiShare Bukkit Plugin main class
 *
 * @author turt2live
 */
public class AntiShare extends JavaPlugin implements com.turt2live.antishare.engine.EngineListener {

    private static AntiShare instance;
    private File dataFolder;
    private int blockSize;

    @Override
    public void onLoad() {
        instance = this;

        dataFolder = new File(getDataFolder(), "data");
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    @Override
    public void onEnable() {
        Engine.getInstance().addListener(this);
        Engine.getInstance().setLogger(this.getLogger());

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        getConfig().options().copyDefaults(false);

        // load engine variables
        blockSize = getConfig().getInt("caching.block-size", 256);
        long cacheMax = getConfig().getLong("caching.cache-expiration", 120000);
        long cacheInterval = getConfig().getLong("caching.cache-timer-interval", 60000);
        long periodicSave = getConfig().getLong("caching.periodic-save", 0);

        // Validate engine variables
        if (blockSize <= 0) blockSize = 256;
        if (cacheMax <= 0) cacheMax = 120000;
        if (cacheInterval <= 0) cacheInterval = 60000;

        // Setup engine
        Engine.getInstance().setCacheMaximum(cacheMax);
        Engine.getInstance().setCacheIncrement(cacheInterval);
        Engine.getInstance().setSaveInterval(periodicSave);

        // Setup listeners
        getServer().getPluginManager().registerEvents(new EngineListener(), this);

        // Probe all currently loaded worlds
        for (World world : getServer().getWorlds()) {
            Engine.getInstance().createWorldEngine(world.getName());
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Saving data...");
        // Save everything
        Engine.getInstance().prepareShutdown();

        // Cleanup
        getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public void onWorldEngineCreate(WorldEngine engine) {
        File storeLocation = new File(dataFolder, engine.getWorldName());

        if (!storeLocation.exists()) storeLocation.mkdirs();

        engine.setBlockManager(new FileBlockManager(blockSize, storeLocation)); // TODO: Configuration (type)

        for (ASGameMode gameMode : ASGameMode.values()) {
            engine.setTrackedBlocks(gameMode, new DummyBlockTracker()); // TODO: Debug code
        }

        // Start the engine
        engine.getBlockManager().loadAll();
    }

    /**
     * Gets the block size this AntiShare plugin is using
     *
     * @return the block size
     */
    public int getBlockSize() {
        return blockSize;
    }

    @Override
    public void onEngineShutdown() {
        // Ignore this event
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
