package com.turt2live.antishare.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;

import com.turt2live.antishare.ASUtils;

public class ConversationSessionAbandonedListener implements ConversationAbandonedListener {

	@Override
	public void conversationAbandoned(ConversationAbandonedEvent event){
		if(event.getContext().getSessionData("terminate") == null && !event.gracefulExit()){
			ASUtils.sendToConversable(event.getContext().getForWhom(), ChatColor.RED + "Your session was closed because turt2live failed. Please tell him you got this error.");
		}
		if(event.gracefulExit()){ //Null prompt
			ASUtils.sendToConversable(event.getContext().getForWhom(), ChatColor.GREEN + "Your session was closed.");
		}
	}

}
