package com.turt2live.antishare.regions;

import java.util.logging.Level;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.AntiShare.LogType;
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
	public enum Axis{
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

	private World world;
	private double split;
	private Axis axis;
	private double creative;
	private double survival;
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
	 * @param creative the creative side
	 * @param survival the survival side
	 */
	public WorldSplit(World world, double split, Axis axis, double creative, double survival){
		this.world = world;
		this.split = split;
		this.axis = axis;
		this.creative = creative;
		this.survival = survival;
		this.plugin = AntiShare.getInstance();
		checkValues();
	}

	/**
	 * Sets up the warning options
	 * 
	 * @param warn true to warn, false otherwise
	 * @param before the number of blocks to warn the user at
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
			if(creative > survival){
				return GameMode.CREATIVE;
			}else if(survival > creative){
				return GameMode.SURVIVAL;
			}
		}else if(point < split){
			if(creative < survival){
				return GameMode.CREATIVE;
			}else if(survival < creative){
				return GameMode.SURVIVAL;
			}
		}
		return defaultMode;
	}

	// Checks to ensure the values are correct
	private void checkValues(){
		if(creative > survival){
			creative = 1;
			survival = 0;
		}else if(survival > creative){
			survival = 1;
			creative = 0;
		}else{
			axis = Axis.NONE;
			plugin.getMessenger().log("Invalid world split for world " + world.getName(), Level.WARNING, LogType.BYPASS);
		}
	}
}
