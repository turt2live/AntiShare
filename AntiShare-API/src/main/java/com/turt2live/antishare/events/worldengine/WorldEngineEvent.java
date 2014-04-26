package com.turt2live.antishare.events.worldengine;

import com.turt2live.antishare.engine.WorldEngine;
import com.turt2live.antishare.events.Event;

/**
 * A generic WorldEngineEvent
 *
 * @author turt2live
 */
public abstract class WorldEngineEvent implements Event {

    private WorldEngine engine;

    /**
     * Creates a new WorldEngineEvent
     *
     * @param engine the engine involved, cannot be null
     */
    public WorldEngineEvent(WorldEngine engine) {
        if (engine == null) throw new IllegalArgumentException("engine cannot be null");

        this.engine = engine;
    }

    /**
     * Gets the applicable world engine
     *
     * @return the world engine
     */
    public WorldEngine getEngine() {
        return engine;
    }

}
