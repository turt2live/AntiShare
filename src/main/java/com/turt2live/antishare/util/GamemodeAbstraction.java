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
package com.turt2live.antishare.util;

import com.turt2live.antishare.AntiShare;
import org.bukkit.GameMode;

/**
 * Class to determine GameMode similarities
 *
 * @author turt2live
 */
public class GamemodeAbstraction {

    /**
     * Determines if AntiShare sees 2 GameModes as the same
     *
     * @param gm1 first gamemode
     * @param gm2 second gamemode
     * @return true if AntiShare sees gm1 and gm2 as the same
     */
    public static boolean isMatch(GameMode gm1, GameMode gm2) {
        if (isCreative(gm1) && isCreative(gm2)) {
            return true;
        }
        return gm1 == gm2;
    }

    /**
     * Determines if a GameMode is just like Creative Mode (to AntiShare)
     *
     * @param gamemode the gamemode
     * @return true if AntiShare sees gm1 as Creative Mode
     */
    public static boolean isCreative(GameMode gamemode) {
        if (gamemode == null) {
            return false;
        }
        if (!isAdventureCreative()) {
            return gamemode == GameMode.CREATIVE;
        }
        return gamemode == GameMode.CREATIVE || gamemode == GameMode.ADVENTURE;
    }

    /**
     * Determines if AntiShare will be seeing Adventure mode and Creative mode as the same
     *
     * @return true if Adventure and Creative mode are the same (to AntiShare)
     */
    public static boolean isAdventureCreative() {
        AntiShare plugin = AntiShare.p;
        return plugin.settings().adventureEqCreative;
    }

}
