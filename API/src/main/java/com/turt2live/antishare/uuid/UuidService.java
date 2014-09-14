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

import com.turt2live.uuid.CachingServiceProvider;
import com.turt2live.uuid.MemoryPlayerRecord;
import com.turt2live.uuid.PlayerRecord;
import com.turt2live.uuid.ServiceProvider;
import com.turt2live.uuid.turt2live.v2.ApiV2Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents the UUID service provided by AntiShare. The cache sources used
 * by this implementation must return valid player UUIDs (UUID version 4). If
 * a UUID is in version 4 format, it is trusted by this service. If the UUID
 * is not in a valid format, it is looked up from the underlying service provider.
 * </P>
 * This uses http://uuid.turt2live.com/api/v2 as the source for all UUID/name lookups.
 *
 * @author turt2live
 */
public class UuidService implements ServiceProvider {

    private static UuidService SERVICE_INSTANCE;
    private List<CacheSource> additionalSources = new ArrayList<>();
    private CachingServiceProvider cacheProvider;

    UuidService() {
        cacheProvider = new CachingServiceProvider(new ApiV2Service());
    }

    @Override
    public List<PlayerRecord> doBulkLookup(String... playerNames) {
        if (playerNames == null) throw new IllegalArgumentException();
        List<PlayerRecord> records = new ArrayList<>();

        for (String name : playerNames) {
            if (name == null) throw new IllegalArgumentException();

            // Check caches
            UUID uuid = null;
            for (CacheSource source : additionalSources) {
                uuid = source.get(name);
                if (uuid != null) break;
            }

            if (uuid != null && uuid.version() != 4) uuid = null;

            PlayerRecord record;
            if (uuid != null) {
                record = new MemoryPlayerRecord(uuid, name);
            } else {
                record = cacheProvider.doLookup(name);
            }

            if (record != null) records.add(record);
        }

        return records;
    }

    @Override
    public List<PlayerRecord> doBulkLookup(UUID... uuids) {
        if (uuids == null) throw new IllegalArgumentException();
        List<PlayerRecord> records = new ArrayList<>();

        for (UUID uuid : uuids) {
            if (uuid == null) throw new IllegalArgumentException();

            // Check caches
            String playerName = null;
            for (CacheSource source : additionalSources) {
                playerName = source.get(uuid);
                if (playerName != null) break;
            }

            PlayerRecord record;
            if (playerName != null) {
                record = new MemoryPlayerRecord(uuid, playerName);
            } else {
                record = cacheProvider.doLookup(uuid);
            }

            if (record != null) records.add(record);
        }

        return records;
    }

    @Override
    public PlayerRecord doLookup(String playerName) {
        List<PlayerRecord> records = doBulkLookup(playerName);
        if (records != null && !records.isEmpty()) return records.get(0);
        return null;
    }

    @Override
    public PlayerRecord doLookup(UUID uuid) {
        List<PlayerRecord> records = doBulkLookup(uuid);
        if (records != null && !records.isEmpty()) return records.get(0);
        return null;
    }

    /**
     * Adds a cache source to the uuid service
     *
     * @param source the source to add, cannot be null
     */
    public void addSource(CacheSource source) {
        if (source == null) throw new IllegalArgumentException();

        additionalSources.add(source);
    }

    /**
     * Removes all known additional cache sources from memory
     */
    public void removeSources() {
        additionalSources.clear();
    }

    /**
     * Gets the current instance of the UUID service. If this is instance
     * has not been created, one will be made such that a non-null value
     * will be returned.
     *
     * @return the current UUID service
     */
    public static UuidService getInstance() {
        if (SERVICE_INSTANCE == null) SERVICE_INSTANCE = new UuidService();
        return SERVICE_INSTANCE;
    }

    // --- start delegates ---

    @Override
    public PlayerRecord getRandomSample() {
        return cacheProvider.getRandomSample();
    }

    @Override
    public List<PlayerRecord> getRandomSample(int amount) {
        return cacheProvider.getRandomSample(amount);
    }

    @Override
    public String getServiceName() {
        return cacheProvider.getServiceName();
    }

    @Override
    public String[] getNameHistory(UUID uuid) {
        return cacheProvider.getNameHistory(uuid);
    }

    // --- end delegates ---
}
