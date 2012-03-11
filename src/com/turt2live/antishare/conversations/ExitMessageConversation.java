package com.turt2live.antishare.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.ConversationContext;

public class ExitMessageConversation implements ConversationCanceller {

	private Conversation conversation;

	@Override
	public boolean cancelBasedOnInput(ConversationContext context, String input){
		if(input.toLowerCase().startsWith("/quit") || input.toLowerCase().startsWith("quit")){
			context.getForWhom().sendRawMessage(ConfigurationConversation.PREFIX + ChatColor.DARK_RED + "Session closed. Data not saved.");
			conversation.getContext().setSessionData("terminate", "message");
			return true;
		}else if(input.toLowerCase().startsWith("/save") || input.toLowerCase().startsWith("save")){
			context.getForWhom().sendRawMessage(ConfigurationConversation.PREFIX + ChatColor.DARK_GREEN + "Data saved.");
			// TODO: Save data
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
