package com.turt2live.antishare.conversations.permissions;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.conversations.ASMenu;
import com.turt2live.antishare.conversations.MainMenu;
import com.turt2live.antishare.conversations.WaitPrompt;

public class PermissionsMenu extends ASMenu {

	// TODO: Uncomment tnt explosions when a better solution is ready

	public PermissionsMenu(){
		super("AntiShare.allow", "AntiShare.onlyIfCreative", "AntiShare.reload", "AntiShare.regions", "AntiShare.roam",
				"AntiShare.noswap", "AntiShare.freeplace", "AntiShare.notify", "AntiShare.silent", "AntiShare.admin",
				"AntiShare.mirror", "AntiShare.gamemode"/*, "AntiShare.tnt"*/);
	}

	@Override
	public void displayMenu(Conversable target){
		ASUtils.sendToConversable(target, ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "Permissions" + ChatColor.DARK_GREEN + " ]=======");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "<permission>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Get help with a permission node");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "list" + ChatColor.GOLD + " - " + ChatColor.AQUA + "List all permission nodes");
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input){
		input = input.trim();
		Conversable target = context.getForWhom();
		if(input.equalsIgnoreCase("back")){
			return new MainMenu();
		}else if(input.equalsIgnoreCase("list")){
			ASUtils.sendToConversable(target, ChatColor.GOLD + "AntiShare.allow  AntiShare.onlyIfCreative  AntiShare.reload");
			ASUtils.sendToConversable(target, ChatColor.GOLD + "AntiShare.regions  AntiShare.roam  AntiShare.noswap");
			ASUtils.sendToConversable(target, ChatColor.GOLD + "AntiShare.blockBypass  AntiShare.freeplace  AntiShare.mirror");
			ASUtils.sendToConversable(target, ChatColor.GOLD + "AntiShare.notify  AntiShare.silent  AntiShare.gamemode");
			ASUtils.sendToConversable(target, ChatColor.GOLD + /*AntiShare.tnt*/" AntiShare.admin");
			return new WaitPrompt(new PermissionsMenu());
		}else{
			ASUtils.sendToConversable(target, ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + input + ChatColor.DARK_GREEN + " ]=======");
			if(input.equalsIgnoreCase("AntiShare.allow")){
				ASUtils.sendToConversable(target, "There are many sub categories, but the main one is " + ChatColor.YELLOW + "AntiShare.allow.*" + ChatColor.WHITE + " meaning that you allow someone to have permission to do anything (they are not blocked). Set this to false to block every event to a player.");
				ASUtils.sendToConversable(target, ChatColor.GREEN + "See the config.yml for a full list of AnitShare.allow nodes.");
				ASUtils.sendToConversable(target, ChatColor.LIGHT_PURPLE + "These series of permissions are false by default, unless you are an OP.");
			}else if(input.equalsIgnoreCase("AntiShare.onlyIfCreative")){
				ASUtils.sendToConversable(target, "By setting this, you are telling AntiShare to only apply the blocked actions if the player is in creative mode. If you want them to be restricted regardless of Gamemode, set this to false.");
				ASUtils.sendToConversable(target, ChatColor.LIGHT_PURPLE + "This permission is true by default.");
			}else if(input.equalsIgnoreCase("AntiShare.reload")){
				ASUtils.sendToConversable(target, "Set this to true if you want the target to be able to use /antishare reload");
				ASUtils.sendToConversable(target, ChatColor.LIGHT_PURPLE + "This permission is false by default, unless you are an OP.");
			}else if(input.equalsIgnoreCase("AntiShare.regions")){
				ASUtils.sendToConversable(target, "Set this to true if you want the target to be able to create and edit regions.");
				ASUtils.sendToConversable(target, ChatColor.LIGHT_PURPLE + "This permission is false by default, unless you are an OP.");
			}else if(input.equalsIgnoreCase("AntiShare.roam")){
				ASUtils.sendToConversable(target, "Set this to true if you want the target to be able to roam through regions without having the effects of inventory cahnges or gamemode alterations.");
				ASUtils.sendToConversable(target, ChatColor.LIGHT_PURPLE + "This permission is false by default, unless you are an OP.");
			}else if(input.equalsIgnoreCase("AntiShare.noswap")){
				ASUtils.sendToConversable(target, "Set this to true if you want the target to be able to switch gamemodes without having their inventories changed.");
				ASUtils.sendToConversable(target, ChatColor.LIGHT_PURPLE + "This permission is false by default, unless you are an OP.");
			}else if(input.equalsIgnoreCase("AntiShare.blockBypass")){
				ASUtils.sendToConversable(target, "Set this to true if you want the target to be able to break any creative mode block, regardless of gamemode");
				ASUtils.sendToConversable(target, ChatColor.LIGHT_PURPLE + "This permission is false by default, unless you are an OP.");
			}else if(input.equalsIgnoreCase("AntiShare.freeplace")){
				ASUtils.sendToConversable(target, "Set this to true if you want the target to be able to place creative blocks without it ever showing up in the creative block database.");
				ASUtils.sendToConversable(target, ChatColor.LIGHT_PURPLE + "This permission is false by default, unless you are an OP.");
			}else if(input.equalsIgnoreCase("AntiShare.notify")){
				ASUtils.sendToConversable(target, "Set this to true if you want the target to be able to get AntiShare messages when specified events occur");
				ASUtils.sendToConversable(target, ChatColor.LIGHT_PURPLE + "This permission is false by default, unless you are an OP.");
			}else if(input.equalsIgnoreCase("AntiShare.silent")){
				ASUtils.sendToConversable(target, "Set this to true if you want the target to be able to be able to play without firing the AntiShare notifications themselves.");
				ASUtils.sendToConversable(target, ChatColor.LIGHT_PURPLE + "This permission is false by default, unless you are an OP.");
			}else if(input.equalsIgnoreCase("AntiShare.admin")){
				ASUtils.sendToConversable(target, "Set this to true if you want the target to be able to have full access to AntiShare and it's features, this also negates any blocking they may experience, such as interactions, block place/break, and throwing items.");
				ASUtils.sendToConversable(target, ChatColor.LIGHT_PURPLE + "This permission is false by default, unless you are an OP.");
			}else if(input.equalsIgnoreCase("AntiShare.mirror")){
				ASUtils.sendToConversable(target, "If this is true, the target can view people's inventories through a command.");
				ASUtils.sendToConversable(target, ChatColor.LIGHT_PURPLE + "This permission is false by default, unless you are an OP.");
			}else if(input.equalsIgnoreCase("AntiShare.gamemode")){
				ASUtils.sendToConversable(target, "If this is true, the target can change their own gamemode through /gm or another person's through the same command.");
				ASUtils.sendToConversable(target, ChatColor.LIGHT_PURPLE + "This permission is false by default, unless you are an OP.");
				//			}else if(input.equalsIgnoreCase("AntiShare.tnt")){
				//				ASUtils.sendToConversable(target, "If true, the target can place TNT that, when it explodes, drops items regardless of settings/permissions.");
				//				ASUtils.sendToConversable(target, ChatColor.LIGHT_PURPLE + "This permission is false by default, unless you are an OP.");
			}else{
				ASUtils.sendToConversable(target, ChatColor.RED + "That is not a permission node!");
			}
			return new WaitPrompt(new PermissionsMenu());
		}
	}

}
