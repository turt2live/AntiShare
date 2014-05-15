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

import com.turt2live.antishare.object.attribute.BlockType;

import java.util.Map;
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
    public BlockType getType(UUID uuid);

    /**
     * Sets the type of an entity in the internal cache
     *
     * @param uuid the entity UUID, cannot be null
     * @param type the entity type, null is interpreted as UNKNOWN
     */
    public void setType(UUID uuid, BlockType type);

    /**
     * Gets all known entities being tracked. Edits to the returned map yield
     * no results.
     *
     * @return a copy of all known entities being tracked
     */
    public Map<UUID, BlockType> getAll();

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
     * Runs a cleanup (on the current thread) on the EntityManager. This will remove
     * any excess objects which have not been touched from the manager by cleanly
     * unloading them.
     */
    public void cleanup();

    /**
     * Gets the time (in milliseconds) this entity store was last accessed
     *
     * @return the last time this store was accessed
     */
    public long getLastAccess();

}
