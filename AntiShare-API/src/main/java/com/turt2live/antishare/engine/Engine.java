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

package com.turt2live.antishare.engine;

import com.turt2live.antishare.configuration.Configuration;
import com.turt2live.antishare.configuration.InventoryMergeSettings;
import com.turt2live.antishare.configuration.MemoryConfiguration;
import com.turt2live.antishare.configuration.groups.GroupManager;
import com.turt2live.antishare.events.EventDispatcher;
import com.turt2live.antishare.events.engine.EngineShutdownEvent;
import com.turt2live.antishare.events.worldengine.WorldEngineCreateEvent;
import com.turt2live.antishare.io.InventoryManager;
import com.turt2live.antishare.io.memory.MemoryInventoryManager;
import com.turt2live.antishare.object.AInventory;
import com.turt2live.antishare.object.APlayer;
import com.turt2live.antishare.object.AWorld;
import com.turt2live.antishare.object.pattern.PatternManager;
import com.turt2live.lib.items.AbstractedItem;
import com.turt2live.lib.items.provider.ItemProvider;
import com.turt2live.lib.items.provider.ProviderManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

/**
 * Represents the AntiShare engine
 *
 * @author turt2live
 */
public final class Engine {

    // TODO: Cleanup configuration system

    /**
     * The default cache increment (60 seconds)
     */
    public static final long DEFAULT_CACHE_INCREMENT = 60000; // 60 seconds
    /**
     * The default cache maximum time (120 seconds)
     */
    public static final long DEFAULT_CACHE_MAXIMUM = 120000; // 120 seconds
    /**
     * The default save interval (0, off)
     */
    public static final long DEFAULT_SAVE_INTERVAL = 0; // Default no save
    /**
     * Configuration key for 'break attachments as placed'
     */
    public static final String CONFIG_BREAK_ATTACHMENTS_AS_PLACED = "blocks.attachemnts.break-as-placed";
    /**
     * Configuration key for 'deny break if attachment type mismatch'
     */
    public static final String CONFIG_MISMATCHED_ATTACHMENTS_DENY = "blocks.attachments.deny-break";
    /**
     * Configuration key for 'block spread with gamemode'
     */
    public static final String CONFIG_PHYSICS_GROW_WITH_GAMEMODE = "blocks.physics.grow-with-gamemode";
    /**
     * Configuration key for 'random break as gamemode'
     */
    public static final String CONFIG_PHYSICS_BREAK_AS_GAMEMODE = "blocks.physics.block-item-drop";
    /**
     * Configuration key for 'hopper mismatch type transfer'
     */
    public static final String CONFIG_HOPPER_MISMATCH_INTERACTION = "blocks.hoppers.deny-mixed";
    /**
     * Configuration key for 'deny pistons if lineup mismatch'
     */
    public static final String CONFIG_PISTON_MISMATCH = "blocks.pistons.deny-mismatch";
    /**
     * Configuration key for 'classic mode interaction' handling
     */
    public static final String CONFIG_INTERACT_CLASSIC_MODE = "blocks.interaction.classic-mode";
    /**
     * Configuration key for 'if not classic mode, can creative players open natural containers?'
     * <p/>
     * This will be 'true' to ALLOW the interaction.
     */
    public static final String CONFIG_INTERACT_NATURAL_CONTAINERS = "blocks.interaction.creative-natural-containers";
    /**
     * Configuration key for 'if not classic mode, will containers inherit gamemodes from players?'
     */
    public static final String CONFIG_INTERACT_CONTAINER_INHERIT = "blocks.interaction.natural-container-absorb-gamemode";
    /**
     * Configuration key for 'can players attack cross gamemode?'
     */
    public static final String CONFIG_ENTITIES_CROSS_GAMEMODE_ATTACK = "blocks.entities.cross-gamemode-attack";

    private static Engine instance;

