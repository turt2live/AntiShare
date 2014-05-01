package com.turt2live.antishare.engine;

import com.turt2live.antishare.configuration.groups.GroupManager;
import com.turt2live.antishare.economy.ASEconomy;
import com.turt2live.antishare.events.EventDispatcher;
import com.turt2live.antishare.events.engine.EngineShutdownEvent;
import com.turt2live.antishare.events.worldengine.WorldEngineCreateEvent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

/**
 * Represents the AntiShare engine
 *
 * @author turt2live
 */
public final class Engine {

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

    private static Engine instance;

    private long saveInterval = DEFAULT_SAVE_INTERVAL;
    private long cacheMaximum = DEFAULT_CACHE_MAXIMUM;
    private long cacheIncrement = DEFAULT_CACHE_INCREMENT;
    private ConcurrentMap<String, WorldEngine> engines = new ConcurrentHashMap<String, WorldEngine>();
    private Timer cacheTimer, saveTimer;
    private Logger logger = Logger.getLogger(getClass().getName());
    private ASEconomy economy;
    private GroupManager groupManager = null;

    private Engine() {
        newCacheTimer();
        newSaveTimer();
        setCacheIncrement(cacheIncrement);
        setSaveInterval(saveInterval);
    }

    /**
     * @deprecated For use by tests only
     */
    @Deprecated
    void forceNotInitialized() {
        groupManager = null;
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

        return true;
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

        this.groupManager = manager;
        this.groupManager.loadAll();
    }

    /**
     * Gets the economy engine for this engine. If economy support is
     * not configured or not enabled, this will return null
     *
     * @return the economy instance, or null for none
     */
    public ASEconomy getEconomy() {
        if (!isReady()) throw new EngineNotInitializedException();

        return economy;
    }

    /**
     * Sets the economy instance to use for this enngine. If null, it is assumed
     * the behaviour of "no economy" is applicable.
     *
     * @param economy the economy, may be null
     */
    public void setEconomy(ASEconomy economy) {
        this.economy = economy;
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

        this.logger = logger;
    }

    /**
     * Gets the engine for the specified world. If none exists, a new WorldEngine is
     * created and registered.
     *
     * @param world the world to lookup, cannot be null
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
     * @return the world engine
     */
    public WorldEngine createWorldEngine(String world) {
        if (!isReady()) throw new EngineNotInitializedException();
        if (world == null) throw new IllegalArgumentException("world cannot be null");
        if (engines.containsKey(world)) return engines.get(world);

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
