package com.turt2live.antishare.engine;

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

    private Engine() {
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
     * Prepares the engine for shutdown
     */
    public void prepareShutdown() {
        for (EngineListener listener : listeners)
            listener.onEngineShutdown();

        for (WorldEngine engine : engines.values())
            engine.prepareShutdown();
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
