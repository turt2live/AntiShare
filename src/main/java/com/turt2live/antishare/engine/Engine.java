package com.turt2live.antishare.engine;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents the AntiShare engine
 *
 * @author turt2live
 */
public final class Engine {

    private static Engine instance;
    private CopyOnWriteArrayList<EngineListener> listeners = new CopyOnWriteArrayList<EngineListener>();
    private ConcurrentMap<String, WorldEngine> engines = new ConcurrentHashMap<String, WorldEngine>();
    private long cacheMaximum = 120000; // 120 seconds // TODO: Config
    private long cacheIncrement = 60000; // 60 seconds // TODO: Config
    private Timer cacheTimer;

    private Engine() {
        cacheTimer = new Timer();
        cacheTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (WorldEngine engine : engines.values()) {
                    engine.getBlockManager().cleanup();
                }
            }
        }, cacheIncrement, cacheIncrement);
    }

    /**
     * Adds a listener to this engine. If the listener is already registered, the
     * listener is not re-registered.
     *
     * @param listener the listener to register, cannot be null
     */
    public void addListener(EngineListener listener) {
        if (listener == null) throw new IllegalArgumentException("listener may not be null");

        if (!listeners.contains(listener)) listeners.add(listener);
    }

    /**
     * Removes a listener from the engine
     *
     * @param listener the listener to remove, cannot be null
     */
    public void removeListener(EngineListener listener) {
        if (listener == null) throw new IllegalArgumentException("listener may not be null");

        listeners.remove(listener);
    }

    /**
     * Gets the engine for the specified world. If none exists, a new WorldEngine is
     * created and registered.
     *
     * @param world the world to lookup, cannot be null
     * @return the world engine
     */
    public WorldEngine getEngine(String world) {
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
        if (world == null) throw new IllegalArgumentException("world cannot be null");
        if (engines.containsKey(world)) return engines.get(world);

        WorldEngine engine = new WorldEngine(world);
        engines.put(world, engine);

        for (EngineListener listener : listeners)
            listener.onWorldEngineCreate(engine);

        return engine;
    }

    /**
     * Unloads a world engine from the core engine. If the passed world is
     * null or not found, this will do nothing.
     *
     * @param world the world to unload
     */
    public void unloadWorldEngine(String world) {
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
        for (EngineListener listener : listeners)
            listener.onEngineShutdown();

        cacheTimer.cancel();
        for (WorldEngine engine : engines.values())
            engine.prepareShutdown();
        engines.clear();
        listeners.clear();
    }

    /**
     * Gets the maximum time the cache is permitted to hold an object
     *
     * @return the maximum cache time, in milliseconds
     */
    public long getCacheMaximum() {
        return cacheMaximum;
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
