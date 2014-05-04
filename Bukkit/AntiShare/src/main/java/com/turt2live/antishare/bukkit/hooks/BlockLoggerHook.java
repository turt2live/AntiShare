package com.turt2live.antishare.bukkit.hooks;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * The central hook for block logging integration
 *
 * @author turt2live
 */
public class BlockLoggerHook implements BlockLogger {

    private List<BlockLogger> hooks = new ArrayList<BlockLogger>();

    /**
     * Adds a non-null logger to the internal list
     *
     * @param logger the logger to add, null is ignored
     */
    public void addLogger(BlockLogger logger) {
        if (logger != null) {
            hooks.add(logger);
        }
    }

    /**
     * Removes a non-null logger from the internal list
     *
     * @param logger the logger to remove, null is ignored
     */
    public void removeLogger(BlockLogger logger) {
        if (logger != null) {
            hooks.remove(logger);
        }
    }

    /**
     * Removes all known internal loggers from the list
     */
    public void clearLoggers() {
        hooks.clear();
    }
}
