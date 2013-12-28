/*******************************************************************************
 * Copyright (c) 2013 Travis Ralston.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.io;

import com.feildmaster.lib.configuration.EnhancedConfiguration;

public class GameModeIdentity extends GenericDataFile {

    /**
     * Determines if a player has changed GameMode before
     *
     * @param player the player to check
     * @return true if they have changed Game Mode before
     */
    public static boolean hasChangedGameMode(String player) {
        EnhancedConfiguration yaml = getFile("gamemodeswitches");
        return yaml.getBoolean(player, false);
    }

    /**
     * Sets a player as "has changed Game Mode"
     *
     * @param player the player
     */
    public static void setChangedGameMode(String player) {
        EnhancedConfiguration yaml = getFile("gamemodeswitches");
        yaml.set(player, true);
        yaml.save();
    }

}