    private long saveInterval = DEFAULT_SAVE_INTERVAL;
    private long cacheMaximum = DEFAULT_CACHE_MAXIMUM;
    private long cacheIncrement = DEFAULT_CACHE_INCREMENT;
    private ConcurrentMap<String, WorldEngine> engines = new ConcurrentHashMap<>();
    private Timer cacheTimer, saveTimer;
    private Logger logger = Logger.getLogger(getClass().getName());
    private GroupManager groupManager = null;
    private Configuration configuration = new MemoryConfiguration();
    private PatternManager patterns = new PatternManager();
    private InventoryManager inventoryManager = new MemoryInventoryManager();
    private ItemProvider itemProvider = null;
    private WorldProvider worlds = null;
    private InventoryMergeSettings mergeSettings = new InventoryMergeSettings(configuration);

    private Engine() {
        newCacheTimer();
        newSaveTimer();
        setCacheIncrement(cacheIncrement);
        setSaveInterval(saveInterval);

        // Setup the item provider base package
        String packName = AbstractedItem.class.getPackage().getName();
        ProviderManager.setBasePackage(packName);
    }

    /**
     * @deprecated For use by tests only
     */
    @Deprecated
    void forceNotInitialized() {
        groupManager = null;
        worlds = null;
    }

    /**
     * Determines if this engine is ready or not. If this engine
     * is not ready, {@link com.turt2live.antishare.engine.EngineNotInitializedException}
     * may be thrown from various methods.
     *
     * @return true if ready, false otherwise.
     */
    public boolean isReady() {
        if (groupManager == null) return false;
        return worlds != null;

    }

    /**
     * Gets the world provider.
     *
     * @return the world provider
     */
    public WorldProvider getWorldProvider() {
        if (!isReady()) throw new EngineNotInitializedException();

        return worlds;
    }

    /**
     * Sets the world provider.
     *
     * @param provider the world provider, cannot be null
     */
    public void setWorldProvider(WorldProvider provider) {
        if (provider == null) throw new IllegalArgumentException();

        DevEngine.log("[Engine] New world provider: " + (provider.getClass().getName()));

        this.worlds = provider;
    }

    /**
     * Gets a world by name. If the engine is not ready (does not have a
     * world provider), then this will throw an exception.
     *
     * @param name the world name to lookup, cannot be null
     *
     * @return the world found, or null if none
     */
    public AWorld getWorld(String name) {
        if (!isReady()) throw new EngineNotInitializedException();
        if (name == null) throw new IllegalArgumentException();

        return worlds.getWorld(name);
    }

    /**
     * Gets the current inventory manager
     *
     * @return the current inventory manager
     */
    public InventoryManager getInventoryManager() {
        if (!isReady()) throw new EngineNotInitializedException();

        return inventoryManager;
    }

    /**
     * Sets the inventory manager to use in this engine
     *
     * @param manager the new manager, cannot be null
     */
    public void setInventoryManager(InventoryManager manager) {
        if (manager == null) throw new IllegalArgumentException();

        DevEngine.log("[Engine] New inventory manager: " + manager.getClass().getName());

        this.inventoryManager = manager;
    }

    /**
     * Gets the item provider for this engine.
     *
     * @return the item provider, may be null if not initialized
     */
    public ItemProvider getItemProvider() {
        if (!isReady()) throw new EngineNotInitializedException();

        return itemProvider;
    }

    /**
     * Loads the item provider for use. If no provider can be found, an exception is raised.
     *
     * @throws java.lang.IllegalArgumentException thrown if the stream yields an invalid provider
     */
    public void loadItemProvider() {
        DevEngine.log("[Engine] Attempting to load item provider ");
        ProviderManager providerManager = ProviderManager.getInstance();

        List<ItemProvider> providers = providerManager.getProviders();
        DevEngine.log("[Engine] There are " + providers.size() + " possible item providers.");
        for (ItemProvider provider : providers) {
            DevEngine.log("[Engine] Loaded item provider: " + provider.getClass().getName());
        }

        ItemProvider chosen = providerManager.getProvider();
        if (chosen == null) throw new IllegalArgumentException("No provider loaded");

        DevEngine.log("[Engine] New item provider: " + chosen.getClass().getName());
        itemProvider = chosen;
    }

