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
package com.turt2live.antishare.compatibility.type;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Used for plugins such as LogBlock
 * 
 * @author turt2live
 */
public abstract class BlockLogger{

	/**
	 * Default player name to inject into Block Loggers
	 */
	public static final String PLAYER_NAME = "AntiSharePlugin";
	/**
	 * Default data byte
	 */
	public static final byte DEFAULT_DATA = 0;

	/**
	 * Breaks a block as the AntiShare player
	 * 
	 * @param playerName the player name involved, or null for no associated player
	 * @param location the location
	 * @param before the previous material
	 * @param data the previous data
	 */
	public abstract void breakBlock(String playerName, Location location, Material before, byte data);

	/**
	 * Places a block as the AntiShare player
	 * 
	 * @param playerName the player name involved, or null for no associated player
	 * @param location the location
	 * @param after the new material
	 * @param data the previous data
	 */
	public abstract void placeBlock(String playerName, Location location, Material after, byte data);

	/**
	 * Breaks an entity as the AntiShare player
	 * 
	 * @param playerName the player name involved, or null for no associated player
	 * @param location the location
	 * @param before the previous material
	 * @param data the previous data
	 */
	public abstract void breakHanging(String playerName, Location location, Material before, byte data);

	/**
	 * Places an entity as the AntiShare player
	 * 
	 * @param playerName the player name involved, or null for no associated player
	 * @param location the location
	 * @param after the new material
	 * @param data the previous data
	 */
	public abstract void placeHanging(String playerName, Location location, Material after, byte data);

}
