package com.turt2live.antishare.bukkit.lang;

import com.turt2live.antishare.bukkit.AntiShare;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * AntiShare Language File
 *
 * @author turt2live
 */
public class Lang {

    public static final String PREFIX = "prefix";
    public static final String TOOL_CHECK_TITLE = "tools.check-title";
    public static final String TOOL_SET_TITLE = "tools.set-title";
    public static final String TOOL_CHECK_LORE = "tools.check-lore";
    public static final String TOOL_SET_LORE = "tools.set-lore";
    public static final String TOOL_ON_SET = "tools.on-set";
    public static final String TOOL_ON_UNSET = "tools.on-unset";
    public static final String TOOL_ON_CHECK = "tools.on-check";
    public static final String TOOL_ON_GET = "tools.on-get";
    public static final String ERROR_INTERNAL = "errors.internal";
    public static final String ERROR_NOT_A_PLAYER = "errors.not-a-player";
    public static final String ERROR_NO_PERMISSION = "errors.permission";
    public static final String ERROR_SYNTAX = "errors.syntax";
    public static final String ERROR_HELP_SUGGEST = "errors.help-suggest";

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
