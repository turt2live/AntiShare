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

import org.bukkit.GameMode;

import com.feildmaster.lib.configuration.EnhancedConfiguration;

public class MoneySaver extends GenericDataFile {

	/**
	 * Gets the balance for a player
	 * 
	 * @param player the player
	 * @param gamemode the gamemode to get the balance for
	 * @return a double (balance). If not found this will return 0
	 */
	public static double getLevel(String player, GameMode gamemode) {
		EnhancedConfiguration yaml = getFile("balance");
		double balance = yaml.getDouble(player + "." + gamemode.name(), 0.0);
		return balance;
	}

	/**
	 * Saves a balance for a player
	 * 
	 * @param player the player
	 * @param gamemode the gamemode to save as
	 * @param balance the balance to save
	 */
	public static void saveLevel(String player, GameMode gamemode, double balance) {
		if (balance <= 0) {
			return;
		}
		EnhancedConfiguration yaml = getFile("balance");
		yaml.set(player + "." + gamemode.name(), balance);
		yaml.save();
	}

}
