package com.turt2live.antishare.bukkit;

import com.turt2live.antishare.bukkit.command.ASCommandHandler;
import com.turt2live.antishare.bukkit.inventory.MaterialProvider;
import com.turt2live.antishare.bukkit.inventory.VaultMaterialProvider;
import com.turt2live.antishare.bukkit.lang.Lang;
import com.turt2live.antishare.bukkit.listener.EngineListener;
import com.turt2live.antishare.bukkit.listener.ToolListener;
import com.turt2live.antishare.collections.ArrayArrayList;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.engine.WorldEngine;
import com.turt2live.antishare.io.flatfile.FileBlockManager;
import com.turt2live.antishare.utils.ASGameMode;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * The AntiShare Bukkit Plugin main class
 *
 * @author turt2live
 */
public class AntiShare extends JavaPlugin implements com.turt2live.antishare.engine.EngineListener {

    private static AntiShare instance;
    private File dataFolder;
    private int blockSize;
    private MaterialProvider materialProvider = new MaterialProvider();

    @Override
    public void onLoad() {
        instance = this;

        dataFolder = new File(getDataFolder(), "data");
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    @Override
    public void onEnable() {
        // Setup configuration
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(getResource("config.yml"));
        getConfig().setDefaults(configuration);
        saveDefaultConfig();

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
        Engine.getInstance().addListener(this);
        Engine.getInstance().setLogger(this.getLogger());
        Engine.getInstance().setCacheMaximum(cacheMax);
        Engine.getInstance().setCacheIncrement(cacheInterval);
        Engine.getInstance().setSaveInterval(periodicSave);

        // Setup listeners
        getServer().getPluginManager().registerEvents(new EngineListener(), this);
        getServer().getPluginManager().registerEvents(new ToolListener(), this);

        // Setup commands
        getCommand("antishare").setExecutor(new ASCommandHandler(this));

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

    @Override
    public void onDisable() {
        // Save everything
        Engine.getInstance().prepareShutdown();

        // Cleanup
        getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public void onWorldEngineCreate(WorldEngine engine) {
        File storeLocation = new File(dataFolder, engine.getWorldName());

        if (!storeLocation.exists()) storeLocation.mkdirs();

        getLogger().info("Indexing '" + engine.getWorldName() + "'...");
        engine.setBlockManager(new FileBlockManager(blockSize, storeLocation));

        FileConfiguration configuration = createBlockConfig();

        for (ASGameMode gameMode : ASGameMode.values()) {
            List<String> values = configuration.getStringList(gameMode.name().toLowerCase());
            if (values == null) values = new ArrayArrayList<String>(new String[]{"none"});

            BlockListGenerator listing = BlockListGenerator.fromList(values, engine.getWorldName());
            engine.setTrackedBlocks(gameMode, listing);
        }
    }

    private FileConfiguration createBlockConfig() {
        File toFile = new File(AntiShare.getInstance().getDataFolder(), "blocks.yml");

        FileConfiguration config = YamlConfiguration.loadConfiguration(toFile);
        FileConfiguration defaults = YamlConfiguration.loadConfiguration(AntiShare.getInstance().getResource("blocks.yml"));
        config.setDefaults(defaults);
        config.options().copyDefaults(true);

        try {
            config.save(toFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;
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
