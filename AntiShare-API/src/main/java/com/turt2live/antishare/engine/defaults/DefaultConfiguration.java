package com.turt2live.antishare.engine.defaults;

import com.turt2live.antishare.configuration.Configuration;

import java.util.List;

/**
 * Default configuration
 * @author turt2live
 */
// TODO: Unit test
public class DefaultConfiguration extends Configuration {

        @Override
        public void load() {

        }

        @Override
        public void save() {

        }

        @Override
        public void set(String key, Object value) {

        }

        @Override
        public Object getObject(String key, Object def) {
            return def;
        }

        @Override
        public int getInt(String key, int def) {
            return def;
        }

        @Override
        public double getDouble(String key, double def) {
            return def;
        }

        @Override
        public String getString(String key, String def) {
            return def;
        }

        @Override
        public List<String> getStringList(String key, List<String> def) {
            return def;
        }

        @Override
        public boolean getBoolean(String key, boolean def) {
            return def;
        }
    }