    /**
     * Gets the group manager instance for this engine.
     *
     * @return the group manager
     */
    public GroupManager getGroupManager() {
        if (!isReady()) throw new EngineNotInitializedException();

        return groupManager;
    }

    /**
     * Sets the new group manager for this engine to use. This will internally call
     * {@link com.turt2live.antishare.configuration.groups.GroupManager#loadAll()}.
     *
     * @param manager the new group manager, cannot be null
     */
    public void setGroupManager(GroupManager manager) {
        if (manager == null) throw new IllegalArgumentException("group manager cannot be null");

        DevEngine.log("[Engine] New group manager: " + manager.getClass().getName());

        this.groupManager = manager;
        this.groupManager.loadAll();
    }

    /**
     * Gets the logger for this engine
     *
     * @return the logger
     */
    public Logger getLogger() {
        if (!isReady()) throw new EngineNotInitializedException();

        return logger;
    }

    /**
     * Sets the new logger for this engine to use
     *
     * @param logger the new logger, cannot be null
     */
    public void setLogger(Logger logger) {
        if (logger == null) throw new IllegalArgumentException("logger may not be null");

        DevEngine.log("[Engine] New logger: " + logger.getClass().getName());

        this.logger = logger;
    }

    /**
     * Gets the pattern manager for this Engine instance
     *
     * @return the patterns
     */
    public PatternManager getPatterns() {
        return patterns;
    }

    /**
     * Gets the engine for the specified world. If none exists, a new WorldEngine is
     * created and registered.
     *
     * @param world the world to lookup, cannot be null
     *
     * @return the world engine
     */
    public WorldEngine getEngine(String world) {
        if (!isReady()) throw new EngineNotInitializedException();
        if (world == null) throw new IllegalArgumentException("world cannot be null");

        WorldEngine engine = engines.get(world);
        if (engine == null) engine = createWorldEngine(world);
        return engine;
    }

    /**
     * Creates a world engine for the supplied world. If the world engine already exists,
     * the existing world engine is created.
     *
     * @param world the world to create an engine for
     *
     * @return the world engine
     */
    public WorldEngine createWorldEngine(String world) {
        if (!isReady()) throw new EngineNotInitializedException();
        if (world == null) throw new IllegalArgumentException("world cannot be null");
        if (engines.containsKey(world)) return engines.get(world);

        DevEngine.log("[Engine] Creating world engine for '" + world + "'...");

        WorldEngine engine = new WorldEngine(world);
        engines.put(world, engine);

        EventDispatcher.dispatch(new WorldEngineCreateEvent(engine));

        return engine;
    }

    /**
     * Unloads a world engine from the core engine. If the passed world is
     * null or not found, this will do nothing.
     *
     * @param world the world to unload
     */
    public void unloadWorldEngine(String world) {
        if (!isReady()) throw new EngineNotInitializedException();
        if (world != null) {
            WorldEngine engine = engines.get(world);
            if (engine != null) {
                DevEngine.log("[Engine] Unloading world engine for '" + world + "'...");
                engine.prepareShutdown();
                engines.remove(world);
            }
        }
    }

    /**
     * Prepares the engine for shutdown. This will save all world engines, cancel the
     * cache timer, and revoke all listeners.
     */
    public void prepareShutdown() {
        EventDispatcher.dispatch(new EngineShutdownEvent());

        DevEngine.log("[Engine] Shutting down");

        newCacheTimer(); // Cancels internally, resetting the timer to no task
        newSaveTimer(); // Cancels internally, resetting the timer to no task
        for (WorldEngine engine : engines.values())
            engine.prepareShutdown();

        engines.clear();
    }

