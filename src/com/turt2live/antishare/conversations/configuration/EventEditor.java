package com.turt2live.antishare.conversations.configuration;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.conversations.ASMenu;

public class EventEditor extends ASMenu {

	public EventEditor(){
		super("block place", "block break", "item drop", "interact", "death", "commands");
	}

	@Override
	public void displayMenu(Conversable target){
		ASUtils.sendToConversable(target, ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "Events" + ChatColor.DARK_GREEN + " ]=======");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "block place" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Edit the block place blacklist");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "block break" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Edit the block break blacklist");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "item drop" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Edit the items that cannot be thrown");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "interact" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Edit the clicked blocks blacklist");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "death" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Edit the spewed items on death blacklist");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "commands" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Edit the commands blcacklist");
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input){
		input = input.toLowerCase().trim();
		AntiShare plugin = (AntiShare) context.getPlugin();
		if(input.equalsIgnoreCase("block place")){
			return new EventOptionEditor(plugin.getConfig().getString("events.block_place"), "block_place", "Block Place Blacklist Editor");
		}else if(input.equalsIgnoreCase("block break")){
			return new EventOptionEditor(plugin.getConfig().getString("events.block_break"), "block_break", "Block Break Blacklist Editor");
		}else if(input.equalsIgnoreCase("item drop")){
			return new EventOptionEditor(plugin.getConfig().getString("events.drop_item"), "drop_item", "Blocked Item Drops Editor");
		}else if(input.equalsIgnoreCase("interact")){
			return new EventOptionEditor(plugin.getConfig().getString("events.interact"), "interact", "Blocked Interactions Editor");
		}else if(input.equalsIgnoreCase("death")){
			return new EventOptionEditor(plugin.getConfig().getString("events.death"), "death", "Blocked Death Item Editor");
		}else if(input.equalsIgnoreCase("commands")){
			return new EventOptionEditor(plugin.getConfig().getString("events.commands"), "commands", "Blocked Commands Editor");
		}else if(input.equalsIgnoreCase("back")){
			return new EditConfigurationMenu();
		}
		return new EventEditor();
	}

}
