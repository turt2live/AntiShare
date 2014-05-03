package com.turt2live.antishare.events.general;

import com.turt2live.antishare.ABlock;
import com.turt2live.antishare.APlayer;
import com.turt2live.antishare.events.Event;

/**
 * Represents an event for an alert. This is fired so that the implementing
 * code of this API can react to alerts and display them accordingly.
 *
 * @author turt2live
 */
public class AlertEvent implements Event {

    public enum AlertType {
        /**
         * Used to display a "user tried to place X" message
         */
        ADMIN_BLOCK_PLACE
    }

    private AlertType type;
    private APlayer player;
    private ABlock block;

    /**
     * Creates a new alert event
     *
     * @param type   the type of the event, cannot be null
     * @param block  the involved block, cannot be null
     * @param player the involved player, cannot be null
     */
    public AlertEvent(AlertType type, APlayer player, ABlock block) {
        if (type == null || player == null || block == null) throw new IllegalArgumentException();

        this.type = type;
        this.player = player;
        this.block = block;
    }

    /**
     * The type of the alert
     *
     * @return the alert type
     */
    public AlertType getType() {
        return type;
    }

    /**
     * Gets the player involved in triggering this alert event
     *
     * @return the player
     */
    public APlayer getPlayer() {
        return player;
    }

    /**
     * Gets the block involved in triggering this alert event
     *
     * @return the block, may be null
     */
    public ABlock getBlock() {
        return block;
    }

}
