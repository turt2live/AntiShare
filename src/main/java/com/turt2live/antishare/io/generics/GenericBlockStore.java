package com.turt2live.antishare.io.generics;

import com.turt2live.antishare.ASLocation;
import com.turt2live.antishare.BlockType;
import com.turt2live.antishare.io.BlockStore;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Generic block store
 *
 * @author turt2live
 */
public abstract class GenericBlockStore implements BlockStore {

    private ConcurrentMap<ASLocation, BlockType> types = new ConcurrentHashMap<ASLocation, BlockType>();

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
        BlockType type = types.get(location);
        return type == null ? BlockType.UNKNOWN : type;
    }

    @Override
    public void setType(ASLocation location, BlockType type) {
        if (location == null) throw new IllegalArgumentException("location cannot be null");

        if (type == null || type == BlockType.UNKNOWN) types.remove(location);
        else types.put(location, type);
    }

    @Override
    public void clear() {
        types.clear();
    }

    protected ConcurrentMap<ASLocation, BlockType> getLiveMap() {
        return types;
    }
}
