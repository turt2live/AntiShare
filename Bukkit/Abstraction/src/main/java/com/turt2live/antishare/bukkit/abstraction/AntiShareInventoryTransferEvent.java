package com.turt2live.antishare.bukkit.abstraction;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * AntiShare event for triggering an inventory transfer event check
 *
 * @author turt2live
 */
public class AntiShareInventoryTransferEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private Block block1, block2;
    private boolean cancelled = false;

    /**
     * Creates a new AntiShare transfer event check
     *
     * @param block1 the first block, cannot be null
     * @param block2 the second block, cannot be null
     */
    public AntiShareInventoryTransferEvent(Block block1, Block block2) {
        if (block1 == null || block2 == null) throw new IllegalArgumentException();

        this.block1 = block1;
        this.block2 = block2;
    }

    /**
     * Gets the first block
     *
     * @return the first block
     */
    public Block getBlock1() {
        return block1;
    }

    /**
     * Gets the second block
     *
     * @return the second block
     */
    public Block getBlock2() {
        return block2;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
