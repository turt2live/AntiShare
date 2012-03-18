package com.turt2live.antishare.conversations.configuration;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.conversations.ASMenu;
import com.turt2live.antishare.conversations.WaitPrompt;

public class MessageEditor extends ASMenu {

	// TODO: Uncomment throw to region when a better solution is ready

	public MessageEditor(){
		super("show", "help", "block break", "block place", "death", "drop item", "eggs", "interact",
				"exp bottle", "inventory swap", "bedrock", "creative block", "pvp", "mob pvp", "no drops",
				"command", "world swap"/*, "throw to region"*/);
	}

	@Override
	public void displayMenu(Conversable target){
		ASUtils.sendToConversable(target, ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "Messages" + ChatColor.DARK_GREEN + " ]=======");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "<type> <value>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Sets the message for <type>");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "help" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Shows a list of <type>'s");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "show <type>" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Shows the current value of <type>");
		ASUtils.sendToConversable(target, ChatColor.GOLD + "Note: " + ChatColor.YELLOW + "Messages are only sent on illegal actions");
		//ASUtils.sendToConversable(target, ChatColor.GOLD + "Note: " + ChatColor.YELLOW + "For 'throw to region', use {name} to use the name of the region!");
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input){
		input = input.toLowerCase().trim();
		AntiShare plugin = (AntiShare) context.getPlugin();
		Conversable target = context.getForWhom();
		if(input.startsWith("help")){
			showMessageTypes(target);
			return new WaitPrompt(new MessageEditor());
		}else if(input.startsWith("show")){
			String node = input.replace("show", "").trim();
			if(nodeIsValid(node)){
				String value = plugin.getConfig().getString("messages." + getProperNode(node));
				ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "Current value for '" + node + "': " + value);
			}else{
				ASUtils.sendToConversable(context.getForWhom(), ChatColor.DARK_RED + "=======[ " + ChatColor.RED + "Invalid Type" + ChatColor.DARK_RED + " ]=======");
				ASUtils.sendToConversable(context.getForWhom(), ChatColor.RED + "Please use one of the following: ");
				showMessageTypes(target);
			}
			return new WaitPrompt(new MessageEditor());
		}else if(input.startsWith("back")){
			return new EditConfigurationMenu();
		}else{ // All the other nodes, no need to check if valid
			String value = (String) context.getSessionData("msg_no_node");
			plugin.getConfig().set("messages." + getProperNode(input), value);
			plugin.getConfig().save();
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "as rl");
			ASUtils.sendToConversable(context.getForWhom(), ChatColor.GREEN + "Value Saved!");
			return new WaitPrompt(new MessageEditor());
		}
	}

	private String getProperNode(String node){
		node = node.toLowerCase().trim();
		if(node.startsWith("block")){ // Place / Break
			node = node.replace("block ", "block_");
		}else if(node.startsWith("drop item")){
			node = "drop_item";
		}else if(node.startsWith("inventory swap")){
			node = "inventory_swap";
		}else if(node.startsWith("creative block")){
			node = "creativeModeBlock";
		}else if(node.startsWith("exp bottle")){
			node = "exp_bottle";
		}else if(node.startsWith("mob pvp")){
			node = "mobpvp";
		}else if(node.startsWith("no drops")){
			node = "noBlockDrop";
		}else if(node.startsWith("command")){
			node = "illegalCommand";
		}else if(node.startsWith("world swap")){
			node = "worldSwap";
			//}else if(node.startsWith("throw to region")){
			//	node = "throwItemIntoRegion";
		}
		// death, interact, bedrock, pvp, and eggs are all covered
		return node;
	}

	private boolean nodeIsValid(String node){
		node = node.toLowerCase().trim();
		return node.startsWith("block break") || node.startsWith("block place") || node.startsWith("death")
				|| node.startsWith("drop item") || node.startsWith("eggs") || node.startsWith("interact")
				|| node.startsWith("exp bottle") || node.startsWith("inventory swap") || node.startsWith("bedrock")
				|| node.startsWith("creative block") || node.startsWith("pvp") || node.startsWith("mob pvp")
				|| node.startsWith("no drops") || node.startsWith("command") || node.startsWith("world swap")
		/*|| node.startsWith("throw to region")*/;

	}

	private void showMessageTypes(Conversable target){
		ASUtils.sendToConversable(target, ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "Message Types" + ChatColor.DARK_GREEN + " ]=======");
		ASUtils.sendToConversable(target, ChatColor.AQUA
				+ "block break  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  block place  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  death");
		ASUtils.sendToConversable(target, ChatColor.AQUA
				+ "drop item  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  interact  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  eggs");
		ASUtils.sendToConversable(target, ChatColor.AQUA
				+ "exp bottle  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  inventory swap  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  bedrock");
		ASUtils.sendToConversable(target, ChatColor.AQUA
				+ "creative block  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  pvp  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  mob pvp");
		ASUtils.sendToConversable(target, ChatColor.AQUA
				+ "no drops  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  command  " + ChatColor.DARK_AQUA + "|" + ChatColor.AQUA
				+ "  world swap");
		//ASUtils.sendToConversable(target, ChatColor.AQUA + "throw to region");
	}

}
