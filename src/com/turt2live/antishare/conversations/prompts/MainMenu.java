package com.turt2live.antishare.conversations.prompts;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import com.turt2live.antishare.ASUtils;

public class MainMenu extends ASMenu {

	public MainMenu(){
		super("edit configuration", "edit config", "edit region", "add region", "permissions help", "perms", "perms help",
				"/edit configuration", "/edit config", "/edit region", "/add region", "/permissions help", "/perms", "/perms help");
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input){
		// TODO: Menus
		return null;
	}

	@Override
	public void displayMenu(Conversable target){
		ASUtils.sendToConversable(target, ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "Main Menu" + ChatColor.DARK_GREEN + " ]=======");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "edit configuration" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Edit the configuration");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "edit region" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Edit a GameMode Region");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "add region" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Create a GameMode Region");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "permissions help" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Get help with the permissions");
	}
}
