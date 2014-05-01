package com.turt2live.antishare.configuration;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Represents a memory-based configuration with no save or load functionality.
 *
 * @author turt2live
 */
// TODO: Unit test
public class MemoryConfiguration extends Configuration {

    private ConcurrentMap<String, Object> values = new ConcurrentHashMap<String, Object>();

    @Override
    public void load() {
    }

    @Override
    public void save() {
    }

    @Override
    public void set(String key, Object value) {
        if (value == null || key == null) throw new IllegalArgumentException();
        values.put(key, value);
    }

    @Override
    public Object getObject(String key, Object def) {
        if (key == null) throw new IllegalArgumentException();
        return values.containsKey(key) ? values.get(key) : def;
    }

    @Override
    public int getInt(String key, int def) {
        Object obj = getObject(key, def);
        if (obj instanceof Integer) return (Integer) obj;
        return def;
    }

    @Override
    public double getDouble(String key, double def) {
        Object obj = getObject(key, def);
        if (obj instanceof Double) return (Double) obj;
        return def;
    }

    @Override
    public String getString(String key, String def) {
        Object obj = getObject(key, def);
        if (obj instanceof String) return (String) obj;
        return def;
    }

    @Override
    public List<String> getStringList(String key, List<String> def) {
        Object obj = getObject(key, def);
        try {
            List<String> list = (List<String>) obj;
            return list;
        } catch (Exception e) {
        }
        return def;
    }

    @Override
    public boolean getBoolean(String key, boolean def) {
        Object obj = getObject(key, def);
        if (obj instanceof Boolean) return (Boolean) obj;
        return def;
    }
}
