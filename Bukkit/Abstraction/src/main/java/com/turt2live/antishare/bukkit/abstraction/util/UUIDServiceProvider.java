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

package com.turt2live.antishare.bukkit.abstraction.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Mojang UUID service provider using http://uuid.turt2live.com as an API endpoint
 */
public final class UUIDServiceProvider {

    /**
     * Inserts dashes into a 32 character string to create a valid UUID format. Null strings (and wrong lengths)
     * return null.
     *
     * @param s the string to convert. Must be exactly 32 characters.
     *
     * @return the converted string, or null for invalid input
     */
    public static String insertDashes(String s) {
        if (s == null || s.length() != 32) return null;
        return s.substring(0, 8) + "-" + s.substring(8, 12) + "-" + s.substring(12, 16) + "-" + s.substring(16, 20) + "-" + s.substring(20, 32);
    }

    /**
     * Gets the name of a player for the specified UUID.
     *
     * @param uuid the uuid to lookup, cannot be null
     *
     * @return the player's name, or null if not found or for invalid input
     */
    public static String getName(UUID uuid) {
        if (uuid == null) return null;
        try {
            URL url = new URL("http://uuid.turt2live.com/name/" + uuid.toString().replaceAll("-", ""));
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String parsed = "";
            String line;
            while ((line = reader.readLine()) != null) parsed += line;
            reader.close();

            Object o = JSONValue.parse(parsed);
            if (o instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) o;
                Object status = jsonObject.get("status");
                if (status instanceof String && ((String) status).equalsIgnoreCase("ok")) {
                    o = jsonObject.get("name");
                    if (o instanceof String) {
                        return (String) o;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @throws IllegalArgumentException thrown for null arguments
     * @see #getName(java.util.UUID)
     */
    public static void getNameAsync(final UUID uuid, final Future<String> future) {
        if (uuid == null || future == null) throw new IllegalArgumentException();
        new Thread(new Runnable() {
            public void run() {
                String name = getName(uuid);
                future.accept(name);
            }
        }).start();
    }

    /**
     * Gets the UUID of a player name.
     *
     * @param name the name, cannot be null
     *
     * @return the UUID for the player, or null if not found or for invalid input
     */
    public static UUID getUUID(String name) {
        if (name == null) return null;
        try {
            URL url = new URL("http://uuid.turt2live.com/uuid/" + name);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String parsed = "";
            String line;
            while ((line = reader.readLine()) != null) parsed += line;
            reader.close();

            Object o = JSONValue.parse(parsed);
            if (o instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) o;
                o = jsonObject.get("uuid");
                if (o instanceof String) {
                    String s = (String) o;
                    if (!s.equalsIgnoreCase("unknown")) {
                        return UUID.fromString(insertDashes(s));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @throws IllegalArgumentException thrown for null arguments
     * @see #getUUID(String)
     */
    public static void getUUIDAsync(final String name, final Future<UUID> future) {
        if (name == null || future == null) throw new IllegalArgumentException();
        new Thread(new Runnable() {
            public void run() {
                UUID uuid = getUUID(name);
                future.accept(uuid);
            }
        }).start();
    }

    /**
     * Gets the known username history of a UUID. All dates are in UTC.
     * This returns a map of player names and when they were last seen (the
     * approximate date they stopped using that name).
     *
     * @param uuid the uuid to lookup, cannot be null
     *
     * @return a map of names and dates (UTC), or an empty map for invalid input or unknown/non-existent history
     */
    public static Map<String, Date> getHistory(UUID uuid) {
        if (uuid == null) return new HashMap<String, Date>();
        try {
            URL url = new URL("http://uuid.turt2live.com/history/" + uuid.toString().replaceAll("-", ""));
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String parsed = "";
            String line;
            while ((line = reader.readLine()) != null) parsed += line;
            reader.close();

            Map<String, Date> map = new HashMap<String, Date>();
            Object o = JSONValue.parse(parsed);
            if (o instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) o;
                Object namesObj = jsonObject.get("names");
                if (namesObj instanceof JSONArray) {
                    JSONArray names = (JSONArray) namesObj;
                    for (int i = 0; i < names.size(); i++) {
                        o = names.get(i);
                        if (o instanceof JSONObject) {
                            JSONObject json = (JSONObject) o;

                            Object nameObj = json.get("name");
                            Object dateObj = json.get("last-seen");
                            if (nameObj instanceof String && dateObj instanceof String) {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                format.setTimeZone(TimeZone.getTimeZone("UTC"));
                                try {
                                    Date date = format.parse((String) dateObj);
                                    map.put((String) nameObj, date);
                                } catch (ParseException e) {
                                    System.out.println("Could not parse " + dateObj + ": " + e.getMessage());
                                }
                            }
                        }
                    }
                }
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<String, Date>();
    }

    /**
     * @throws IllegalArgumentException thrown for null arguments
     * @see #getHistory(java.util.UUID)
     */
    public static void getHistoryAsync(final UUID uuid, final Future<Map<String, Date>> future) {
        if (uuid == null || future == null) throw new IllegalArgumentException();
        new Thread(new Runnable() {
            public void run() {
                Map<String, Date> history = getHistory(uuid);
                future.accept(history);
            }
        }).start();
    }
}
