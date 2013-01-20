/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.regions;

import java.util.logging.Level;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.notification.Alert.AlertTrigger;
import com.turt2live.antishare.notification.Alert.AlertType;
import com.turt2live.antishare.permissions.PermissionNodes;

/**
 * Represents a World Split
 * 
 * @author turt2live
 */
public class WorldSplit {

	/**
	 * An enum to represent a World Split Axis
	 * 
	 * @author turt2live
	 */
	public static enum Axis{
		X,
		Z,
		NONE;

		/**
		 * Gets an axis from a string
		 * 
		 * @param axis the string
		 * @return the axis
		 */
		public static Axis getAxis(String axis){
			if(axis.equalsIgnoreCase("X")){
				return Axis.X;
			}else if(axis.equalsIgnoreCase("Z")){
				return Axis.Z;
			}
			return Axis.NONE;
		}
	}

	private String world;
	private double split;
	private Axis axis;
	private GameMode positive, negative;
	private double blockWarn = 15;
	private boolean warn = false;
	private long warnEvery = 2;
	private AntiShare plugin;

	/**
	 * Creates a new World Split
	 * 
	 * @param world the world
	 * @param split the axis value
	 * @param axis the axis
	 * @param positive the side for values >split
	 * @param negative the side for values <split
	 */
	public WorldSplit(String world, double split, Axis axis, GameMode positive, GameMode negative){
		this.world = world;
		this.split = split;
		this.axis = axis;
		this.positive = positive;
		this.negative = negative;
		this.plugin = AntiShare.getInstance();
		checkValues();
	}

	/**
	 * Sets up the warning options
	 * 
	 * @param warn true to warn, false otherwise
	 * @param before the number of blocks to warn the user at
	 * @param warnEvery the number of milliseconds to warn the user (eg 2000 = 2seconds)
	 */
	public void warning(boolean warn, double before, long warnEvery){
		this.warn = warn;
		this.blockWarn = before;
		this.warnEvery = warnEvery;
	}

	/**
	 * Warn a player if required
	 * 
	 * @param player the player
	 */
	public void warn(Player player){
		Location location = player.getLocation();
		double distance = (axis == Axis.X ? location.getX() : location.getZ()) - split;

		if(axis == Axis.NONE || !warn){
			return;
		}

		if(Math.abs(distance) <= blockWarn){
			String playerMessage = plugin.getMessage("world-split.close-to-split");
			plugin.getAlerts().alert("none", player, playerMessage, AlertType.REGION, AlertTrigger.CLOSE_TO_WORLD_SPLIT, warnEvery);
		}
	}

	/**
	 * Checks a player for the world split
	 * 
	 * @param player the player
	 */
	public void checkPlayer(Player player){
		if(axis.equals(Axis.NONE)){
			return;
		}

		// Look for split
		double value = axis.equals(Axis.X) ? player.getLocation().getX() : player.getLocation().getZ();
		GameMode gamemode = getGameMode(value, player.getGameMode());

		// Sanity 
		if(player.getGameMode().equals(gamemode)){
			return;
		}

		// Check permissions for the side they are traveling
		if(!plugin.getPermissions().has(player, (gamemode == GameMode.CREATIVE ? PermissionNodes.WORLD_SPLIT_NO_SPLIT_CREATIVE : PermissionNodes.WORLD_SPLIT_NO_SPLIT_SURVIVAL))){
			player.setGameMode(gamemode);
		}
	}

	/**
	 * Gets the side of the split a player is on, or null is not affected
	 * 
	 * @param player the player
	 * @return the side of the split
	 */
	public GameMode getSide(Player player){
		GameMode gamemode = player.getGameMode();
		if(plugin.getPermissions().has(player, (gamemode == GameMode.CREATIVE ? PermissionNodes.WORLD_SPLIT_NO_SPLIT_CREATIVE : PermissionNodes.WORLD_SPLIT_NO_SPLIT_SURVIVAL))){
			return null;
		}

		double value = axis.equals(Axis.X) ? player.getLocation().getX() : player.getLocation().getZ();
		gamemode = getGameMode(value, player.getGameMode());
		return gamemode;
	}

	// Gets the gamemode, or returns default if something is wrong
	private GameMode getGameMode(double point, GameMode defaultMode){
		if(point > split){
			// Positive
			return positive;
		}else if(point < split){
			// Negative
			return negative;
		}
		return defaultMode;
	}

	// Checks to ensure the values are correct
	private void checkValues(){
		if(positive != negative && positive != null && negative != null){
			// Valid
		}else{
			axis = Axis.NONE;
			plugin.log("Invalid world split for world " + world, Level.WARNING);
		}
	}
}
