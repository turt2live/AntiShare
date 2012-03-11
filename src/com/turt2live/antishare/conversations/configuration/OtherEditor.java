package com.turt2live.antishare.conversations.configuration;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.conversations.ASMenu;
import com.turt2live.antishare.conversations.ConfigurationConversation;
import com.turt2live.antishare.conversations.WaitPrompt;

public class OtherEditor extends ASMenu {

	public OtherEditor(){
		super("show", "allow eggs", "allow exp bottle", "allow bedrock", "inventories", "track blocks", "pvp", "mob pvp", "no drops",
				"worlds", "throw to region", "tracked blocks");
	}

	@Override
	public void displayMenu(Conversable target){
		ASUtils.sendToConversable(target, ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "Messages" + ChatColor.DARK_GREEN + " ]=======");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "allow eggs <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Allow mob eggs?");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "allow exp bottle <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Allow exp bottles?");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "allow bedrock <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Allow bedrock?");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "inventories <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Gamemode inventories, on?");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "track blocks <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Track 'creative mode' blocks?");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "pvp <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "True to allow pvp");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "mob pvp <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "True to allow mob pvp");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "no drops <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "True allows breaking of creative blocks, no drops though");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "worlds <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "True to allow transfer of worlds");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "throw to region <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Allow throwing of items into regions?");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "tracked blocks <items>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Set which creative blocks to track");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "show <node>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Show the value of an above <node>");
		ASUtils.sendToConversable(target, ChatColor.GOLD + "Note: " + ChatColor.YELLOW + "For 'tracked blocks', '*' means all, and 'none' means none. Remember to use ID's!");
		ASUtils.sendToConversable(target, ChatColor.GOLD + "Note: " + ChatColor.YELLOW + "<t/f> means you have to enter true or false!");
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input){
		input = input.replaceFirst("/", "").toLowerCase().trim();
		AntiShare plugin = (AntiShare) context.getPlugin();
		Conversable target = context.getForWhom();
		if(input.startsWith("show")){
			input = input.replace("show", "").trim();
			displayValue(input, target, plugin);
			return new WaitPrompt(new OtherEditor());
		}else if(input.equals("back")){
			return new EditConfigurationMenu();
		}else if(input.startsWith("allow eggs")){
			String value = input.replace("allow eggs", "").trim();
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new NotificationEditor();
			}else{
				plugin.getConfig().set("other.allow_eggs", ASUtils.getValueOf(value));
			}
		}else if(input.startsWith("allow exp bottle")){
			String value = input.replace("allow exp bottle", "").trim();
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new NotificationEditor();
			}else{
				plugin.getConfig().set("other.allow_exp_bottle", ASUtils.getValueOf(value));
			}
		}else if(input.startsWith("allow bedrock")){
			String value = input.replace("allow bedrock", "").trim();
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new NotificationEditor();
			}else{
				plugin.getConfig().set("other.allow_bedrock", ASUtils.getValueOf(value));
			}
		}else if(input.startsWith("inventories")){
			String value = input.replace("inventories", "").trim();
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new NotificationEditor();
			}else{
				plugin.getConfig().set("other.inventory_swap", ASUtils.getValueOf(value));
			}
		}else if(input.startsWith("track blocks")){
			String value = input.replace("track blocks", "").trim();
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new NotificationEditor();
			}else{
				plugin.getConfig().set("other.track_blocks", ASUtils.getValueOf(value));
			}
		}else if(input.startsWith("pvp")){
			String value = input.replace("pvp", "").trim();
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new NotificationEditor();
			}else{
				plugin.getConfig().set("other.pvp", ASUtils.getValueOf(value));
			}
		}else if(input.startsWith("mob pvp")){
			String value = input.replace("mob pvp", "").trim();
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new NotificationEditor();
			}else{
				plugin.getConfig().set("other.pvp-mobs", ASUtils.getValueOf(value));
			}
		}else if(input.startsWith("no drops")){
			String value = input.replace("no drops", "").trim();
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new NotificationEditor();
			}else{
				plugin.getConfig().set("other.blockDrops", ASUtils.getValueOf(value));
			}
		}else if(input.startsWith("worlds")){
			String value = input.replace("worlds", "").trim();
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new NotificationEditor();
			}else{
				plugin.getConfig().set("other.worldTransfer", ASUtils.getValueOf(value));
			}
		}else if(input.startsWith("throw to region")){
			String value = input.replace("throw to region", "").trim();
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new NotificationEditor();
			}else{
				plugin.getConfig().set("other.cannot_throw_to_regions", !ASUtils.getValueOf(value)); //Inverse
			}
		}else if(input.startsWith("tracked blocks")){
			String value = input.replace("tracked blocks", "").trim();
			plugin.getConfig().set("other.tracked-blocks", value);
		}
		plugin.getConfig().save();
		plugin.reloadConfig();
		ASUtils.sendToConversable(context.getForWhom(), ChatColor.GREEN + "Value Saved!");
		return new WaitPrompt(new OtherEditor());
	}

	private void displayValue(String input, Conversable target, AntiShare plugin){
		String value = "UNKNOWN";
		if(input.startsWith("allow eggs")){
			value = String.valueOf(plugin.getConfig().getBoolean("other.allow_eggs"));
		}else if(input.startsWith("allow exp bottle")){
			value = String.valueOf(plugin.getConfig().getBoolean("other.allow_exp_bottle"));
		}else if(input.startsWith("allow bedrock")){
			value = String.valueOf(plugin.getConfig().getBoolean("other.allow_bedrock"));
		}else if(input.startsWith("inventories")){
			value = String.valueOf(plugin.getConfig().getBoolean("other.inventory_swap"));
		}else if(input.startsWith("track blocks")){
			value = String.valueOf(plugin.getConfig().getBoolean("other.track_blocks"));
		}else if(input.startsWith("pvp")){
			value = String.valueOf(plugin.getConfig().getBoolean("other.pvp"));
		}else if(input.startsWith("mob pvp")){
			value = String.valueOf(plugin.getConfig().getBoolean("other.pvp-mobs"));
		}else if(input.startsWith("no drops")){
			value = String.valueOf(plugin.getConfig().getBoolean("other.blockDrops"));
		}else if(input.startsWith("worlds")){
			value = String.valueOf(plugin.getConfig().getBoolean("other.worldTransfer"));
		}else if(input.startsWith("throw to region")){
			value = String.valueOf(!plugin.getConfig().getBoolean("other.cannot_throw_to_regions")); // Inverse
		}else if(input.startsWith("tracked blocks")){
			value = plugin.getConfig().getString("other.tracked-blocks");
		}
		ASUtils.sendToConversable(target, ChatColor.GOLD + input + ChatColor.YELLOW + " is currently set to " + ChatColor.GOLD + value);
	}

}
