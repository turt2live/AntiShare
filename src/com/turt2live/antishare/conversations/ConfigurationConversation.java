package com.turt2live.antishare.conversations;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.conversations.prompts.MainMenu;

public class ConfigurationConversation extends ConversationFactory {

	public static String PREFIX = ChatColor.GRAY + "[AS Config] " + ChatColor.WHITE;

	public ConfigurationConversation(AntiShare plugin, Conversable target){
		super(plugin);
		withModality(true);
		withPrefix(new ConversationPrefix(){
			@Override
			public String getPrefix(ConversationContext context){
				return PREFIX;
			}
		});
		withFirstPrompt(new MainMenu());
		//withEscapeSequence("/quit");
		//withTimeout(10);
		thatExcludesNonPlayersWithMessage("Sorry! In-game only!");
		withConversationCanceller(new ExitInactiveConversation(plugin, 600));
		withConversationCanceller(new ExitMessageConversation());
		buildConversation(target).begin();
		for(ConversationCanceller c : this.cancellers){
			System.out.println(c.getClass().getName());
		}
	}

	public static boolean isValid(List<String> list, String input){
		for(String item : list){
			if(item.equalsIgnoreCase(input)){
				return true;
			}
		}
		return false;
	}

}
