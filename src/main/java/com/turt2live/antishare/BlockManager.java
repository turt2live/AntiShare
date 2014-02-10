package com.turt2live.antishare;

import com.turt2live.antishare.io.BlockStore;

/**
 * Handles block information
 *
 * @author turt2live
 */
public class BlockManager {

    private BlockStore store;

    /**
     * Creates a new Block Manager
     *
     * @param store the store to use, cannot be null
     */
    public BlockManager(BlockStore store) {
        if (store == null) throw new IllegalArgumentException();

        this.store = store;
    }

}
