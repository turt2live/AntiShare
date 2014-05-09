package com.turt2live.antishare.configuration.groups;

import com.turt2live.antishare.object.APlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Manages groups
 *
 * @author turt2live
 */
// TODO: Unit test
public abstract class GroupManager {

    protected final ConcurrentMap<String, Group> groups = new ConcurrentHashMap<String, Group>();
    protected MainGroup mainGroup;

    /**
     * Loads all groups from the storage system
     */
    public abstract void loadAll();

    /**
     * Gets the main group
     *
     * @return the main group
     */
    public MainGroup getMainGroup() {
        return mainGroup;
    }

    /**
     * Gets a group by name
     *
     * @param name the group name
     *
     * @return the group, or null if not found
     */
    public Group getGroup(String name) {
        if (name == null) return null;
        if (name.equals(mainGroup.getName())) return mainGroup;
        return groups.get(name);
    }

    /**
     * Clears all groups from the system, including the main group
     */
    public void clear() {
        groups.clear();
        mainGroup = null;
    }

    /**
     * Gets a listing of all groups a specified group inherits from
     *
     * @param group the group to get the inheritance tree from. Returns an empty list on null input
     *
     * @return the inherited groups, never null but may be empty
     */
    public List<Group> getInheritances(Group group) {
        if (group == null) return new ArrayList<Group>();

        List<Group> groups = new ArrayList<Group>();

        for (String name : group.getInheritedGroups()) {
            groups.addAll(getInheritances(getGroup(name)));
        }

        return groups;
    }

    /**
     * Gets all the groups for the specified world
     *
     * @param world           the world to lookup, null returns an empty list
     * @param includeDisabled if true, disabled groups will be included in the result set
     *
     * @return the applicable groups, or an empty list
     */
    public List<Group> getGroupsForWorld(String world, boolean includeDisabled) {
        if (world == null) return new ArrayList<Group>();

        List<Group> groups = new ArrayList<Group>();
        for (Group group : this.groups.values()) {
            List<String> worlds = group.getApplicableWorlds();
            if (worlds.contains("all") || worlds.contains(world))
                if (group.isEnabled() || includeDisabled) groups.add(group);
        }

        List<String> worlds = mainGroup.getApplicableWorlds();
        if (worlds.contains("all") || worlds.contains(world))
            if (mainGroup.isEnabled() || includeDisabled) groups.add(mainGroup);

        return groups;
    }

    /**
     * Gets a list of all groups
     *
     * @param includeDisabled if true, disabled groups will be included in the result set
     *
     * @return the applicable groups, or an empty list
     */
    public List<Group> getAllGroups(boolean includeDisabled) {
        List<Group> groups = new ArrayList<Group>();

        for (Group group : this.groups.values()) {
            if (group.isEnabled() || includeDisabled) groups.add(group);
        }

        if (mainGroup.isEnabled() || includeDisabled) groups.add(mainGroup);

        return groups;
    }

    /**
     * Gets a listing of applicable groups to a player, including inherited groups
     *
     * @param player          the player to lookup, cannot be null
     * @param includeDisabled if true, disabled groups will be included in the result set
     *
     * @return the list of groups. May be empty but never null
     */
    public List<Group> getGroupsForPlayer(APlayer player, boolean includeDisabled) {
        List<Group> groups = new ArrayList<Group>();

        for (Group group : this.groups.values()) {
            if (player.hasPermission(group.getPermission())) {
                if (group.isEnabled() || includeDisabled) {
                    addIfNotFound(groups, group);
                    addIfNotFound(groups, getInheritances(group).toArray(new Group[0])); // All inherited groups are automatic
                }
            }
        }

        if (player.hasPermission(mainGroup.getPermission()) && (mainGroup.isEnabled() || includeDisabled)) {
            addIfNotFound(groups, mainGroup);
            addIfNotFound(groups, getInheritances(mainGroup).toArray(new Group[0])); // All inherited groups are automatic
        }

        if (groups.size() <= 0) {
            addIfNotFound(groups, mainGroup);
            addIfNotFound(groups, getInheritances(mainGroup).toArray(new Group[0])); // All inherited groups are automatic
        }

        return groups;
    }

    private void addIfNotFound(List<Group> list, Group... groups) {
        for (Group group : groups) {
            if (!list.contains(group)) list.add(group);
        }
    }
}
