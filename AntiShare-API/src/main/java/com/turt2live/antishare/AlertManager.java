package com.turt2live.antishare;

import com.turt2live.antishare.events.EventDispatcher;
import com.turt2live.antishare.events.general.AlertEvent;


/**
 * The alert manager for AntiShare
 */
// TODO: Unit test
public class AlertManager {

    // TODO: Per-alert enabled alerts

    /**
     * Triggers a block-related alert
     *
     * @param type   the alert type, cannot be null
     * @param player the player who triggered this alert, cannot be null
     * @param block  the block involved with this alert, cannot be null
     */
    public static void triggerAlert(AlertEvent.AlertType type, APlayer player, ABlock block) {
        if (type == null || player == null || block == null) throw new IllegalArgumentException();
        EventDispatcher.dispatch(new AlertEvent(type, player, block));
    }

}
