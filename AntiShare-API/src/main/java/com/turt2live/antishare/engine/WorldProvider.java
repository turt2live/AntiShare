package com.turt2live.antishare.engine;

import com.turt2live.antishare.object.AWorld;

/**
 * Represents a simple world provider
 *
 * @author turt2live
 */
public interface WorldProvider {
    /**
     * Gets a world for the specified name
     *
     * @param name the world name, cannot be null
     * @return the world, or null if not found
     */
    public AWorld getWorld(String name);
}
