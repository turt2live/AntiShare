package com.turt2live.antishare.conversations.prompts;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.conversations.ConfigurationConversation;

public class MainMenu extends FixedSetPrompt implements ASMenu {

	public MainMenu(){
		super("edit configuration", "edit config", "edit region", "add region", "permissions help", "perms", "perms help",
				"/edit configuration", "/edit config", "/edit region", "/add region", "/permissions help", "/perms", "/perms help");
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input){
		if(ConfigurationConversation.isValid(fixedSet, input)){
			return true;
		}
		return false;
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input){
		// TODO: Menus
		return null;
	}

	@Override
	protected String getFailedValidationText(ConversationContext context, String invalidInput){
		return "Please enter one of the following: ";
	}

	@Override
	public String getPromptText(ConversationContext context){
		displayMenu(context.getForWhom());
		return "Enter an option (" + ChatColor.DARK_AQUA + "dark aqua" + ChatColor.WHITE + " text) from above:";
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
