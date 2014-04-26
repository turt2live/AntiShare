package com.turt2live.antishare.events.worldengine;

import com.turt2live.antishare.engine.WorldEngine;

/**
 * Fired when a world engine is shutting down (prior to internal shutdown code).
 *
 * @author turt2live
 */
public class WorldEngineShutdownEvent extends WorldEngineEvent {

    /**
     * Creates a new WorldEngineEvent
     *
     * @param engine the engine involved, cannot be null
     */
    public WorldEngineShutdownEvent(WorldEngine engine) {
        super(engine);
    }
}
