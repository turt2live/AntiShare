package com.turt2live.antishare.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;

import com.turt2live.antishare.ASUtils;

public class ConversationSessionAbandonedListener implements ConversationAbandonedListener {

	@Override
	public void conversationAbandoned(ConversationAbandonedEvent event){
		if(event.getContext().getSessionData("terminate") == null){
			ASUtils.sendToConversable(event.getContext().getForWhom(), ChatColor.DARK_RED + "Your session was closed for an unknown reason.");
		}
	}

}
