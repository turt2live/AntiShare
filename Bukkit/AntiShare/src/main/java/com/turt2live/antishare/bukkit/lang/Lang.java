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
import com.turt2live.antishare.collections.ArrayArrayList;
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

    // Misc strings
    public static final String PREFIX = "prefix";

    // General strings
    public static final String GENERAL_RELOADING = "general.reloading";
    public static final String GENERAL_RELOADED = "general.reloaded";
    public static final String GENERAL_RELOAD_WARNING = "general.reload-warning";
    public static final String GENERAL_RELOAD_CRITICAL = "general.reload-critical";
    public static final String GENERAL_WORLDSPLIT_APPROACH = "general.worldsplit-approach";
    public static final String GENERAL_WORLDSPLIT_CROSSED = "general.worldsplit-crossed";

    // Error strings
    public static final String ERROR_HELP_SUGGEST = "errors.help-suggest";
    public static final String ERROR_INTERNAL = "errors.internal";
    public static final String ERROR_NOT_A_PLAYER = "errors.not-a-player";
    public static final String ERROR_NO_PERMISSION = "errors.permission";
    public static final String ERROR_SYNTAX = "errors.syntax";

    // Tool strings
    public static final String TOOL_CHECK_LORE = "tools.check-lore";
    public static final String TOOL_CHECK_TITLE = "tools.check-title";
    public static final String TOOL_ON_GET = "tools.on-get";
    public static final String TOOL_ON_CHECK = "tools.on-check";
    public static final String TOOL_ON_SET = "tools.on-set";
    public static final String TOOL_ON_UNSET = "tools.on-unset";
    public static final String TOOL_ON_CHECK_ENTITY = "tools.on-check-entities";
    public static final String TOOL_ON_SET_ENTITY = "tools.on-set-entities";
    public static final String TOOL_ON_UNSET_ENTITY = "tools.on-unset-entities";
    public static final String TOOL_SET_LORE = "tools.set-lore";
    public static final String TOOL_SET_TITLE = "tools.set-title";

    // Naughty action strings
    public static final String NAUGHTY_PLACE = "naughty.place";
    public static final String NAUGHTY_BREAK = "naughty.break";
    public static final String NAUGHTY_COMMAND = "naughty.command";
    public static final String NAUGHTY_INTERACTION = "naughty.interact";
    public static final String NAUGHTY_USE = "naughty.use";
    public static final String NAUGHTY_DROP = "naughty.drop";
    public static final String NAUGHTY_PICKUP = "naughty.pickup";
    public static final String NAUGHTY_ENTITY_PLACE = "naughty.entity-place";
    public static final String NAUGHTY_ENTITY_BREAK = "naughty.entity-break";
    public static final String NAUGHTY_ENTITY_INTERACT = "naughty.entity-interact";
    public static final String NAUGHTY_ENTITY_ATTACK = "naughty.entity-attack";
    public static final String NAUGHTY_DEATH = "naughty.death";

    // Naught admin alert strings
    public static final String NAUGHTY_ADMIN_PLACE = "naughty.admin.place";
    public static final String NAUGHTY_ADMIN_BREAK = "naughty.admin.break";
    public static final String NAUGHTY_ADMIN_COMMAND = "naughty.admin.command";
    public static final String NAUGHTY_ADMIN_INTERACTION = "naughty.admin.interact";
    public static final String NAUGHTY_ADMIN_USE = "naughty.admin.use";
    public static final String NAUGHTY_ADMIN_DROP = "naughty.admin.drop";
    public static final String NAUGHTY_ADMIN_PICKUP = "naughty.admin.pickup";
    public static final String NAUGHTY_ADMIN_ENTITY_PLACE = "naughty.admin.entity-place";
    public static final String NAUGHTY_ADMIN_ENTITY_BREAK = "naughty.admin.entity-break";
    public static final String NAUGHTY_ADMIN_ENTITY_INTERACT = "naughty.admin.entity-interact";
    public static final String NAUGHTY_ADMIN_ENTITY_ATTACK = "naughty.admin.entity-attack";
    public static final String NAUGHTY_ADMIN_DEATH = "naughty.admin.death";

    // Help strings
    public static final String HELP_TITLE = "help.title";
    public static final String HELP_LINE = "help.line";
    public static final String HELP_CMD_HELP = "help.command.help";
    public static final String HELP_CMD_TOOLS = "help.command.tools";
    public static final String HELP_CMD_RELOAD = "help.command.reload";

    private static Lang instance;
    private FileConfiguration configuration;

    private Lang() {
        reload();

        // TODO: Find out what causes this to fail otherwise... wtf.
        reload();
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
     * @return the format from the file. If the key is null or not found, this returns a default message
     */
    public String getFormat(String key) {
        if (key == null) return null;

        return configuration.getString(key, "Corrupt/missing key: " + key + ". Try /as reload?");
    }

    /**
     * Gets the format, as a list, for a specified key
     *
     * @param key the key to lookup
     *
     * @return the format from the file. If the key is null or not found, this returns a default list
     */
    public List<String> getFormatList(String key) {
        if (key == null) return null;

        List<String> list = configuration.getStringList(key);
        if (list == null)
            list = new ArrayArrayList<String>("Corrupt/missing key: " + key + ". Try /as reload?");

        return list;
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
