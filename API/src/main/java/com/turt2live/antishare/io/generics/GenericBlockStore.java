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

import com.turt2live.antishare.io.BlockStore;
import com.turt2live.antishare.object.ASLocation;
import com.turt2live.antishare.object.attribute.ObjectType;

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

    private ConcurrentMap<ASLocation, ObjectType> types = new ConcurrentHashMap<>();
    private volatile long lastAccess = 0;

    // Test entry point. Should not be used elsewhere
    void initTest() {
        if (types != null) throw new IllegalArgumentException("Collection not null!");
        this.types = new ConcurrentHashMap<>();
    }

    @Override
    public ObjectType getType(int x, int y, int z) {
        return getType(new ASLocation(x, y, z));
    }

    @Override
    public void setType(int x, int y, int z, ObjectType type) {
        setType(new ASLocation(x, y, z), type);
    }

    @Override
    public ObjectType getType(ASLocation location) {
        updateLastAccess();

        if (location == null) throw new IllegalArgumentException("location cannot be null");

        // Remove world from location store
        location = new ASLocation(null, location.X, location.Y, location.Z);

        ObjectType type = types.get(location);
        return type == null ? ObjectType.UNKNOWN : type;
    }

    @Override
    public void setType(ASLocation location, ObjectType type) {
        updateLastAccess();

        if (location == null) throw new IllegalArgumentException("location cannot be null");

        // Remove world from location store
        location = new ASLocation(null, location.X, location.Y, location.Z);

        if (type == null || type == ObjectType.UNKNOWN) types.remove(location);
        else types.put(location, type);
    }

    @Override
    public Map<ASLocation, ObjectType> getAll() {
        Map<ASLocation, ObjectType> map = new HashMap<>();
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
    protected ConcurrentMap<ASLocation, ObjectType> getLiveMap() {
        return types;
    }
}
