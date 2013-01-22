package com.turt2live.antishare.tekkitcompat;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class ServerHas {

	private static boolean isClass(String clazz){
		try{
			return Class.forName(clazz) != null;
		}catch(ClassNotFoundException e){}
		return false;
	}

	private static boolean isMethod(String method, Class<?> clazz){
		try{
			return clazz.getMethod(method) != null;
		}catch(SecurityException e){}catch(NoSuchMethodException e){}
		return false;
	}

	/**
	 * Determines if the server supports tab completion commands
	 * @return true if tab complete can be found
	 */
	public static boolean tabComplete(){
		return isClass("org.bukkit.command.TabCompleter");
	}

	/**
	 * Determines if the server supports adventure mode (or has the capability of)
	 * @return true if the server can support adventure mode
	 */
	public static boolean adventureMode(){
		try{
			return GameMode.valueOf("ADVENTURE") != null;
		}catch(IllegalArgumentException e){}
		return false;
	}

	/**
	 * Determines if the server has the 1.4.X items
	 * @return true if 1.4.X items can be found
	 */
	public static boolean mc14xItems(){
		return isClass("org.bukkit.entity.ItemFrame");
	}

	/**
	 * Determines if the server has the hanging events. If false, use the painting events
	 * @return true if the hanging events exist, false otherwise
	 */
	public static boolean hangingEvents(){
		return isClass("org.bukkit.event.hanging.HangingBreakEvent");
	}

	/**
	 * Determines if the server has the command block
	 * @return true if the server has the capability of supporting command blocks, false otherwise
	 */
	public static boolean commandBlock(){
		return isClass("org.bukkit.command.BlockCommandSender");
	}

	/**
	 * Determines if the server can support "vanilla" ender chests
	 * @return true if the server has the capability for vanilla ender chests
	 */
	public static boolean enderChests(){
		return isMethod("getEnderChest", Player.class);
	}

	/**
	 * Determines if the server has the "new" CraftBukkit scheduler
	 * @return true if the new scheduler should be used
	 */
	public static boolean runTaskMethod(){
		return isMethod("runTaskAsynchronously", BukkitScheduler.class);
	}

	/**
	 * Determines if the server has the 1.4.X entities
	 * @return true if the server has the capability to support the 1.4.X entities
	 */
	public static boolean mc14xEntities(){
		return isClass("org.bukkit.entity.Witch");
	}

	/**
	 * Determines if the server has "skulls"
	 * @return true if the server has the capability to support skulls
	 */
	public static boolean skulls(){
		return isClass("org.bukkit.block.Skull");
	}

}
