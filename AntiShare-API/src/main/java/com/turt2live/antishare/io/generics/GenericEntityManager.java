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

import com.turt2live.antishare.io.EntityManager;
import com.turt2live.antishare.object.attribute.ObjectType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A generic implementation of an entity manager. This will do basic management
 * of records by checking timestamps, however the initial load of records may be
 * significant. This implementation is designed to abstract the storage system
 * that is operating in the backend to assist in efficient storage. Implementations
 * of this class may inherit a storage scheme so long as the contracts are met.
 *
 * @author turt2live
 */
public abstract class GenericEntityManager implements EntityManager {

    private ConcurrentMap<UUID, ObjectType> records = new ConcurrentHashMap<UUID, ObjectType>();

    @Override
    public ObjectType getType(UUID uuid) {
        if (uuid == null) throw new IllegalArgumentException("UUID cannot be null");

        if (records.containsKey(uuid)) return records.get(uuid);

        return ObjectType.UNKNOWN;
    }

    @Override
    public void setType(UUID uuid, ObjectType type) {
        if (uuid == null) throw new IllegalArgumentException("UUID cannot be null");

        if (type == null || type == ObjectType.UNKNOWN) records.remove(uuid);
        else records.put(uuid, type);
    }

    /**
     * Gets a live copy of the data stored within this manager
     *
     * @return the live copy of the data
     */
    protected Map<UUID, ObjectType> getLiveMap() {
        return records;
    }

    @Override
    public void clear() {
        records.clear();
    }
}
