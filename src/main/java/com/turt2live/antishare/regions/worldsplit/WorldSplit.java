package com.turt2live.antishare.regions.worldsplit;

import org.bukkit.GameMode;
import org.bukkit.Location;

import com.turt2live.antishare.AntiShare;

/**
 * Represents a world split
 * 
 * @author turt2live
 */
public class WorldSplit {

	public static enum Axis {
		X, Z, UNKNOWN;

		public static Axis fromString(String val) {
			if (val.equalsIgnoreCase("X")) {
				return X;
			} else if (val.equalsIgnoreCase("Z")) {
				return Z;
			}
			return UNKNOWN;
		}
	}

	private String worldName = "world";
	private Axis axis = Axis.UNKNOWN;
	private int value = 0, warnDistance = 5;
	private boolean warn = false;
	private GameMode positive = GameMode.CREATIVE, negative = GameMode.SURVIVAL;
	private AntiShare plugin = AntiShare.p;

	public WorldSplit(Axis axis, String worldName, int value, GameMode positive, GameMode negative, boolean warn, int warnValue) {
		this.axis = axis;
		this.worldName = worldName;
		this.value = value;
		this.positive = positive;
		this.negative = negative;
		this.warn = warn;
		this.warnDistance = warn ? warnValue : 1;
	}

	/**
	 * Gets whether or not a warning is enabled on this world split
	 * 
	 * @return true if this world split should warn
	 */
	public boolean getShouldWarn() {
		return warn;
	}

	/**
	 * Gets the distance this world split warns from
	 * 
	 * @return the distance to warn from
	 */
	public int getWarnDistance() {
		return warnDistance;
	}

	/**
	 * Determines if this world split is valid
	 * 
	 * @return true if valid
	 */
	public boolean isValid() {
		return worldName != null && plugin.getServer().getWorld(worldName) != null && axis != null
				&& axis != Axis.UNKNOWN && positive != null && negative != null && positive != negative
				&& warnDistance > 0;
	}

	/**
	 * Gets the value this world is split on. Default is 0
	 * 
	 * @return the value of the split.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Gets the axis of the world split
	 * 
	 * @return the axis
	 */
	public Axis getAxis() {
		return axis;
	}

	/**
	 * Gets the gamemode for a respective side of this split. Can be null
	 * 
	 * @param location the location
	 * @return the gamemode for the side this location resides on. If the location is null then this will be null, if {@link #getAxis()} is UNKNOWN or null then this is also null.
	 */
	public GameMode getGameModeForSide(Location location) {
		if (location == null) {
			return null;
		}
		int v = 0;
		switch (axis) {
		case X:
			v = location.getBlockX();
			break;
		case Z:
			v = location.getBlockZ();
			break;
		default:
			return null;
		}
		if (v <= value) {
			return negative;
		}
		return positive;
	}

}
