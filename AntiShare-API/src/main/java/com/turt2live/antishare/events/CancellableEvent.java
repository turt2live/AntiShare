package com.turt2live.antishare.events;

/**
 * A cancellable event
 *
 * @author turt2live
 */
public interface CancellableEvent extends Event {

    /**
     * Determines if this event is cancelled
     *
     * @return true if cancelled
     */
    public boolean isCancelled();

    /**
     * Sets this event's cancelled state
     *
     * @param cancelled the new state
     */
    public void setCancelled(boolean cancelled);

}
