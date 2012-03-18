package com.turt2live.antishare.conversations.region;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.conversations.ASMenu;
import com.turt2live.antishare.conversations.ConfigurationConversation;
import com.turt2live.antishare.conversations.WaitPrompt;
import com.turt2live.antishare.regions.ASRegion;

public class RegionEditor extends ASMenu {

	private ASRegion region;

	// inventory and area are unsupported due to a chat lock

	public RegionEditor(ASRegion region){
		super("name", "show enter message", "show exit message", "enter message", "exit message"/*, "inventory"*/, "gamemode"/*, "area"*/, "current");
		this.region = region;
	}

	@Override
	public void displayMenu(Conversable target){
		ASUtils.sendToConversable(target, ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "Edit Region: " + region.getName() + ChatColor.DARK_GREEN + " ]=======");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "name <name>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Sets the name of the region");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "show enter message <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Show enter messages?");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "show exit message <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Show exit messages?");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "enter message <message>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Set the on enter message");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "exit message <message>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Set the on leave message");
		//ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "inventory <set/none>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Sets the inventory for the region");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "gamemode <survival/creative>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Set the gamemode");
		//ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "area" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Sets the area of the region");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "current" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Shows current settings");
		ASUtils.sendToConversable(target, ChatColor.GOLD + "Note: " + ChatColor.YELLOW + "<t/f> means you have to enter true or false!");
		//ASUtils.sendToConversable(target, ChatColor.GOLD + "Note: " + ChatColor.YELLOW + "'inventory set' means to take your current inventory and apply it, 'none' means to not switch to a temporary inventory");
		ASUtils.sendToConversable(target, ChatColor.GOLD + "Note: " + ChatColor.YELLOW + "In the messages you can use {name} to insert the region name");
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input){
		String original = input.trim();
		input = input.toLowerCase().trim();
		AntiShare plugin = (AntiShare) context.getPlugin();
		Conversable target = context.getForWhom();
		if(input.equalsIgnoreCase("back")){
			return new EditRegionMenu(1, plugin);
		}else if(input.startsWith("name")){
			String name = original.substring(5).trim();
			region.setName(name);
		}else if(input.startsWith("show enter message")){
			String value = input.substring(18).trim();
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new RegionEditor(region);
			}else{
				region.setMessageOptions(ASUtils.getValueOf(value), region.isExitMessageActive());
			}
		}else if(input.startsWith("show exit message")){
			String value = input.substring(17).trim();
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new RegionEditor(region);
			}else{
				region.setMessageOptions(region.isEnterMessageActive(), ASUtils.getValueOf(value));
			}
		}else if(input.startsWith("enter message")){
			String message = input.substring(13).trim();
			region.setEnterMessage(message);
		}else if(input.startsWith("exit message")){
			String message = input.substring(12).trim();
			region.setExitMessage(message);
		}else if(input.startsWith("gamemode")){
			String gamemodeName = input.substring(8).trim();
			GameMode gamemode;
			if(gamemodeName.equalsIgnoreCase("creative") || gamemodeName.equalsIgnoreCase("c") || gamemodeName.equalsIgnoreCase("1")){
				gamemode = GameMode.CREATIVE;
			}else if(gamemodeName.equalsIgnoreCase("survival") || gamemodeName.equalsIgnoreCase("s") || gamemodeName.equalsIgnoreCase("0")){
				gamemode = GameMode.SURVIVAL;
			}else{
				ConfigurationConversation.showError(target, ChatColor.RED + "I don't know what Game Mode '" + gamemodeName + "' is!");
				return new RegionEditor(region);
			}
			region.setGameMode(gamemode);
		}else if(input.startsWith("current")){
			ASUtils.sendToConversable(target, ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "Region Information" + ChatColor.DARK_GREEN + " ]=======");
			ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "Name: " + ChatColor.AQUA + region.getName());
			ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "Show Enter Message: " + ChatColor.AQUA + region.isEnterMessageActive());
			ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "Show Exit Message: " + ChatColor.AQUA + region.isExitMessageActive());
			ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "Enter Message: " + ChatColor.AQUA + region.getEnterMessage());
			ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "Exit Message: " + ChatColor.AQUA + region.getExitMessage());
			ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "Gamemode: " + ChatColor.AQUA + region.getGameModeSwitch().name().toLowerCase());
			return new WaitPrompt(new RegionEditor(region));
		}
		ASUtils.sendToConversable(context.getForWhom(), ChatColor.GREEN + "Value Saved!");
		return new RegionEditor(region);
	}
}
