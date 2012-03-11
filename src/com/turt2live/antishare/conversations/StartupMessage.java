package com.turt2live.antishare.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

import com.turt2live.antishare.ASUtils;

public class StartupMessage extends MessagePrompt {

	@Override
	public String getPromptText(ConversationContext context){
		ASUtils.sendToConversable(context.getForWhom(), ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "AntiShare Configuration Helper" + ChatColor.DARK_GREEN + " ]=======");
		ASUtils.sendToConversable(context.getForWhom(), ChatColor.GREEN + "These commands will work at anytime!");
		ASUtils.sendToConversable(context.getForWhom(), ChatColor.DARK_AQUA + "save" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Saves everything and makes it effective");
		ASUtils.sendToConversable(context.getForWhom(), ChatColor.DARK_AQUA + "exit" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Exits your session, be sure to save first!");
		return ChatColor.DARK_AQUA + "main" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Sends you to the main menu " + ChatColor.DARK_PURPLE + "[TODO]";
		// Final line is a return to avoid formatting issues client-side
	}

	@Override
	protected Prompt getNextPrompt(ConversationContext context){
		return new MainMenu();
	}

}
