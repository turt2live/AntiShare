package com.turt2live.antishare.conversations.configuration;

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

public class NotificationEditor extends ASMenu {

	// TODO: Uncomment throw to region when known issue is fixed

	public NotificationEditor(){
		super("show", "help", "on", "off", "console",

				"legal block break", "legal block place", "legal death", "legal drop item", "legal eggs", "legal interact",
				"legal exp bottle", "legal bedrock", "legal creative block", "legal survival block", "legal pvp", "legal mob pvp",
				"legal command", "legal world swap", "legal throw to region", "legal fire charge", "legal bucket", "legal fire",
				"legal tnt",

				"illegal block break", "illegal block place", "illegal death", "illegal drop item", "illegal eggs", "illegal interact",
				"illegal exp bottle", "illegal bedrock", "illegal creative block", "illegal survival block", "illegal pvp", "illegal mob pvp",
				"illegal command", "illegal world swap", "illegal throw to region", "illegal fire charge", "illegal bucket", "illegal fire",
				"illegal tnt",

				"gamemode change", "region enter", "region exit", "creative explosions");
	}

	@Override
	public void displayMenu(Conversable target){
		ASUtils.sendToConversable(target, ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "Notifications" + ChatColor.DARK_GREEN + " ]=======");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "<type> <value>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Sets <type> to on or off");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "help" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Shows a list of <type>'s");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "on" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Allows sending of notifications");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "off" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Stops sending of notifications");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "console <true/false>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "True to alert console");
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input){
		input = input.toLowerCase().trim();
		AntiShare plugin = (AntiShare) context.getPlugin();
		Conversable target = context.getForWhom();
		if(input.startsWith("help")){
			showMessageTypes(target);
			return new WaitPrompt(new NotificationEditor());
		}else if(input.startsWith("show")){
			String node = input.replace("show", "").trim();
			if(nodeIsValid(node)){
				String value = plugin.getConfig().getString("notification." + getProperNode(node));
				ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "Current value for '" + node + "': " + value);
			}else{
				ASUtils.sendToConversable(context.getForWhom(), ChatColor.DARK_RED + "=======[ " + ChatColor.RED + "Invalid Type" + ChatColor.DARK_RED + " ]=======");
				ASUtils.sendToConversable(context.getForWhom(), ChatColor.RED + "Please use one of the following: ");
				showMessageTypes(target);
			}
			return new WaitPrompt(new NotificationEditor());
		}else if(input.startsWith("back")){
			return new EditConfigurationMenu();
		}else if(input.equalsIgnoreCase("on")){
			plugin.getConfig().set("notification.send", true);
			plugin.getConfig().save();
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "as rl");
			ASUtils.sendToConversable(context.getForWhom(), ChatColor.GREEN + "Value Saved!");
			return new WaitPrompt(new NotificationEditor());
		}else if(input.equalsIgnoreCase("off")){
			plugin.getConfig().set("notification.send", false);
			plugin.getConfig().save();
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "as rl");
			ASUtils.sendToConversable(context.getForWhom(), ChatColor.GREEN + "Value Saved!");
			return new WaitPrompt(new NotificationEditor());
		}else if(input.startsWith("console")){
			String value = input.replace("console", "").trim();
			if(ASUtils.getValueOf(value) != null){
				plugin.getConfig().set("notification.console", ASUtils.getValueOf(value));
				plugin.getConfig().save();
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "as rl");
				ASUtils.sendToConversable(context.getForWhom(), ChatColor.GREEN + "Value Saved!");
			}else{
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new NotificationEditor();
			}
			return new WaitPrompt(new NotificationEditor());
		}else{ // All the other nodes
			String value = (String) context.getSessionData("notifications_no_node");
			if(ASUtils.getValueOf(value) == null){
				ConfigurationConversation.showError(target, ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new NotificationEditor();
			}else{
				plugin.getConfig().set("notification." + getProperNode(input), value);
				plugin.getConfig().save();
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "as rl");
				ASUtils.sendToConversable(context.getForWhom(), ChatColor.GREEN + "Value Saved!");
				return new WaitPrompt(new NotificationEditor());
			}
		}
	}

	private String getProperNode(String node){
		node = node.toLowerCase().trim();
		boolean legal = node.startsWith("legal");
		boolean illegal = node.startsWith("illegal");
		if(legal){
			node = node.replaceFirst("legal", "");
		}else if(illegal){
			node = node.replaceFirst("illegal", "");
		}
		if(node.startsWith("block")){ // Place / Break
			node = node.replace("block ", "block_");
		}else if(node.startsWith("drop item")){
			node = "drop_item";
		}else if(node.startsWith("gamemode change")){
			node = "gamemode_change";
		}else if(node.startsWith("creative block")){
			node = "creative_block_break";
		}else if(node.startsWith("survival block")){
			node = "survival_block_break";
		}else if(node.startsWith("eggs")){
			node = "egg";
		}else if(node.startsWith("exp bottle")){
			node = "exp_bottle";
		}else if(node.startsWith("mob pvp")){
			node = "mob-pvp";
		}else if(node.startsWith("command")){
			node = "illegalCommand";
		}else if(node.startsWith("world swap")){
			node = "world_transfer";
		}else if(node.startsWith("throw to region")){
			node = "region_item";
		}else if(node.startsWith("bedrock")){
			node = "bedrock_attempt";
		}else if(node.startsWith("region")){ // Enter / Exit
			node = node.replace("region ", "region_");
		}else if(node.startsWith("fire charge")){
			node = node.replace("fire charge", "fire_charge");
		}else if(node.startsWith("tnt")){
			node = node.replace("tnt", "tnt-place");
		}else if(node.startsWith("creative explosions")){
			node = node.replace("creative explosions", "tnt_creative_explosion");
		}
		// death, interact, bucket, fire, and pvp are all covered
		String prefix = "general.";
		if(legal){
			prefix = "legal.";
		}else if(illegal){
			prefix = "illegal.";
		}
		return prefix + node;
	}

	private boolean nodeIsValid(String node){
		node = node.toLowerCase().trim();
		if(!node.startsWith("illegal") && !node.startsWith("legal")){
			return false;
		}
		return node.startsWith("legal block break") || node.startsWith("legal block place") || node.startsWith("legal death")
				|| node.startsWith("legal drop item") || node.startsWith("legal eggs") || node.startsWith("legal interact")
				|| node.startsWith("legal exp bottle") || node.startsWith("legal bedrock") || node.startsWith("legal creative block")
				|| node.startsWith("legal survival block") || node.startsWith("legal pvp") || node.startsWith("legal mob pvp")
				|| node.startsWith("legal command") || node.startsWith("legal world swap") || node.startsWith("legal throw to region")
				|| node.startsWith("legal fire") /* Covers fire charge */|| node.startsWith("legal tnt") || node.startsWith("legal bucket")

				|| node.startsWith("illegal block break") || node.startsWith("illegal block place") || node.startsWith("illegal death")
				|| node.startsWith("illegal drop item") || node.startsWith("illegal eggs") || node.startsWith("illegal interact")
				|| node.startsWith("illegal exp bottle") || node.startsWith("illegal bedrock") || node.startsWith("illegal creative block")
				|| node.startsWith("illegal survival block") || node.startsWith("illegal pvp") || node.startsWith("illegal mob pvp")
				|| node.startsWith("illegal command") || node.startsWith("illegal world swap") || node.startsWith("illegal throw to region")
				|| node.startsWith("illegal fire") /* Covers fire charge */|| node.startsWith("illegal tnt") || node.startsWith("illegal bucket")

				|| node.startsWith("gamemode change") || node.startsWith("region enter") || node.startsWith("region exit")
				|| node.startsWith("creative explosions");

	}

	private void showMessageTypes(Conversable target){
		ASUtils.sendToConversable(target, ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "Notification Types" + ChatColor.DARK_GREEN + " ]=======");
		ASUtils.sendToConversable(target, ChatColor.AQUA
				+ "<i/l> block break  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  <i/l> block place  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  <i/l> death");
		ASUtils.sendToConversable(target, ChatColor.AQUA
				+ "<i/l> drop item  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  <i/l> interact  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  <i/l> eggs");
		ASUtils.sendToConversable(target, ChatColor.AQUA
				+ "<i/l> exp bottle  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  <i/l> command  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  <i/l> bedrock");
		ASUtils.sendToConversable(target, ChatColor.AQUA
				+ "<i/l> creative block  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "<i/l> survival block  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  <i/l> mob pvp");
		ASUtils.sendToConversable(target, ChatColor.AQUA
				+ "<i/l> pvp  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  <i/l> world swap  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  <i/l> fire");
		ASUtils.sendToConversable(target, ChatColor.AQUA
				+ "<i/l> fire charge  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  <i/l> bucket  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  <i/l> tnt");
		ASUtils.sendToConversable(target, ChatColor.AQUA
				+ "  <i/l> throw to region" + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "region enter  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  gamemode change");
		ASUtils.sendToConversable(target, ChatColor.AQUA
				+ "  region exit  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  creative explosions");
		ASUtils.sendToConversable(target, ChatColor.GREEN + "Note: " + ChatColor.DARK_GREEN + "<i/l>" + ChatColor.GREEN + " means you need to specify illegal or legal");
	}

}
