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
package com.turt2live.antishare.inventory;

import org.bukkit.GameMode;
import org.bukkit.World;

/**
 * Linked Inventory
 */
public class LinkedInventory {

	private String[] worlds;
	private GameMode gamemode;

	/**
	 * Creates a new Linked Inventory
	 * 
	 * @param affectedGameMode the gamemode
	 * @param worlds the worlds to link
	 */
	public LinkedInventory(GameMode affectedGameMode, String... worlds) {
		this.gamemode = affectedGameMode;
		this.worlds = worlds;
	}

	/**
	 * Determines if a world is affected by the Linked Inventory
	 * 
	 * @param world the world
	 * @return true if affected, false otherwise
	 */
	public boolean isWorldAffected(World world) {
		for (String w : worlds) {
			if (world.getName().equalsIgnoreCase(w)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines if the supplied Game Mode matches the designated Game Mode
	 * 
	 * @param gamemode the game mode to test
	 * @return true if this linked inventory applies to this gamemode, false otherwise
	 */
	public boolean isGameModeAffected(GameMode gamemode) {
		return gamemode == this.gamemode;
	}

	/**
	 * Gets a cloned array of the affected worlds
	 * 
	 * @return a cloned array of affected worlds
	 */
	public String[] getAffectedWorlds() {
		return worlds.clone();
	}

}
