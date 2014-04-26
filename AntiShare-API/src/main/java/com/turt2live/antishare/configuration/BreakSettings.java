package com.turt2live.antishare.configuration;

import com.turt2live.antishare.utils.ASGameMode;

/**
 * Small class for holding information about break settings
 *
 * @author turt2live
 */
// TODO: Unit test
public final class BreakSettings {

    /**
     * True for denial, false otherwise
     */
    public final boolean denyAction;
    /**
     * The gamemode to break as, never null
     */
    public final ASGameMode breakAs;

    /**
     * Creates a new BreakSettings object
     *
     * @param deny    true to deny the action, false otherwise
     * @param breakAs the gamemode to break something as, cannot be null
     */
    public BreakSettings(boolean deny, ASGameMode breakAs) {
        if (breakAs == null) throw new IllegalArgumentException("break as cannot be null");

        this.denyAction = deny;
        this.breakAs = breakAs;
    }

}
