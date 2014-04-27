package com.turt2live.antishare;

/**
 * An AntiShare Block
 *
 * @author turt2live
 */
public interface ABlock {

    /**
     * Gets the block's location
     *
     * @return the block location
     */
    public ASLocation getLocation();

    /**
     * Gets the world of this block
     *
     * @return the world
     */
    public AWorld getWorld();
}
