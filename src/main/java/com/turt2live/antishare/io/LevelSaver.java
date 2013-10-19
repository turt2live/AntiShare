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
import org.bukkit.entity.Player;

import com.feildmaster.lib.configuration.EnhancedConfiguration;

public class LevelSaver extends GenericDataFile {

	/**
	 * Stores a level
	 */
	public static class Level {

		/**
		 * Level (eg: 40)
		 */
		public final int level;
		/**
		 * Percentage (eg 0.5 = 50% to next level)
		 */
		public final float percent;

		/**
		 * Creates a new level
		 * 
		 * @param level the level
		 * @param percent percent, as a decimal, to next level
		 */
		public Level(int level, float percent) {
			this.level = level;
			this.percent = percent;
		}

		/**
		 * Assigns this level to a player
		 * 
		 * @param player the player to apply
		 */
		public void setTo(Player player) {
			player.setLevel(level);
			player.setExp(percent);
		}

	}

	/**
	 * Gets the level for a player
	 * 
	 * @param player the player
	 * @param gamemode the gamemode to get the level for
	 * @return a level. If not found this will return a level of 0 with 0% to the next level.
	 */
	public static Level getLevel(String player, GameMode gamemode) {
		EnhancedConfiguration file = getFile("levels");
		if (!file.isSet(player + "." + gamemode.name())) {
			return new Level(0, 0);
		}
		float percent = (float) file.getDouble(player + "." + gamemode.name() + ".percent", 0f);
		int level = file.getInt(player + "." + gamemode.name() + ".level", 0);
		return new Level(level, percent);
	}

	/**
	 * Saves a level for a player
	 * 
	 * @param player the player
	 * @param gamemode the gamemode to save as
	 * @param level the level to save
	 */
	public static void saveLevel(String player, GameMode gamemode, Level level) {
		if (level.level == 0 && level.percent < 0.01) {
			return;
		}
		EnhancedConfiguration file = getFile("levels");
		file.set(player + "." + gamemode.name() + ".level", level.level);
		file.set(player + "." + gamemode.name() + ".percent", level.percent);
		file.save();
	}

}
