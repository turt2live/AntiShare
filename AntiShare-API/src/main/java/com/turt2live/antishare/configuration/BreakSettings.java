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

package com.turt2live.antishare.configuration;

import com.turt2live.antishare.object.attribute.ASGameMode;

/**
 * Small class for holding information about break settings
 *
 * @author turt2live
 */
public final class BreakSettings {

    /**
     * True for denial, false otherwise
     */
    public final boolean denyAction;
    /**
     * The gamemode to break as, never null
     */
    public final ASGameMode breakAs;

    /**
     * Creates a new BreakSettings object
     *
     * @param deny    true to deny the action, false otherwise
     * @param breakAs the gamemode to break something as, cannot be null
     */
    public BreakSettings(boolean deny, ASGameMode breakAs) {
        if (breakAs == null) throw new IllegalArgumentException("break as cannot be null");

        this.denyAction = deny;
        this.breakAs = breakAs;
    }

}
