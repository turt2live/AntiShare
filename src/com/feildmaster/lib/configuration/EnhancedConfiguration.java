package com.feildmaster.lib.configuration;

import java.io.*;
import java.util.*;
import org.bukkit.configuration.*;
import org.bukkit.plugin.Plugin;

// DefaultSections - May not go with this
// Watch File and Reload manual edits?
//  - Threaded saving on timer? :O
// Path Comments
//  - The next thing to code

/**
 * Enhancing configuration to do the following:
 * <li>Stores a file for configuration to use.</li>
 * <li>Self contained "load," "reload," and "save" functions.</li>
 * <li>Self contained "loadDefaults" functions that set defaults.</li>
 * <li>Adds "getLastException" to return the last exception from self contained functions.</li>
 * <li>Adds "options().header(String, String)" to build multiline headers easier(?)</li>
 *
 * @author Feildmaster
 */
public class  EnhancedConfiguration extends org.bukkit.configuration.file.YamlConfiguration {
    private final File file;
    private final Plugin plugin;
    private Exception exception;

    /**
     * Creates a new EnhancedConfiguration with a file named "config.yml," stored in the plugin DataFolder
     *
     * @param plugin The plugin registered to this Configuration
     */
    public EnhancedConfiguration(Plugin plugin) {
        this("config.yml", plugin);
    }

    /**
     * Creates a new EnhancedConfiguration with a file stored in the plugin DataFolder
     *
     * @param file The name of the file
     * @param plugin The plugin registered to this Configuration
     */
    public EnhancedConfiguration(String file, Plugin plugin) {
        this(new File(plugin.getDataFolder(), file), plugin);
    }

    /**
     * Creates a new EnhancedConfiguration with given File and Plugin.
     *
     * @param file The file to store in this configuration
     * @param plugin The plugin registered to this Configuration
     */
    public EnhancedConfiguration(File file, Plugin plugin) {
        this.file = file;
        this.plugin = plugin;
        options = new EnhancedConfigurationOptions(this);

        load();
    }

    /**
     * Loads set file
     * <p>
     * Stores exception if possible.
     * </p>
     *
     * @return True on successful load
     */
    public final boolean load() {
        try {
            load(file);
            return true;
        } catch (Exception ex) {
            exception = ex;
            return false;
        }
    }

    /**
     * Saves to the set file
     * <p>
     * Stores exception if possible.
     * </p>
     *
     * @return True on successful save
     */
    public final boolean save() {
        try {
            save(file);
            return true;
        } catch (Exception ex) {
            exception = ex;
            return false;
        }
    }

    /**
     * Returns the last stored exception
     *
     * @return Last stored Exception
     */
    public Exception getLastException() {
        return exception;
    }

    /**
     * Loads defaults based off the name of stored file.
     * <p>
     * Stores exception if possible.
     * </p>
     *
     * @return True on success
     */
    public boolean loadDefaults() {
        try {
            return loadDefaults(file.getName());
        } catch (Exception ex) {
            exception = ex;
            return false;
        }
    }

    /**
     * Sets your defaults after loading the Plugin file.
     * <p>
     * Stores exception if possible.
     * </p>
     *
     * @param filename File to load from Plugin jar
     * @return True on success
     */
    public boolean loadDefaults(String filename) {
        try {
            return loadDefaults(plugin.getResource(filename));
        } catch (Exception ex) {
            exception = ex;
            return false;
        }
    }

    /**
     * Sets your defaults after loading them.
     * <p>
     * Stores exception if possible.
     * </p>
     *
     * @param filestream Stream to load defaults from
     * @return True on success, false otherwise.
     */
    public boolean loadDefaults(InputStream filestream) {
        try {
            setDefaults(loadConfiguration(filestream));
            return true;
        } catch (Exception ex) {
            exception = ex;
            return false;
        }
    }

    /**
     * Saves configuration with all defaults
     *
     * @return True if saved
     */
    public boolean saveDefaults() {
        options().copyDefaults(true);
        options().copyHeader(true);
        boolean saved = save();
        options().copyDefaults(false);
        options().copyHeader(false);

        return saved;
    }

    /**
     * Check loaded defaults against current configuration
     *
     * @return false When all defaults aren't present in config
     */
    public boolean checkDefaults() {
        if (getDefaults() == null) {
            return true;
        }
        return getKeys(true).containsAll(getDefaults().getKeys(true));
    }

    /**
     * 
     *
     * @return True if file exists, False if not, or if there was an exception.
     */
    public boolean fileExists() {
        try {
            return file.exists();
        } catch (Exception ex) {
            exception = ex;
            return false;
        }
    }

    // Custom Options
    /**
     * Get the options
     *
     * @return Enhanced Options
     */
    public EnhancedConfigurationOptions options() {
        return (EnhancedConfigurationOptions) options;
    }

    // Cache System
    private Map<String, Object> cache = new HashMap<String, Object>();
    public Object get(String path, Object def) {
        Object value = cache.get(path);
        if (value != null) {
            return value;
        }

        value = super.get(path, def);
        if (value != null) {
            cache.put(path, value);
        }

        return value;
    }
    public void set(String path, Object value) {
        if (value == null && cache.containsKey(path)) {
            cache.remove(path);
        } else if (value != null) {
            cache.put(path, value);
        }
        super.set(path, value);
    }

    // Header Overrides... To fix line breaks
    protected String parseHeader(String input) {
        return super.parseHeader(input);
    }
    protected String buildHeader() {
        return super.buildHeader();
    }

    // Custom Yaml Loader... Later
    public String saveToString() {
        return super.saveToString();
    }
    public void loadFromString(String contents) throws InvalidConfigurationException {
        super.loadFromString(contents);
    }
}