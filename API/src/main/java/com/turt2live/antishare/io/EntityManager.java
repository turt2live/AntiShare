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

package com.turt2live.antishare.io;

import com.turt2live.antishare.object.attribute.ObjectType;

import java.util.UUID;

/**
 * Stores information about entities
 *
 * @author turt2live
 */
public interface EntityManager {

    /**
     * Gets the entity type. If the entity UUID is not found, UNKNOWN is returned
     *
     * @param uuid the entity UUID, cannot be null
     *
     * @return the type of entity, never null
     */
    public ObjectType getType(UUID uuid);

    /**
     * Sets the type of an entity in the internal cache
     *
     * @param uuid the entity UUID, cannot be null
     * @param type the entity type, null is interpreted as UNKNOWN
     */
    public void setType(UUID uuid, ObjectType type);

    /**
     * Saves all the known entities
     */
    public void save();

    /**
     * Loads all the known entities. The implementing manager will assume a
     * save has been completed and may wipe the previous entries from memory.
     */
    public void load();

    /**
     * Clears all records from memory, eliminating all records
     */
    public void clear();
}
