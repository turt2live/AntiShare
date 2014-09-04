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

package com.turt2live.antishare.uuid;

import java.util.UUID;

/**
 * Represents a source for UUIDs, such as a local cache. Uses of this cache source
 * include the internal UUID operations of AntiShare. Sources which use a cache source
 * may reject the returned values for various characteristics (those characteristics
 * should be documented in their respective places)
 *
 * @author turt2live
 */
public interface CacheSource {

    /**
     * Gets the UUID for the specified player name. If the supplied player name
     * does not have an associated UUID in the cache, this should return null. This
     * should throw an exception on null input.
     *
     * @param playerName the player name to lookup, cannot be null
     *
     * @return the cached UUID, or null if not found
     */
    public UUID get(String playerName);

    /**
     * Gets the player name for the specified UUID. If the supplied player UUID
     * does not have a an associated name in the cache, this should return null. This
     * should throw an exception on null input.
     *
     * @param player the player UUID to lookup, cannot be null
     *
     * @return the cached name, or null if not found
     */
    public String get(UUID player);
}
