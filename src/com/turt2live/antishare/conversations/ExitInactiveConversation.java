package com.turt2live.antishare.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.InactivityConversationCanceller;

import com.turt2live.antishare.AntiShare;

public class ExitInactiveConversation extends InactivityConversationCanceller {

	public ExitInactiveConversation(AntiShare plugin, int timeout){
		super(plugin, timeout);
	}

	@Override
	protected void cancelling(Conversation conversation){
		super.cancelling(conversation);
		conversation.getForWhom().sendRawMessage(ConfigurationConversation.PREFIX + ChatColor.DARK_RED + "Conversation closed due to timeout. Data not saved.");
	}

}
