package com.turt2live.antishare.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.conversations.configuration.EditConfigurationMenu;
import com.turt2live.antishare.conversations.permissions.PermissionsMenu;
import com.turt2live.antishare.conversations.region.EditRegionMenu;

public class MainMenu extends ASMenu {

	/*
	 * TODO: Create a work around
	 * =========================
	 * ADD REGION IS UNSUPPORTED
	 * =========================
	 * 
	 * This is because there is no way to get the CommandSender
	 * involved.
	 */

	public MainMenu(){
		super("edit configuration", "edit config", "edit region", "add region", "permissions help", "permissions", "perms", "perms help");
	}

	@Override
	public void displayMenu(Conversable target){
		ASUtils.sendToConversable(target, ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "Main Menu" + ChatColor.DARK_GREEN + " ]=======");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "edit configuration" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Edit the configuration");
		//ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "add region" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Create a GameMode Region");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "edit region" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Edit a GameMode Region");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "permissions help" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Get help with the permissions");
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input){
		input = input.trim();
		if(input.equalsIgnoreCase("edit configuration") || input.equalsIgnoreCase("edit config")){
			return new EditConfigurationMenu();
		}else if(input.equalsIgnoreCase("edit region")){
			return new EditRegionMenu(1, (AntiShare) context.getPlugin());
		}else if(input.equalsIgnoreCase("permissions help") || input.equalsIgnoreCase("perms help")
				|| input.equalsIgnoreCase("permissions") || input.equalsIgnoreCase("perms")){
			return new PermissionsMenu();
		}else if(input.equalsIgnoreCase("back")){
			ASUtils.sendToConversable(context.getForWhom(), ChatColor.RED + "You cannot go back from here! If you want to leave try: exit");
		}
		return new MainMenu();
	}
}