    /**
     * Gets the maximum time the cache is permitted to hold an object
     *
     * @return the maximum cache time, in milliseconds
     */
    public long getCacheMaximum() {
        if (!isReady()) throw new EngineNotInitializedException();

        return cacheMaximum;
    }

    /**
     * Sets the cache maximum. The value is a millisecond value for how long an object
     * may remain stale before being removed
     *
     * @param cacheMaximum the new cache maximum, cannot be less than or equal to zero
     */
    public void setCacheMaximum(long cacheMaximum) {
        if (cacheMaximum <= 0) throw new IllegalArgumentException("maximum cannot be less than or equal to zero");

        DevEngine.log("[Engine] New cache maximum: " + cacheMaximum);

        this.cacheMaximum = cacheMaximum;
    }

    /**
     * Gets the number of milliseconds it takes for the cache timer to tick
     *
     * @return the milliseconds for a tick
     */
    public long getCacheIncrement() {
        if (!isReady()) throw new EngineNotInitializedException();

        return cacheIncrement;
    }

    /**
     * Sets the new cache increment. This is a millisecond value for how often a cache
     * cleanup check is issued. Once this is called with a valid value, the cache timer
     * is rescheduled to occur immediately and will have a period equal to the value
     * passed.
     *
     * @param cacheIncrement the new increment, cannot be less than or equal to zero
     */
    public void setCacheIncrement(long cacheIncrement) {
        if (cacheIncrement <= 0)
            throw new IllegalArgumentException("cache increment must not be less than or equal to zero");

        DevEngine.log("[Engine] New cache increment: " + cacheIncrement);

        this.cacheIncrement = cacheIncrement;
        newCacheTimer();
        cacheTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (WorldEngine engine : engines.values()) {
                    engine.getBlockManager().cleanup();
                }
            }
        }, 0L, cacheIncrement);
    }

    /**
     * Gets the save interval for the periodic save function. If the returned value
     * is less than or equal to zero, the periodic save function is disabled and not
     * operating. Any other positive value is used to indicate the period by which
     * the engine triggers a save.
     *
     * @return the save interval
     */
    public long getSaveInterval() {
        if (!isReady()) throw new EngineNotInitializedException();

        return saveInterval;
    }

    /**
     * Sets the new save interval. This is a millisecond value for how often the engine
     * should periodically save data in the subsequent world engines and itself. Values
     * less than or equal to zero are considered to be "do not save periodically" and
     * strictly follow that behaviour. Once called with a value that will trigger a
     * periodic save, the timer will save immediately and fire every interval until
     * cancelled.
     *
     * @param saveInterval the new save interval
     */
    public void setSaveInterval(long saveInterval) {
        DevEngine.log("[Engine] New save interval: " + saveInterval);

        this.saveInterval = saveInterval;
        newSaveTimer();
        if (saveInterval > 0) {
            saveTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    for (WorldEngine engine : engines.values()) {
                        engine.getBlockManager().saveAll();
                    }
                }
            }, 0, saveInterval);
        }
    }

    /**
     * Gets the active Engine configuration
     *
     * @return the engine configuration
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Sets the configuration for this Engine to use. This will call load()
     * internally.
     *
     * @param configuration the configuration to use, cannot be null
     */
    public void setConfiguration(Configuration configuration) {
        if (configuration == null) throw new IllegalArgumentException();

        this.configuration = configuration;
        configuration.load();

        mergeSettings = new InventoryMergeSettings(configuration);
    }

    /**
     * Gets the applicable inventory merge settings for this engine
     *
     * @return the inventory merge settings
     */
    public InventoryMergeSettings getMergeSettings() {
        return mergeSettings;
    }

    /**
     * Gets a particular flag setting from the internal configuration of this
     * engine. If the key is not found, the default is returned.
     *
     * @param configKey the configuration key to lookup, cannot be null
     * @param def       the default to use if not found
     *
     * @return the flag or the default setting
     */
    public boolean getFlag(String configKey, boolean def) {
        return configuration.getBoolean(configKey, def); // Does it's own null check
    }

    /**
     * Processes a player changing worlds. This will perform any actions required
     * to keep a proper player state.
     *
     * @param player the player changing worlds, cannot be null
     * @param from   the world the player is travelling from, cannot be null
     * @param to     the world the player is travelling to, cannot be null
     */
    // TODO: Unit test
    public void processWorldChange(APlayer player, AWorld from, AWorld to) {
        if (player == null || from == null || to == null) throw new IllegalArgumentException();

        // TODO: Permission check

        DevEngine.log("[Engine] Processing player world change",
                "[Engine] \t\tplayer = " + player,
                "[Engine] \t\tfrom = " + from,
                "[Engine] \t\tto = " + to);

        AInventory inventory = player.getInventory();
        inventory.setWorld(from);
        List<AInventory> resulting = processInventoryMerge(inventory, player.getUUID());
        for (AInventory i : resulting) getInventoryManager().setInventory(player.getUUID(), i);

        player.setInventory(getInventoryManager().getInventory(player.getUUID(), player.getGameMode(), to));
    }

    /**
     * Processes a player joining the server. This will perform any actions that
     * need to occur once the player joins the server (such as loading data or
     * updating the player's state).
     *
     * @param player the player joining the server, cannot be null
     */
    // TODO: Unit test
    public void processPlayerJoin(APlayer player) {
        if (player == null) throw new IllegalArgumentException();

        // TODO: Handle case of players not having an initial inventory

        DevEngine.log("[Engine] Processing player join",
                "[Engine] \t\tplayer = " + player);

        // We need to find the inventory for their current world/gamemode and set them
        AInventory inventory = getInventoryManager().getInventory(player.getUUID(), player.getGameMode(), player.getWorld());
        player.setInventory(inventory);
    }

    /**
     * Processes a player leaving the server for any reason. This will perform
     * the required cleanup calls as well as any final actions that need to occur
     * upon the player's exit.
     *
     * @param player the player leaving the server, cannot be null
     */
    // TODO: Unit test
    public void processPlayerQuit(APlayer player) {
        if (player == null) throw new IllegalArgumentException();

        DevEngine.log("[Engine] Processing player quit",
                "[Engine] \t\tplayer = " + player);

        List<AInventory> resulting = processInventoryMerge(player.getInventory(), player.getUUID());
        for (AInventory inventory : resulting) getInventoryManager().setInventory(player.getUUID(), inventory);
        getInventoryManager().save(player.getUUID());
    }

    /**
     * Processes an inventory merge based upon a newly created inventory. This will gather a collection
     * of applicable inventories for the player and merge the passed inventory into the collected inventories
     * where applicable. The returned set contains all the collected inventories (modified) as well as
     * the passed inventory.
     *
     * @param created the newly created inventory, cannot be null
     * @param player  the player that is applicable, cannot be null
     *
     * @return the resulting list of modified inventories, including the passed inventory
     */
    // TODO: Unit test
    List<AInventory> processInventoryMerge(AInventory created, UUID player) {
        if (created == null || player == null) throw new IllegalArgumentException();

        List<AInventory> others = getInventoryManager().getInventories(player);
        List<Integer> remove = new ArrayList<>();
        for (int i = 0; i < others.size(); i++) {
            AInventory val = others.get(i);
            if (val.getGameMode() == created.getGameMode() && val.getWorld().equals(created.getWorld())) {
                remove.add(i);
            }
        }

        for (int i : remove) others.remove(i);

        getMergeSettings().mergeInventories(created, others);
        others.add(created);

        return others;
    }

    private void newCacheTimer() {
        if (cacheTimer != null) cacheTimer.cancel();
        cacheTimer = new Timer();
    }

    private void newSaveTimer() {
        if (saveTimer != null) saveTimer.cancel();
        saveTimer = new Timer();
    }

    /**
     * Gets the engine instance
     *
     * @return the engine instance
     */
    public static Engine getInstance() {
        if (instance == null) instance = new Engine();
        return instance;
    }

}
