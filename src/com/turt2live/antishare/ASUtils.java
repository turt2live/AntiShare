package com.turt2live.antishare;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;

public class ASUtils {
	public static String addColor(String message){
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static void sendToPlayer(CommandSender target, String message){
		if(!message.equalsIgnoreCase("nomsg")
				&& !message.equalsIgnoreCase("no message")
				&& !message.equalsIgnoreCase("none")
				&& !message.equalsIgnoreCase("noshow")
				&& !message.equalsIgnoreCase("no show")){
			message = addColor(message);
			if(!ChatColor.stripColor(message).startsWith("[AntiShare]")){
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.WHITE + message;
			}
			target.sendMessage(message);
		}
	}

	public static void sendToConversable(Conversable target, String message){
		if(!message.equalsIgnoreCase("nomsg")
				&& !message.equalsIgnoreCase("no message")
				&& !message.equalsIgnoreCase("none")
				&& !message.equalsIgnoreCase("noshow")
				&& !message.equalsIgnoreCase("no show")){
			message = addColor(message);
			if(!ChatColor.stripColor(message).startsWith("[AntiShare]")){
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.WHITE + message;
			}
			target.sendRawMessage(message);
		}
	}

	public static Boolean getValueOf(String value){
		if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("t") || value.equalsIgnoreCase("on")
				|| value.equalsIgnoreCase("active") || value.equalsIgnoreCase("1")){
			return true;
		}else if(value.equalsIgnoreCase("false") || value.equalsIgnoreCase("f") || value.equalsIgnoreCase("off")
				|| value.equalsIgnoreCase("inactive") || value.equalsIgnoreCase("0")){
			return false;
		}
		return null;
	}

	public static GameMode getGameMode(String value){
		if(value.equalsIgnoreCase("creative") || value.equalsIgnoreCase("c") || value.equalsIgnoreCase("1")){
			return GameMode.CREATIVE;
		}else if(value.equalsIgnoreCase("survival") || value.equalsIgnoreCase("s") || value.equalsIgnoreCase("0")){
			return GameMode.SURVIVAL;
		}
		return null;
	}

	public static boolean isInteractable(Material material){
		switch (material){
		case DISPENSER:
			return true;
		case NOTE_BLOCK:
			return true;
		case BED_BLOCK:
			return true;
		case CHEST:
			return true;
		case WORKBENCH:
			return true;
		case FURNACE:
			return true;
		case BURNING_FURNACE:
			return true;
		case WOODEN_DOOR:
			return true;
		case LEVER:
			return true;
		case STONE_PLATE:
			return true;
		case IRON_DOOR_BLOCK:
			return true;
		case WOOD_PLATE:
			return true;
		case STONE_BUTTON:
			return true;
		case JUKEBOX:
			return true;
		case LOCKED_CHEST:
			return true;
		case TRAP_DOOR:
			return true;
		case MONSTER_EGGS:
			return true;
		case FENCE_GATE:
			return true;
		case ENCHANTMENT_TABLE:
			return true;
		case BREWING_STAND:
			return true;
		case CAULDRON:
			return true;
		default:
			return false;
		}
	}
}
