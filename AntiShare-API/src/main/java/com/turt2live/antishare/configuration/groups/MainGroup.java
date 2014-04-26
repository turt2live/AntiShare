package com.turt2live.antishare.configuration.groups;

import com.turt2live.antishare.collections.ArrayArrayList;
import com.turt2live.antishare.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the MainGroup for AntiShare
 *
 * @author turt2live
 */
// TODO: Unit test
public abstract class MainGroup extends Group {

    /**
     * Creates a new main group from the configuration and manager supplied
     *
     * @param configuration the configuration to be used, cannot be null
     */
    public MainGroup(Configuration configuration) {
        super(configuration);
    }

    @Override
    public List<String> getInheritedGroups() {
        return new ArrayList<String>();
    }

    @Override
    public List<String> getApplicableWorlds() {
        return new ArrayArrayList<String>(new String[]{"all"});
    }

    @Override
    public String getName() {
        return "main";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
