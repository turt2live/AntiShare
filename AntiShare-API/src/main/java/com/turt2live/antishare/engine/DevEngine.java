package com.turt2live.antishare.engine;

/**
 * Represents the developer engine. This handles all debugging code
 * as well as integration with external services to ensure the operation
 * of this API Engine.
 *
 * @author turt2live
 */
public class DevEngine {

    private static boolean ENABLED = false;

    // TODO: Make use of the DevTools API

    private DevEngine() {
    }

    /**
     * Sets this developer engine enabled or disabled
     *
     * @param enabled true for enabled, false otherwise
     */
    public static void setEnabled(boolean enabled) {
        ENABLED = enabled;
    }

    /**
     * Determines if this developer engine is enabled
     *
     * @return true for enabled, false otherwise
     */
    public static boolean isEnabled() {
        return ENABLED;
    }

}
