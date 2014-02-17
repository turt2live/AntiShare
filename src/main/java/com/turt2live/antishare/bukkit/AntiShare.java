package com.turt2live.antishare.bukkit;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.io.flatfile.FileBlockManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * The AntiShare Bukkit Plugin main class
 *
 * @author turt2live
 */
public class AntiShare extends JavaPlugin {

    @Override
    public void onEnable() {
        // Prepare the engine
        Engine engine = Engine.getInstance();

        // TODO: Per-world block stores and a proper location
        File storeLocation = new File(getDataFolder(), "data");
        if (!storeLocation.exists()) storeLocation.mkdirs();

        engine.setBlockManager(new FileBlockManager(2048, storeLocation)); // TODO: Configuration

        for (ASGameMode gameMode : ASGameMode.values()) {
            engine.setTrackedBlocks(gameMode, new DummyBlockTracker());
        }

        // Start the engine
        engine.getBlockManager().loadAll();

        // Setup listeners
        getServer().getPluginManager().registerEvents(new EngineListener(), this);
    }

    @Override
    public void onDisable() {
        // Save everything
        Engine.getInstance().prepareShutdown();
    }
}
