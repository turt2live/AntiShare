/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

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
    public final List<String> getInheritedGroups() {
        return new ArrayList<>();
    }

    @Override
    public final List<String> getApplicableWorlds() {
        return new ArrayArrayList<>("all");
    }

    @Override
    public final String getName() {
        return "main";
    }

    @Override
    public final boolean isEnabled() {
        return true;
    }

}
