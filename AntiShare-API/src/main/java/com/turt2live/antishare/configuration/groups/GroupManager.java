package com.turt2live.antishare.configuration.groups;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Manages groups
 *
 * @author turt2live
 */
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
}
