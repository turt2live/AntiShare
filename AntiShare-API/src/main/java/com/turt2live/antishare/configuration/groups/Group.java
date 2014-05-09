package com.turt2live.antishare.configuration.groups;

import com.turt2live.antishare.configuration.BreakSettings;
import com.turt2live.antishare.configuration.Configuration;
import com.turt2live.antishare.engine.list.BlockTypeList;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.object.attribute.ASGameMode;

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
     * Creates a new group from the configuration and manager supplied
     *
     * @param configuration the configuration to be used, cannot be null
     */
    public Group(Configuration configuration) {
        if (configuration == null) throw new IllegalArgumentException("arguments cannot be null");

        this.configuration = configuration;
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
     * Gets the permission required to apply this group to a player
     *
     * @return the required permission
     */
    public String getPermission() {
        return configuration.getString("permission-enable", "AntiShare.group." + getName());
    }

    /**
     * Gets the acting gamemode for a specified gamemode. This does not include
     * inherited groups.
     *
     * @param gameMode the gamemode to lookup
     *
     * @return the gamemode the specified gamemode should act as
     */
    public ASGameMode getActingMode(ASGameMode gameMode) {
        if (gameMode == null) throw new IllegalArgumentException("gamemode cannot be null");
        String acting = configuration.getString("blocks.gamemode-settings." + gameMode.name().toLowerCase(), gameMode.name().toLowerCase());
        ASGameMode gm1 = ASGameMode.fromString(acting);
        if (gm1 != null) return gm1;
        return gameMode;
    }

    /**
     * Gets the break settings for this group for a specified gamemode
     * breaking a specified gamemode's block.
     *
     * @param gamemode the gamemode breaking the block, cannot be null
     * @param breaking the gamemode of the block, cannot be null
     *
     * @return the applicable break settings
     */
    public BreakSettings getBreakSettings(ASGameMode gamemode, ASGameMode breaking) {
        if (gamemode == null || breaking == null) throw new IllegalArgumentException("gamemodes cannot be null");
        String breakingStr = gamemode.name().toLowerCase() + "-breaking-" + breaking.name().toLowerCase();
        boolean deny = configuration.getBoolean("blocks.break." + breakingStr + ".deny", true);
        String breakAsStr = configuration.getString("blocks.break." + breakingStr + ".break-as", breaking.name().toLowerCase());
        ASGameMode breakAs = ASGameMode.fromString(breakAsStr);
        if (breakAs == null) breakAs = breaking;
        return new BreakSettings(deny, breakAs);
    }

    /**
     * Gets the block tracking list for a specified GameMode. This does not include
     * inherited groups.
     *
     * @param gameMode the gamemode to lookup
     *
     * @return the block list
     */
    public abstract BlockTypeList getTrackedList(ASGameMode gameMode);

    /**
     * Gets the rejection list for a specified list type
     *
     * @param list the list type
     *
     * @return the list found, or null if none exists
     */
    public abstract RejectionList getRejectionList(RejectionList.ListType list);
}
