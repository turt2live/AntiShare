package com.turt2live.antishare.engine;

/**
 * Represents an engine listener. This can be used by the implementation of the engine
 * to assist in setup of objects.
 *
 * @author turt2live
 */
public interface EngineListener {

    /**
     * Called when a world engine is created. This is called after the world
     * engine has been registered internally.
     *
     * @param engine the engine that was created
     */
    public void onWorldEngineCreate(WorldEngine engine);

    /**
     * Called when the engine is told to prepare for shutdown. This is called
     * prior to any internal routines.
     */
    public void onEngineShutdown();

}
