package com.turt2live.antishare.compatibility.type;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Used for plugins such as LogBlock
 * 
 * @author turt2live
 */
public abstract class BlockLogger {

	public static final String PLAYER_NAME = "AntiSharePlugin";
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

}
