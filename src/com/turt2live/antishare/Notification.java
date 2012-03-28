package com.turt2live.antishare;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.turt2live.antishare.enums.BlockedType;
import com.turt2live.antishare.enums.NotificationType;
import com.turt2live.antishare.event.AntiShareEvent;

public class Notification {

	public static void sendNotification(NotificationType type, AntiShare plugin, Player player, String variable, Material material){
		boolean send = true;
		if(!plugin.getConfig().getBoolean("notifications.send")){
			send = false;
		}
		if(plugin.getPermissions().has(player, "AntiShare.silent", player.getWorld())){
			send = false;
		}
		if(!plugin.getConfig().getBoolean(type.getConfigValue())){
			send = false;
		}
		variable = variable.replaceAll("_", " ");
		plugin.getServer().getPluginManager().callEvent(new AntiShareEvent(type, variable, player));
		String message = "";
		if(send){
			switch (type){
			// ILLEGAL actions
			case ILLEGAL_BLOCK_PLACE:
				if(variable.equalsIgnoreCase("BEDROCK")){
					break;
				}
				if(plugin.storage.isBlocked(material, BlockedType.INTERACT, player.getWorld())){
					break;
				}
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to place " + ChatColor.DARK_RED + variable + ChatColor.AQUA + "!";
				break;
			case ILLEGAL_BLOCK_BREAK:
				if(variable.equalsIgnoreCase("BEDROCK")){
					break;
				}
				if(plugin.storage.isBlocked(material, BlockedType.INTERACT, player.getWorld())){
					break;
				}
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to break " + ChatColor.DARK_RED + variable + ChatColor.AQUA + "!";
				break;
			case ILLEGAL_CREATIVE_BLOCK_BREAK:
				if(variable.equalsIgnoreCase("BEDROCK")){
					break;
				}
				if(plugin.storage.isBlocked(material, BlockedType.INTERACT, player.getWorld())){
					break;
				}
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to break the creative block " + ChatColor.DARK_RED + variable + ChatColor.AQUA + "!";
				break;
			case ILLEGAL_SURVIVAL_BLOCK_BREAK:
				if(variable.equalsIgnoreCase("BEDROCK")){
					break;
				}
				if(plugin.storage.isBlocked(material, BlockedType.INTERACT, player.getWorld())){
					break;
				}
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to break the survival block " + ChatColor.DARK_RED + variable + ChatColor.AQUA + "!";
				break;
			case ILLEGAL_WORLD_CHANGE:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to go to world " + ChatColor.DARK_RED + variable + ChatColor.AQUA + "!";
				break;
			case ILLEGAL_COMMAND:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to send the command " + ChatColor.DARK_RED + variable + ChatColor.AQUA + "!";
				break;
			case ILLEGAL_DEATH:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to die in " + ChatColor.DARK_RED + variable + ChatColor.AQUA + " mode!";
				break;
			case ILLEGAL_DROP_ITEM:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to drop the item " + ChatColor.DARK_RED + variable + ChatColor.AQUA + "!";
				break;
			case ILLEGAL_INTERACTION:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to interact with " + ChatColor.DARK_RED + variable + ChatColor.AQUA + "!";
				break;
			case ILLEGAL_PLAYER_PVP:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to hit " + ChatColor.DARK_RED + variable + ChatColor.AQUA + "!";
				break;
			case ILLEGAL_MOB_PVP:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to hit a " + ChatColor.DARK_RED + variable + ChatColor.AQUA + "!";
				break;
			case ILLEGAL_EGG:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to use a " + ChatColor.DARK_RED + variable + ChatColor.AQUA + "!";
				break;
			case ILLEGAL_EXP_BOTTLE:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to use an " + ChatColor.DARK_RED + variable + ChatColor.AQUA + "!";
				break;
			case ILLEGAL_BEDROCK:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to use " + ChatColor.DARK_RED + variable + ChatColor.AQUA + "!";
				break;
			case ILLEGAL_ITEM_THROW_INTO_REGION:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " threw an item into the region " + ChatColor.DARK_RED + variable + ChatColor.AQUA + "!";
				break;
			case ILLEGAL_FIRE_CHARGE:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to use a " + ChatColor.DARK_RED + variable + ChatColor.AQUA + "!";
				break;
			case ILLEGAL_FIRE:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " used " + ChatColor.DARK_RED + variable + ChatColor.AQUA + "!";
				break;
			case ILLEGAL_BUCKET:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " used a " + ChatColor.DARK_RED + variable + ChatColor.AQUA + "!";
				break;
			case ILLEGAL_TNT_PLACE:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " placed a " + ChatColor.DARK_RED + variable + ChatColor.AQUA + "!";
				break;
			case ILLEGAL_REGION_ITEM:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to throw " + ChatColor.DARK_RED + variable + ChatColor.AQUA + " into a region!";
				break;

			// LEGAL actions
			case LEGAL_BLOCK_PLACE:
				if(variable.equalsIgnoreCase("BEDROCK")){
					break;
				}
				if(plugin.storage.isBlocked(material, BlockedType.INTERACT, player.getWorld())){
					break;
				}
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to place " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + "!";
				break;
			case LEGAL_BLOCK_BREAK:
				if(variable.equalsIgnoreCase("BEDROCK")){
					break;
				}
				if(plugin.storage.isBlocked(material, BlockedType.INTERACT, player.getWorld())){
					break;
				}
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to break " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + "!";
				break;
			case LEGAL_CREATIVE_BLOCK_BREAK:
				if(variable.equalsIgnoreCase("BEDROCK")){
					break;
				}
				if(plugin.storage.isBlocked(material, BlockedType.INTERACT, player.getWorld())){
					break;
				}
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to break the creative block " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + "!";
				break;
			case LEGAL_SURVIVAL_BLOCK_BREAK:
				if(variable.equalsIgnoreCase("BEDROCK")){
					break;
				}
				if(plugin.storage.isBlocked(material, BlockedType.INTERACT, player.getWorld())){
					break;
				}
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to break the survival block " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + "!";
				break;
			case LEGAL_WORLD_CHANGE:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to go to world " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + "!";
				break;
			case LEGAL_COMMAND:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to send the command " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + "!";
				break;
			case LEGAL_DEATH:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to die in " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + " mode!";
				break;
			case LEGAL_DROP_ITEM:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to drop the item " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + "!";
				break;
			case LEGAL_INTERACTION:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to interact with " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + "!";
				break;
			case LEGAL_PLAYER_PVP:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to hit " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + "!";
				break;
			case LEGAL_MOB_PVP:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to hit a " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + "!";
				break;
			case LEGAL_EGG:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to use a " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + "!";
				break;
			case LEGAL_EXP_BOTTLE:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to use an " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + "!";
				break;
			case LEGAL_BEDROCK:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to use " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + "!";
				break;
			case LEGAL_ITEM_THROW_INTO_REGION:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " threw an item into the region " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + "!";
				break;
			case LEGAL_FIRE_CHARGE:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to use a " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + "!";
				break;
			case LEGAL_FIRE:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " used " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + "!";
				break;
			case LEGAL_BUCKET:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " used a " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + "!";
				break;
			case LEGAL_TNT_PLACE:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " placed a " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + "!";
				break;
			case LEGAL_REGION_ITEM:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " tried to throw " + ChatColor.DARK_GREEN + variable + ChatColor.AQUA + " into a region!";
				break;

			// GENERAL actions
			case GAMEMODE_CHANGE:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " changed to gamemode " + ChatColor.BLUE + variable + ChatColor.AQUA + "!";
				break;
			case REGION_ENTER:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " entered the region " + ChatColor.BLUE + variable + ChatColor.AQUA + "!";
				break;
			case REGION_EXIT:
				message = ChatColor.GRAY + "[AntiShare] " + ChatColor.AQUA + player.getName() + " left the region " + ChatColor.BLUE + variable + ChatColor.AQUA + "!";
				break;
			}
			String prefix = type.name().startsWith("LEGAL") ? "[LEGAL]" : (type.name().startsWith("ILLEGAL") ? "[ILLEGAL]" : "");
			plugin.log.logEvent(prefix + message.replace("[AntiShare]", ""));
			plugin.log.log(prefix + message.replace("[AntiShare]", ""));
		}
		String prefix = type.name().startsWith("LEGAL") ? "[LEGAL]" : (type.name().startsWith("ILLEGAL") ? "[ILLEGAL]" : "");
		plugin.log.logForce(prefix + message.replace("[AntiShare]", ""));
		if(message.length() > 0){
			for(Player online : Bukkit.getOnlinePlayers()){
				if(online.hasPermission("AntiShare.notify")){
					online.sendMessage(message);
				}
			}
			if(plugin.getConfig().getBoolean("notifications.console")){
				prefix = type.name().startsWith("LEGAL") ? "[LEGAL] " : (type.name().startsWith("ILLEGAL") ? "[ILLEGAL] " : "");
				Bukkit.getConsoleSender().sendMessage(prefix + message);
			}
		}
	}

	public static void sendNotification(NotificationType type, Player player, String variable, Material material){
		sendNotification(type, (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare"), player, variable, material);
	}

	public static void sendNotification(NotificationType type, Player player, String variable){
		sendNotification(type, (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare"), player, variable, null);
	}

}
