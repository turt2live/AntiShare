package com.turt2live.antishare.io;

import com.turt2live.antishare.ASLocation;
import com.turt2live.antishare.BlockType;

/**
 * Handles block information
 *
 * @author turt2live
 */
public interface BlockManager {

    /**
     * Gets the store for a specified location
     *
     * @param x the x location
     * @param y the y location
     * @param z the z location
     * @return the block store for the specified location, may be null
     */
    public BlockStore getStore(int x, int y, int z);

    /**
     * Gets the store for a specified location
     *
     * @param location the location, cannot be null
     * @return the block store for the specified location, may be null
     */
    public BlockStore getStore(ASLocation location);

    /**
     * Sets a block's type for the given location
     *
     * @param x    the X location
     * @param y    the Y location
     * @param z    the Z location
     * @param type the new block type. Null is assumed to be {@link BlockType#UNKNOWN}
     */
    public void setBlockType(int x, int y, int z, BlockType type);

    /**
     * Sets a block's type for the given location
     *
     * @param location the location, cannot be null
     * @param type     the new block type. Null is assumed to be {@link BlockType#UNKNOWN}
     */
    public void setBlockType(ASLocation location, BlockType type);

    /**
     * Gets the block type for the given location
     *
     * @param x the X location
     * @param y the Y location
     * @param z the Z location
     * @return the block type
     */
    public BlockType getBlockType(int x, int y, int z);

    /**
     * Gets the block type for the given location
     *
     * @param location the location, cannot be null
     * @return the block type
     */
    public BlockType getBlockType(ASLocation location);

    /**
     * Saves all the known block stores
     */
    public void saveAll();

    /**
     * Loads all the known block stores
     */
    public void loadAll();

}
