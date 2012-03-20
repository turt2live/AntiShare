package com.turt2live.antishare.conversations.hazards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.conversations.ASMenu;
import com.turt2live.antishare.conversations.ConfigurationConversation;
import com.turt2live.antishare.conversations.WaitPrompt;
import com.turt2live.antishare.conversations.configuration.EditConfigurationMenu;
import com.turt2live.antishare.conversations.configuration.OtherEditor;

public class HazardMenu extends ASMenu {

	public HazardMenu(){
		super("allow eggs", "allow exp bottle", "allow bedrock", "allow tnt", "allow fire charge", "allow fire", "allow buckets");
	}

	@Override
	public void displayMenu(Conversable target){
		ASUtils.sendToConversable(target, ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "Messages" + ChatColor.DARK_GREEN + " ]=======");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "allow eggs <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Allow mob eggs?");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "allow exp bottle <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Allow exp bottles?");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "allow bedrock <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Allow bedrock?");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "allow tnt <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Allow tnt?");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "allow fire charge <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Allow fire charges?");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "allow fire <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Allow fire/flint & tinder?");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "allow buckets <t/f>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Allow buckets?");
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
				return new HazardMenu();
			}else{
				plugin.getConfig().set("hazards.allow_eggs", ASUtils.getValueOf(value));
			}
		}else if(input.startsWith("allow exp bottle")){
			String value = input.replace("allow exp bottle", "").trim();
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new HazardMenu();
			}else{
				plugin.getConfig().set("hazards.allow_exp_bottle", ASUtils.getValueOf(value));
			}
		}else if(input.startsWith("allow bedrock")){
			String value = input.replace("allow bedrock", "").trim();
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new HazardMenu();
			}else{
				plugin.getConfig().set("hazards.allow_bedrock", ASUtils.getValueOf(value));
			}
		}else if(input.startsWith("allow tnt")){
			String value = input.replace("allow tnt", "").trim();
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new HazardMenu();
			}else{
				plugin.getConfig().set("hazards.allow_tnt", ASUtils.getValueOf(value));
			}
		}else if(input.startsWith("allow fire charge")){
			String value = input.replace("allow fire charge", "").trim();
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new HazardMenu();
			}else{
				plugin.getConfig().set("hazards.allow_fire_charge", ASUtils.getValueOf(value));
			}
		}else if(input.startsWith("allow fire")){
			String value = input.replace("allow fire", "").trim();
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new HazardMenu();
			}else{
				plugin.getConfig().set("hazards.allow_flint", ASUtils.getValueOf(value));
			}
		}else if(input.startsWith("allow buckets")){
			String value = input.replace("allow buckets", "").trim();
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new HazardMenu();
			}else{
				plugin.getConfig().set("hazards.allow_buckets", ASUtils.getValueOf(value));
			}
		}
		plugin.getConfig().save();
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "as rl");
		ASUtils.sendToConversable(context.getForWhom(), ChatColor.GREEN + "Value Saved!");
		return new WaitPrompt(new OtherEditor());
	}

	private void displayValue(String input, Conversable target, AntiShare plugin){
		String value = "UNKNOWN";
		if(input.startsWith("allow eggs")){
			value = String.valueOf(plugin.getConfig().getBoolean("hazards.allow_eggs"));
		}else if(input.startsWith("allow exp bottle")){
			value = String.valueOf(plugin.getConfig().getBoolean("hazards.allow_exp_bottle"));
		}else if(input.startsWith("allow bedrock")){
			value = String.valueOf(plugin.getConfig().getBoolean("hazards.allow_bedrock"));
		}else if(input.startsWith("allow tnt")){
			value = String.valueOf(plugin.getConfig().getBoolean("hazards.allow_tnt"));
		}else if(input.startsWith("allow fire charge")){
			value = String.valueOf(plugin.getConfig().getBoolean("hazards.allow_fire_charge"));
		}else if(input.startsWith("allow fire")){
			value = String.valueOf(plugin.getConfig().getBoolean("hazards.allow_flint"));
		}else if(input.startsWith("allow buckets")){
			value = String.valueOf(plugin.getConfig().getBoolean("hazards.allow_buckets"));
		}
		ASUtils.sendToConversable(target, ChatColor.GOLD + input + ChatColor.YELLOW + " is currently set to " + ChatColor.GOLD + value);
	}
}
