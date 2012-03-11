package com.turt2live.antishare.conversations.configuration;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.conversations.ASMenu;
import com.turt2live.antishare.conversations.WaitPrompt;

public class EventOptionEditor extends ASMenu {

	private String value;
	private String eventName;
	private String menuTitle;

	public EventOptionEditor(String value, String eventName, String menuTitle){
		super("add", "set");
		this.value = value;
		this.eventName = eventName;
		this.menuTitle = menuTitle;
	}

	@Override
	public void displayMenu(Conversable target){
		ASUtils.sendToConversable(target, ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + menuTitle + ChatColor.DARK_GREEN + " ]=======");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "add <value>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Adds a value to the list");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "set <value>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Sets the list equal to <value>");
		ASUtils.sendToConversable(target, ChatColor.AQUA + "Note: You must use spaces to idenity item IDs! For example: add 1 384 46");
		ASUtils.sendToConversable(target, ChatColor.YELLOW + "Don't want anyting blocked? Use " + ChatColor.GOLD + "set none");
		ASUtils.sendToConversable(target, ChatColor.YELLOW + "Want everything blocked? Use " + ChatColor.GOLD + "set *");
		ASUtils.sendToConversable(target, ChatColor.DARK_GREEN + "Current Value: " + ChatColor.GREEN + value);
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input){
		input = input.toLowerCase().trim();
		if(input.startsWith("set")){
			input = input.replaceFirst("set", "").trim();
			value = input;
		}else if(input.startsWith("add")){
			input = input.replaceFirst("add", "").trim();
			value = value + " " + input;
		}else if(input.startsWith("back")){
			return new EventEditor();
		}
		value = value.trim();
		((AntiShare) context.getPlugin()).getConfig().set("events." + eventName, value);
		((AntiShare) context.getPlugin()).getConfig().save();
		((AntiShare) context.getPlugin()).reloadConfig();
		ASUtils.sendToConversable(context.getForWhom(), ChatColor.GREEN + "Value Saved!");
		return new WaitPrompt(new EventEditor());
	}

	@Override
	public String getPromptText(ConversationContext context){
		displayMenu(context.getForWhom());
		return "Enter a value using the prefixes listed above:";
	}

	@Override
	protected String getFailedValidationText(ConversationContext context, String invalidInput){
		return "Please use add or set!";
	}

}
