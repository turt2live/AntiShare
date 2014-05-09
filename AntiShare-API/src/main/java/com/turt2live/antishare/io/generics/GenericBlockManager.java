/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.io.generics;

import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.io.BlockManager;
import com.turt2live.antishare.io.BlockStore;
import com.turt2live.antishare.object.ASLocation;
import com.turt2live.antishare.object.attribute.BlockType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A generic block manager
 *
 * @author turt2live
 */
public abstract class GenericBlockManager implements BlockManager {

    /**
     * The number of blocks to be stored per store
     */
    protected final int blocksPerStore;

    private ConcurrentMap<ASLocation, BlockStore> stores = new ConcurrentHashMap<ASLocation, BlockStore>();

    /**
     * Creates a new generic block manager
     *
     * @param blocksPerStore the number of blocks per store
     */
    public GenericBlockManager(int blocksPerStore) {
        if (blocksPerStore <= 0) throw new IllegalArgumentException("cannot have less than 1 block per store");

        this.blocksPerStore = blocksPerStore;
    }

    /**
     * Gets the number of blocks per store
     *
     * @return the number of blocks per store
     */
    public int getBlocksPerStore() {
        return blocksPerStore;
    }

    @Override
    public BlockStore getStore(int x, int y, int z) {
        return getStore(new ASLocation(x, y, z));
    }

    @Override
    public BlockStore getStore(ASLocation location) {
        if (location == null) throw new IllegalArgumentException("location cannot be null");

        int sx = (int) Math.floor(location.X / (double) getBlocksPerStore());
        int sy = (int) Math.floor(location.Y / (double) getBlocksPerStore());
        int sz = (int) Math.floor(location.Z / (double) getBlocksPerStore());
        ASLocation storeLocation = new ASLocation(sx, sy, sz);

        BlockStore store = this.stores.get(storeLocation);
        if (store == null) {
            store = createStore(sx, sy, sz);
            this.stores.put(storeLocation, store);
            store.load();
        }
        return store;
    }

    @Override
    public void setBlockType(int x, int y, int z, BlockType type) {
        setBlockType(new ASLocation(x, y, z), type);
    }

    @Override
    public void setBlockType(ASLocation location, BlockType type) {
        if (location == null) throw new IllegalArgumentException("location cannot be null");

        if (type == null) type = BlockType.UNKNOWN;

        BlockStore store = getStore(location);
        if (store != null)
            store.setType(location, type);
    }

    @Override
    public BlockType getBlockType(int x, int y, int z) {
        return getBlockType(new ASLocation(x, y, z));
    }

    @Override
    public BlockType getBlockType(ASLocation location) {
        if (location == null) throw new IllegalArgumentException("location cannot be null");

        BlockStore store = getStore(location);
        if (store == null) return BlockType.UNKNOWN;

        return store.getType(location);
    }

    @Override
    public void saveAll() {
        for (BlockStore store : stores.values()) {
            store.save();
        }
    }

    @Override
    public void cleanup() {
        long now = System.currentTimeMillis();
        for (Map.Entry<ASLocation, BlockStore> storeEntry : stores.entrySet()) {
            ASLocation location = storeEntry.getKey();
            BlockStore store = storeEntry.getValue();

            long lastAccess = store.getLastAccess();
            if (now - lastAccess > Engine.getInstance().getCacheMaximum()) {
                store.save();
                this.stores.remove(location);
            }
        }
    }

    /**
     * Gets the live map of the stores. The key is a encoded version of a
     * block coordinate using the following mathematical algorithm:<br/>
     * <code>
     * int storeX = floor(blockX / {@link #getBlocksPerStore()});<br/>
     * int storeY = floor(blockY / {@link #getBlocksPerStore()});<br/>
     * int storeZ = floor(blockZ / {@link #getBlocksPerStore()});<br/>
     * {@link com.turt2live.antishare.object.ASLocation} theKey = new {@link com.turt2live.antishare.object.ASLocation}(storeX, storeY, storeZ);
     * </code>
     *
     * @return the live map
     */
    protected ConcurrentMap<ASLocation, BlockStore> getLiveStores() {
        return stores;
    }

    /**
     * Creates a new store file. The invoking object is expected to load any information
     * from the resulting store as this method should be expected to not load data.
     *
     * @param sx the store x location, as per {@link #getBlocksPerStore()}
     * @param sy the store y location, as per {@link #getBlocksPerStore()}
     * @param sz the store z location, as per {@link #getBlocksPerStore()}
     *
     * @return the new store, should not be null
     */
    protected abstract BlockStore createStore(int sx, int sy, int sz);
}
