package com.turt2live.antishare.conversations;

import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.ConversationContext;

public class ExitMessageConversation implements ConversationCanceller {

	private Conversation conversation;

	@Override
	public boolean cancelBasedOnInput(ConversationContext context, String input){
		input = input.replaceFirst("/", "");
		if(input.toLowerCase().startsWith("quit") || input.toLowerCase().startsWith("exit")){
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
