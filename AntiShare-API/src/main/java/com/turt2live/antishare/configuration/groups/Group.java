package com.turt2live.antishare.configuration.groups;

import com.turt2live.antishare.configuration.Configuration;
import com.turt2live.antishare.engine.BlockTypeList;
import com.turt2live.antishare.utils.ASGameMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an AntiShare group
 *
 * @author turt2live
 */
public abstract class Group {

    /**
     * The configuration object for this group
     */
    protected final Configuration configuration;
    /**
     * The manager object for this group
     */
    protected final GroupManager manager;

    /**
     * Creates a new group from the configuration and manager supplied
     *
     * @param configuration the configuration to be used, cannot be null
     * @param manager       the manager to use, cannot be null
     */
    public Group(Configuration configuration, GroupManager manager) {
        if (configuration == null || manager == null) throw new IllegalArgumentException("arguments cannot be null");

        this.configuration = configuration;
        this.manager = manager;
    }

    /**
     * Gets a list of inherited group names
     *
     * @return a list of inherited groups, or an empty list
     */
    public List<String> getInheritedGroups() {
        return configuration.getStringList("inherit", new ArrayList<String>());
    }

    /**
     * Gets the applicable worlds for this group. Entries listed "all" will
     * indicate "all worlds".
     *
     * @return the applicable worlds, never null but may be empty
     */
    public List<String> getApplicableWorlds() {
        return configuration.getStringList("worlds", new ArrayList<String>());
    }

    /**
     * Gets the name of this group
     *
     * @return the name of this group
     */
    public String getName() {
        return configuration.getString("name", "UnknownGroup");
    }

    /**
     * Determines whether or not this group is enabled
     *
     * @return true if enabled, or false otherwise
     */
    public boolean isEnabled() {
        return configuration.getBoolean("enabled", true);
    }

    /**
     * Gets the block list for a specified GameMode. This does not include
     * inherited groups.
     *
     * @param gameMode the gamemode to lookup
     * @return the block list
     */
    public abstract BlockTypeList getBlockList(ASGameMode gameMode);

    /**
     * Gets the acting gamemode for a specified gamemode. This does not include
     * inherited groups.
     *
     * @param gameMode the gamemode to lookup
     * @return the gamemode the specified gamemode should act as
     */
    public abstract ASGameMode getActingMode(ASGameMode gameMode);

}
