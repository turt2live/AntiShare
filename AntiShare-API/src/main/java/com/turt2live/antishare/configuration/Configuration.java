package com.turt2live.antishare.configuration;

import java.util.List;

/**
 * An AntiShare configuration. This is used for configuring various
 * control mechanisms of the plugin.
 *
 * @author turt2live
 */
public abstract class Configuration {

    /**
     * Loads this configuration
     */
    public abstract void load();

    /**
     * Saves this configuration
     */
    public abstract void save();

    /**
     * Sets an object in the configuration
     *
     * @param key   the key to save, cannot be null
     * @param value the value to save. If null, "remove" is implied
     */
    public abstract void set(String key, Object value);

    /**
     * Gets an object from the configuration
     *
     * @param key the key to lookup, cannot be null
     * @param def the default value to use if the key cannot be found
     * @return the object or the default value if not found
     */
    public abstract Object getObject(String key, Object def);

    /**
     * Gets an integer from the configuration
     *
     * @param key the key to lookup, cannot be null
     * @param def the default value to use if the key cannot be found
     * @return the integer or the default value if not found
     */
    public abstract int getInt(String key, int def);

    /**
     * Gets a double from the configuration
     *
     * @param key the key to lookup, cannot be null
     * @param def the default value to use if the key cannot be found
     * @return the double or the default value if not found
     */
    public abstract double getDouble(String key, double def);

    /**
     * Gets a string from the configuration
     *
     * @param key the key to lookup, cannot be null
     * @param def the default value to use if the key cannot be found
     * @return the string or the default value if not found
     */
    public abstract String getString(String key, String def);

    /**
     * Gets a list of strings from the configuration
     *
     * @param key the key to lookup, cannot be null
     * @param def the default value to use if the key cannot be found
     * @return the list of strings or the default value if not found
     */
    public abstract List<String> getStringList(String key, List<String> def);

    /**
     * Gets a boolean from the configuration
     *
     * @param key the key to lookup, cannot be null
     * @param def the default value to use if the key cannot be found
     * @return the boolean or the default value if not found
     */
    public abstract boolean getBoolean(String key, boolean def);

    /**
     * Uses a default of 'null'
     *
     * @see #getObject(String, Object)
     */
    public Object getObject(String key) {
        return getObject(key, null);
    }

    /**
     * Uses a default of '0'
     *
     * @see #getInt(String, int)
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * Uses a default of '0'
     *
     * @see #getDouble(String, double)
     */
    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    /**
     * Uses a default of 'null'
     *
     * @see #getString(String, String)
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /**
     * Uses a default of 'null'
     *
     * @see #getStringList(String, java.util.List)
     */
    public List<String> getStringList(String key) {
        return getStringList(key, null);
    }

    /**
     * Uses a default of 'false'
     *
     * @see #getBoolean(String, boolean)
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

}
