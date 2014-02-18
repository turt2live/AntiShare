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

    @Override
    public void onLoad() {
        instance = this;

        dataFolder = new File(getDataFolder(), "data");

        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    @Override
    public void onEnable() {
        Engine.getInstance().addListener(this);

        // Setup listeners
        getServer().getPluginManager().registerEvents(new EngineListener(), this);

        // Probe all currently loaded worlds
        for (World world : getServer().getWorlds()) {
            Engine.getInstance().createWorldEngine(world.getName());
        }
    }

    @Override
    public void onDisable() {
        // Save everything
        Engine.getInstance().prepareShutdown();
    }

    @Override
    public void onWorldEngineCreate(WorldEngine engine) {
        File storeLocation = new File(dataFolder, engine.getWorldName());

        if (!storeLocation.exists()) storeLocation.mkdirs();

        engine.setBlockManager(new FileBlockManager(2048, storeLocation)); // TODO: Configuration

        for (ASGameMode gameMode : ASGameMode.values()) {
            engine.setTrackedBlocks(gameMode, new DummyBlockTracker()); // TODO: Debug code
        }

        // Start the engine
        engine.getBlockManager().loadAll();
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
