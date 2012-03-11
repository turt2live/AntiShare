package com.turt2live.antishare.conversations;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;

public class ConfigurationConversation extends ConversationFactory {

	public static String PREFIX = ChatColor.GRAY + "[AntiShare] " + ChatColor.WHITE;

	public ConfigurationConversation(AntiShare plugin, Conversable target){
		super(plugin);
		withModality(true);
		withPrefix(new ConversationPrefix(){
			@Override
			public String getPrefix(ConversationContext context){
				return PREFIX;
			}
		});
		withFirstPrompt(new StartupMessage());
		withLocalEcho(false);
		thatExcludesNonPlayersWithMessage("Sorry! In-game only!");
		withConversationCanceller(new ExitInactiveConversation(plugin, 600));
		withConversationCanceller(new ExitMessageConversation());
		addConversationAbandonedListener(new ConversationSessionAbandonedListener());
		buildConversation(target).begin();
	}

	public static boolean isValid(List<String> list, String input, ConversationContext context){
		String originalInput = input;
		input = input.trim().toLowerCase();
		if(input.startsWith("back")){
			return true;
		}
		for(String item : list){
			item = item.toLowerCase().trim();
			if(input.startsWith(item)){
				String tempInput = originalInput.substring(0, item.length()).toLowerCase() + originalInput.substring(item.length());
				context.setSessionData("msg_no_node", tempInput.replaceFirst(item, "").trim());
				context.setSessionData("notification_no_node", tempInput.replaceFirst(item, "").trim());
				return true;
			}
		}
		return false;
	}

	public static void showError(Conversable target, String message){
		ASUtils.sendToConversable(target, ChatColor.DARK_RED + "=======[ " + ChatColor.RED + "Invalid Option" + ChatColor.DARK_RED + " ]=======");
		ASUtils.sendToConversable(target, message);
	}

}
