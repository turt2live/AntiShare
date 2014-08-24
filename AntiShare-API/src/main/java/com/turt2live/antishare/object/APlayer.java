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

package com.turt2live.antishare.object;

import com.turt2live.antishare.ASGameMode;

/**
 * Represents an AntiShare player
 *
 * @author turt2live
 */
public interface APlayer extends AEntity {

    /**
     * Gets the player's name
     *
     * @return the player's name
     */
    public String getName();

    /**
     * Gets the player's Game Mode
     *
     * @return the player's Game Mode
     */
    public ASGameMode getGameMode();

    /**
     * Sets the player's Game Mode
     *
     * @param gameMode the new Game Mode to use, cannot be null
     */
    public void setGameMode(ASGameMode gameMode);

    /**
     * Determines if a player has a permission
     *
     * @param permission the permission to check, cannot be null
     *
     * @return true if the player has the permission, false otherwise
     */
    public boolean hasPermission(String permission);

    /**
     * Sends a chat message to the player. Internally chat colors are converted as
     * appropriate.
     *
     * @param message the message to send, cannot be null
     */
    public void sendMessage(String message);

    /**
     * Gets the player's current world
     *
     * @return the player's current world
     */
    public AWorld getWorld();

    /**
     * Gets the current inventory for this player
     *
     * @return the current inventory
     */
    public AInventory getInventory();

    /**
     * Sets the inventory of this player to the specified items. Implementations
     * should not copy the gamemode or world of the inventory, just the contents.
     *
     * @param inventory the inventory for which the contents should be used. If null,
     *                  the inventory should be cleared.
     */
    public void setInventory(AInventory inventory);

}
