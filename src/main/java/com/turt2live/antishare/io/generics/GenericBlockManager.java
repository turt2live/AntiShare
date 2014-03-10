package com.turt2live.antishare.io.generics;

import com.turt2live.antishare.ASLocation;
import com.turt2live.antishare.BlockType;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.io.BlockManager;
import com.turt2live.antishare.io.BlockStore;

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

    @Override
    public BlockStore getStore(int x, int y, int z) {
        return getStore(new ASLocation(x, y, z));
    }

    @Override
    public BlockStore getStore(ASLocation location) {
        if (location == null) throw new IllegalArgumentException("location cannot be null");

        int sx = (int) Math.floor(location.X / (double) blocksPerStore);
        int sy = (int) Math.floor(location.Y / (double) blocksPerStore);
        int sz = (int) Math.floor(location.Z / (double) blocksPerStore);
        ASLocation storeLocation = new ASLocation(sx, sy, sz);

        BlockStore store = stores.get(storeLocation);
        if (store == null) {
            store = createStore(sx, sy, sz);
            stores.put(storeLocation, store);
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
                stores.remove(location);
            }
        }
    }

    /**
     * Gets the live map of the stores. The key is a encoded version of a
     * block coordinate using the following mathematical algorithm:<br/>
     * <code>
     * int storeX = floor(blockX / {@link #blocksPerStore});<br/>
     * int storeY = floor(blockY / {@link #blocksPerStore});<br/>
     * int storeZ = floor(blockZ / {@link #blocksPerStore});<br/>
     * {@link com.turt2live.antishare.ASLocation} theKey = new {@link com.turt2live.antishare.ASLocation}(storeX, storeY, storeZ);
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
     * @param sx the store x location, as per {@link #blocksPerStore}
     * @param sy the store y location, as per {@link #blocksPerStore}
     * @param sz the store z location, as per {@link #blocksPerStore}
     * @return the new store, should not be null
     */
    protected abstract BlockStore createStore(int sx, int sy, int sz);
}
