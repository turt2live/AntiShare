package com.turt2live.antishare.io;

import com.turt2live.antishare.ASLocation;
import com.turt2live.antishare.BlockType;

/**
 * Represents storage for block information
 *
 * @author turt2live
 */
public interface BlockStore {

    /**
     * Gets the type of a block type
     *
     * @param x the x location
     * @param y the y location
     * @param z the z location
     * @return the block type
     */
    public BlockType getType(int x, int y, int z);

    /**
     * Sets the type of a block
     *
     * @param x    the x location
     * @param y    the y location
     * @param z    the z location
     * @param type the new block type. Null is assumed to be {@link com.turt2live.antishare.BlockType#UNKNOWN}
     */
    public void setType(int x, int y, int z, BlockType type);

    /**
     * Gets the block type for a specified location
     *
     * @param location the location, cannot be null
     * @return the block type
     */
    public BlockType getType(ASLocation location);

    /**
     * Sets the type of a block
     *
     * @param location the location, cannot be null
     * @param type     the new block type. Null is assumed to be {@link com.turt2live.antishare.BlockType#UNKNOWN}
     */
    public void setType(ASLocation location, BlockType type);

    /**
     * Saves the store
     */
    public void save();

    /**
     * Loads the store
     */
    public void load();

    /**
     * Clears the store
     */
    public void clear();

}
