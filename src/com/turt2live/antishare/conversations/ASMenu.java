package com.turt2live.antishare.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;

import com.turt2live.antishare.ASUtils;

public abstract class ASMenu extends FixedSetPrompt {

	public ASMenu(String... set){
		super(set);
	}

	public abstract void displayMenu(Conversable target);

	@Override
	public String getPromptText(ConversationContext context){
		displayMenu(context.getForWhom());
		return "Enter an option (" + ChatColor.DARK_AQUA + "dark aqua" + ChatColor.WHITE + " text) from above:";
	}

	@Override
	protected String getFailedValidationText(ConversationContext context, String invalidInput){
		ASUtils.sendToConversable(context.getForWhom(), ChatColor.DARK_RED + "=======[ " + ChatColor.RED + "Invalid Option" + ChatColor.DARK_RED + " ]=======");
		return "Please enter one of the following: ";
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input){
		input = input.replaceFirst("/", "");
		if(ConfigurationConversation.isValid(fixedSet, input, context)){
			return true;
		}
		return false;
	}
}
