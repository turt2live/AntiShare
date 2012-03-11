package com.turt2live.antishare.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;

import com.turt2live.antishare.ASUtils;

public class ConversationSessionAbandonedListener implements ConversationAbandonedListener {

	@Override
	public void conversationAbandoned(ConversationAbandonedEvent event){
		ASUtils.sendToConversable(event.getContext().getForWhom(), ChatColor.GREEN + "Your session was closed.");
	}

}
