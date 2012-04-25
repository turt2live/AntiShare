package com.turt2live.antishare;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.material.Attachable;

/**
 * Utilities
 * 
 * @author turt2live
 */
public class ASUtils {

	/**
	 * Adds color to a message
	 * 
	 * @param message the message
	 * @return the colored message
	 */
	public static String addColor(String message){
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	/**
	 * Sends a message to a player.<br>
	 * This will prefix "[AntiShare]" to the message and not send if the message is simply "no message".
	 * 
	 * @param target the player to send to
	 * @param message the message to send
	 */
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

	/**
	 * Gets a boolean from a String
	 * 
	 * @param value the String
	 * @return the boolean (or null if not found)
	 */
	public static Boolean getBoolean(String value){
		if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("t") || value.equalsIgnoreCase("on")
				|| value.equalsIgnoreCase("active") || value.equalsIgnoreCase("1")){
			return true;
		}else if(value.equalsIgnoreCase("false") || value.equalsIgnoreCase("f") || value.equalsIgnoreCase("off")
				|| value.equalsIgnoreCase("inactive") || value.equalsIgnoreCase("0")){
			return false;
		}
		return null;
	}

	/**
	 * Gets a GameMode from a String
	 * 
	 * @param value the string
	 * @return the GameMode (or null if not found)
	 */
	public static GameMode getGameMode(String value){
		if(value.equalsIgnoreCase("creative") || value.equalsIgnoreCase("c") || value.equalsIgnoreCase("1")){
			return GameMode.CREATIVE;
		}else if(value.equalsIgnoreCase("survival") || value.equalsIgnoreCase("s") || value.equalsIgnoreCase("0")){
			return GameMode.SURVIVAL;
		}
		return null;
	}

	/**
	 * Determines if a Material is interactable (to AntiShare's standards)
	 * 
	 * @param material the material
	 * @return true if interactable
	 */
	public static boolean isInteractable(Material material){
		switch (material){
		case DISPENSER:
		case NOTE_BLOCK:
		case BED_BLOCK:
		case CHEST:
		case WORKBENCH:
		case FURNACE:
		case BURNING_FURNACE:
		case WOODEN_DOOR:
		case LEVER:
		case STONE_PLATE:
		case IRON_DOOR_BLOCK:
		case WOOD_PLATE:
		case STONE_BUTTON:
		case JUKEBOX:
		case LOCKED_CHEST:
		case TRAP_DOOR:
		case MONSTER_EGGS:
		case FENCE_GATE:
		case ENCHANTMENT_TABLE:
		case BREWING_STAND:
		case CAULDRON:
			return true;
		}
		return false;
	}

	/**
	 * Determines if a block would be dropped if an attached block were to break.<br>
	 * This also checks if the block is attached to a source.
	 * 
	 * @param block the block (attached to the breaking block)
	 * @param source the block that the checked block may be attached to (null for no source)
	 * @return true if the block would fall
	 */
	public static boolean isDroppedOnBreak(Block block, Block source){
		boolean attached = false;
		if(block.getState().getData() instanceof Attachable && !block.getType().equals(Material.PISTON_EXTENSION)){
			if(source != null){
				Attachable att = (Attachable) block.getState().getData();
				// We need to use location because Java is mean like that >.<
				Location l1 = source.getLocation();
				Location l2 = block.getRelative(att.getAttachedFace()).getLocation();
				attached = l1.getBlockX() == l2.getBlockX() && l1.getBlockY() == l2.getBlockY() && l1.getBlockZ() == l2.getBlockZ();
			}else{
				attached = true;
			}
		}
		return attached;
	}

	/**
	 * Capitalizes only the first letter of a string
	 * 
	 * @param string the string
	 * @return the string, capitalized correctly
	 */
	public static String capitalize(String string){
		String parts[] = string.toLowerCase().replaceAll(" ", "_").split("_");
		StringBuilder returnString = new StringBuilder();
		for(String part : parts){
			returnString.append(part.substring(0, 1).toUpperCase() + part.substring(1) + " ");
		}
		return returnString.toString().trim();
	}

}
