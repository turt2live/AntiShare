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

	public static boolean tabComplete(){
		return isClass("org.bukkit.command.TabCompleter");
	}

	public static boolean adventureMode(){
		try{
			return GameMode.valueOf("ADVENTURE") != null;
		}catch(IllegalArgumentException e){}
		return false;
	}

	public static boolean mc14xItems(){
		return isClass("org.bukkit.entity.ItemFrame");
	}

	public static boolean hangingEvents(){
		return isClass("org.bukkit.event.hanging.HangingBreakEvent");
	}

	public static boolean commandBlock(){
		return isClass("org.bukkit.command.BlockCommandSender");
	}

	public static boolean enderChests(){
		return isMethod("getEnderChest", Player.class);
	}

	public static boolean runTaskMethod(){
		return isMethod("runTaskAsynchronously", BukkitScheduler.class);
	}

	public static boolean mc14xEntities(){
		return isClass("org.bukkit.entity.Witch");
	}

	public static boolean skulls(){
		return isClass("org.bukkit.block.Skull");
	}

}
