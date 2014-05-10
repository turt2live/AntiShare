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

package com.turt2live.antishare.bukkit.lang;

import com.turt2live.antishare.bukkit.AntiShare;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * AntiShare Language File. This is largely not documented for code cleanliness
 *
 * @author turt2live
 */
public class Lang {

    public static final String ERROR_HELP_SUGGEST = "errors.help-suggest";
    public static final String ERROR_INTERNAL = "errors.internal";
    public static final String ERROR_NOT_A_PLAYER = "errors.not-a-player";
    public static final String ERROR_NO_PERMISSION = "errors.permission";
    public static final String ERROR_SYNTAX = "errors.syntax";
    public static final String HELP_TITLE = "help.title";
    public static final String HELP_LINE = "help.line";
    public static final String PREFIX = "prefix";
    public static final String TOOL_CHECK_LORE = "tools.check-lore";
    public static final String TOOL_CHECK_TITLE = "tools.check-title";
    public static final String TOOL_ON_CHECK = "tools.on-check";
    public static final String TOOL_ON_GET = "tools.on-get";
    public static final String TOOL_ON_SET = "tools.on-set";
    public static final String TOOL_ON_UNSET = "tools.on-unset";
    public static final String TOOL_SET_LORE = "tools.set-lore";
    public static final String TOOL_SET_TITLE = "tools.set-title";
    public static final String NAUGHTY_PLACE = "naughty.place";
    public static final String NAUGHTY_BREAK = "naughty.break";
    public static final String NAUGHTY_COMMAND = "naughty.command";
    public static final String NAUGHTY_ADMIN_PLACE = "naughty.admin.place";
    public static final String NAUGHTY_ADMIN_BREAK = "naughty.admin.break";
    public static final String NAUGHTY_ADMIN_COMMAND = "naughty.admin.command";

    // Help strings
    public static final String HELP_CMD_HELP = "help.command.help";
    public static final String HELP_CMD_TOOLS = "help.command.tools";

    private static Lang instance;
    private FileConfiguration configuration;

    private Lang() {
        reload();
    }

    /**
     * Reloads the language file settings
     */
    public void reload() {
        File toFile = new File(AntiShare.getInstance().getDataFolder(), "lang.yml");

        FileConfiguration lang = YamlConfiguration.loadConfiguration(toFile);
        FileConfiguration defaults = YamlConfiguration.loadConfiguration(AntiShare.getInstance().getResource("lang.yml"));
        lang.setDefaults(defaults);
        lang.options().copyDefaults(true);

        try {
            lang.save(toFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        configuration = lang;
    }

    /**
     * Gets the format for a specified key
     *
     * @param key the key to lookup
     *
     * @return the format from the file. If the key is null or not found, this returns null
     */
    public String getFormat(String key) {
        if (key == null) return null;

        return configuration.getString(key);
    }

    /**
     * Gets the format, as a list, for a specified key
     *
     * @param key the key to lookup
     *
     * @return the format from the file. If the key is null or not found, this returns null
     */
    public List<String> getFormatList(String key) {
        if (key == null) return null;

        return configuration.getStringList(key);
    }

    /**
     * Gets the language file instance
     *
     * @return the language file instance
     */
    public static Lang getInstance() {
        if (instance == null) instance = new Lang();
        return instance;
    }

}
