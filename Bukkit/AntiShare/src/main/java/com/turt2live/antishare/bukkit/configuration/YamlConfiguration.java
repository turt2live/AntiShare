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

package com.turt2live.antishare.bukkit.configuration;

import com.turt2live.antishare.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * AntiShare to/from Bukkit YAML wrapper
 */
public class YamlConfiguration extends Configuration {

    private org.bukkit.configuration.file.YamlConfiguration bukkit;
    private File file;

    public YamlConfiguration(File file) {
        if (file == null) throw new IllegalArgumentException("arguments cannot be null");
        this.bukkit = new org.bukkit.configuration.file.YamlConfiguration();
        this.file = file;
        load();
    }

    @Override
    public void load() {
        try {
            bukkit.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        try {
            bukkit.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void set(String key, Object value) {
        bukkit.set(key, value);
    }

    @Override
    public Object getObject(String key, Object def) {
        return bukkit.get(key, def);
    }

    @Override
    public int getInt(String key, int def) {
        return bukkit.getInt(key, def);
    }

    @Override
    public double getDouble(String key, double def) {
        return bukkit.getDouble(key, def);
    }

    @Override
    public String getString(String key, String def) {
        return bukkit.getString(key, def);
    }

    @Override
    public List<String> getStringList(String key, List<String> def) {
        List<String> list = bukkit.getStringList(key);
        if (list == null) return def;
        return list;
    }

    @Override
    public boolean getBoolean(String key, boolean def) {
        return bukkit.getBoolean(key, def);
    }
}
