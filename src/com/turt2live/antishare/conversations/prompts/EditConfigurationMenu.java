package com.turt2live.antishare.conversations.prompts;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import com.turt2live.antishare.ASUtils;

public class EditConfigurationMenu extends ASMenu {

	public EditConfigurationMenu(){
		super("events", "messages", "notifications", "sql", "other",
				"/events", "/messages", "/notifications", "/sql", "/other");
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void displayMenu(Conversable target){
		ASUtils.sendToConversable(target, ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "Edit Configuration" + ChatColor.DARK_GREEN + " ]=======");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "events" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Edit the blocked lists");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "messages" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Edit the messages sent to users");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "notifications" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Edit which notifications are sent");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "sql" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Edit the SQL options");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "other" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Edit other options");
	}

}
