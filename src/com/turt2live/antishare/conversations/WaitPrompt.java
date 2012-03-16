package com.turt2live.antishare.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class WaitPrompt extends StringPrompt {

	private Prompt next;

	public WaitPrompt(Prompt next){
		this.next = next;
	}

	@Override
	public String getPromptText(ConversationContext context){
		return ChatColor.YELLOW + "Enter anything to continue...";
	}

	@Override
	public Prompt acceptInput(ConversationContext context, String input){
		return next;
	}
}
