package com.turt2live.antishare;

/**
 * An AntiShare world
 *
 * @author turt2live
 */
public interface AWorld {

    /**
     * Gets the name of this world
     *
     * @return the world's name
     */
    public String getName();

    /**
     * Gets the block at the specified location
     *
     * @param location the location of the block. If null, "air" is returned
     * @return the block. If not found an "air" block is returned.
     */
    public ABlock getBlock(ASLocation location);

}
