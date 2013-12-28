package com.turt2live.antishare.util;

import com.turt2live.antishare.config.ASConfig.InteractionSettings;

/**
 * Represents a class to hold "delay break settings"
 *
 * @author turt2live
 */
public class DelayBreakSettings {

    public final InteractionSettings interactionSettings;
    public final String playerName;

    public DelayBreakSettings(InteractionSettings interaction, String playerName) {
        this.interactionSettings = interaction;
        this.playerName = playerName;
    }

}
