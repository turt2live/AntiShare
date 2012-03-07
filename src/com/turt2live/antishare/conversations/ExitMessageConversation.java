package com.turt2live.antishare.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.ConversationContext;

public class ExitMessageConversation implements ConversationCanceller {

	private Conversation conversation;

	// No Constructor

	@Override
	public boolean cancelBasedOnInput(ConversationContext context, String input){
		if(input.equalsIgnoreCase("/quit")){
			context.getForWhom().sendRawMessage(ConfigurationConversation.PREFIX + ChatColor.DARK_RED + "Session closed. Data not saved.");
			return true;
		}else if(input.equalsIgnoreCase("/save-all")){
			context.getForWhom().sendRawMessage(ConfigurationConversation.PREFIX + ChatColor.DARK_GREEN + "Session closed. Data saved.");
			// TODO: Save data
			return true;
		}
		return false;
	}

	@Override
	public void setConversation(Conversation conversation){
		this.conversation = conversation;
	}

	@Override
	public ConversationCanceller clone(){
		ExitMessageConversation ret = new ExitMessageConversation();
		ret.setConversation(conversation);
		return ret;
	}

}
