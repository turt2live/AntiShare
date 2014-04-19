package com.turt2live.antishare.io.generics;

import com.turt2live.antishare.io.BlockStore;
import com.turt2live.antishare.utils.ASLocation;
import com.turt2live.antishare.utils.BlockType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Generic block store
 *
 * @author turt2live
 */
public abstract class GenericBlockStore implements BlockStore {

    private ConcurrentMap<ASLocation, BlockType> types = new ConcurrentHashMap<ASLocation, BlockType>();
    private volatile long lastAccess = 0;

    // Test entry point. Should not be used elsewhere
    void initTest() {
        if (types != null) throw new IllegalArgumentException("Collection not null!");
        this.types = new ConcurrentHashMap<ASLocation, BlockType>();
    }

    @Override
    public BlockType getType(int x, int y, int z) {
        return getType(new ASLocation(x, y, z));
    }

    @Override
    public void setType(int x, int y, int z, BlockType type) {
        setType(new ASLocation(x, y, z), type);
    }

    @Override
    public BlockType getType(ASLocation location) {
        updateLastAccess();

        if (location == null) throw new IllegalArgumentException("location cannot be null");

        BlockType type = types.get(location);
        return type == null ? BlockType.UNKNOWN : type;
    }

    @Override
    public void setType(ASLocation location, BlockType type) {
        updateLastAccess();

        if (location == null) throw new IllegalArgumentException("location cannot be null");

        if (type == null || type == BlockType.UNKNOWN) types.remove(location);
        else types.put(location, type);
    }

    @Override
    public Map<ASLocation, BlockType> getAll() {
        Map<ASLocation, BlockType> map = new HashMap<ASLocation, BlockType>();
        map.putAll(types);
        return map;
    }

    @Override
    public void clear() {
        updateLastAccess();

        types.clear();
    }

    @Override
    public long getLastAccess() {
        return lastAccess;
    }

    private void updateLastAccess() {
        lastAccess = System.currentTimeMillis();
    }

    /**
     * Gets a live map of the underlying collection
     *
     * @return the live map of the underlying collection
     */
    protected ConcurrentMap<ASLocation, BlockType> getLiveMap() {
        return types;
    }
}
