package com.turt2live.antishare.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.InactivityConversationCanceller;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;

public class ExitInactiveConversation extends InactivityConversationCanceller {

	String time = "";

	public ExitInactiveConversation(AntiShare plugin, int timeout){
		super(plugin, timeout);
		int hours = (int) Math.floor(timeout / 3600);
		int minutes = (int) Math.floor((timeout - (hours * 3600)) / 60);
		int seconds = (timeout - ((minutes * 60) + (hours * 3600)));
		if(hours > 0){
			time = hours + " Hour" + (hours > 1 ? "s" : "");
		}
		if(minutes > 0){
			if(time.length() > 0){
				time = time + ", " + minutes + " Minute" + (minutes > 1 ? "s" : "");
			}else{
				time = minutes + " Minute" + (minutes > 1 ? "s" : "");
			}
		}
		if(seconds > 0){
			if(time.length() > 0){
				time = time + ", " + seconds + " Second" + (seconds > 1 ? "s" : "");
			}else{
				time = seconds + " Second" + (seconds > 1 ? "s" : "");
			}
		}
	}

	@Override
	protected void cancelling(Conversation conversation){
		ASUtils.sendToConversable(conversation.getForWhom(), ChatColor.RED + "You were inactive for " + time + ".");
		conversation.getContext().setSessionData("terminate", "timeout;" + time);
		super.cancelling(conversation);
	}

	@Override
	public ExitInactiveConversation clone(){
		return new ExitInactiveConversation((AntiShare) plugin, timeoutSeconds);
	}

}
