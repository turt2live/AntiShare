package com.turt2live.antishare.events;

/**
 * An implementation of a CancellableEvent
 * @author turt2live
 */
public abstract class CancellableEventBase implements CancellableEvent{

    /**
     * The cancelled state, default false
     */
    protected boolean cancelled = false;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
