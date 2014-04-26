package com.turt2live.antishare.events.worldengine;

import com.turt2live.antishare.engine.WorldEngine;

/**
 * Fired when a new WorldEngine is created (after being registered internally).
 *
 * @author turt2live
 */
public class WorldEngineCreateEvent extends WorldEngineEvent {

    /**
     * Creates a new WorldEngineEvent
     *
     * @param engine the engine involved, cannot be null
     */
    public WorldEngineCreateEvent(WorldEngine engine) {
        super(engine);
    }
}
