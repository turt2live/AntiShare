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

public class SQLEditor extends ASMenu {

	/*
	 * ==================
	 * PORT IS NOT USED!!
	 * ==================
	 */

	public SQLEditor(){
		super("use", "host", "username", "password", "database"/*, "port"*/, "current");
	}

	@Override
	public void displayMenu(Conversable target){
		ASUtils.sendToConversable(target, ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "SQL Settings" + ChatColor.DARK_GREEN + " ]=======");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "use" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Set to true to turn on SQL support");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "host" + ChatColor.GOLD + " - " + ChatColor.AQUA + "The SQL host to connect to");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "username" + ChatColor.GOLD + " - " + ChatColor.AQUA + "The username to login to MySQL");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "password" + ChatColor.GOLD + " - " + ChatColor.AQUA + "The SQL password to use.");
		ASUtils.sendToConversable(target, ChatColor.RED + "Set passwords manually! Not here in Minecraft!");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "database" + ChatColor.GOLD + " - " + ChatColor.AQUA + "The MySQL database to use");
		//ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "port" + ChatColor.GOLD + " - " + ChatColor.AQUA + "The port to connect to");
		ASUtils.sendToConversable(target, ChatColor.DARK_AQUA + "current" + ChatColor.GOLD + " - " + ChatColor.AQUA + "Displays the current settings");
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input){
		input = input.toLowerCase().trim();
		AntiShare plugin = (AntiShare) context.getPlugin();
		if(input.startsWith("use")){
			input = input.replace("use", "").trim();
			if(ASUtils.getValueOf(input) == null){
				ConfigurationConversation.showError(context.getForWhom(), ChatColor.RED + "'" + input + "' is not valid! Did you mean true, or false?");
				return new SQLEditor();
			}else{
				plugin.getConfig().set("SQL.use", ASUtils.getValueOf(input));
			}
		}else if(input.startsWith("host")){
			input = input.replace("host", "").trim();
			plugin.getConfig().set("SQL.host", input);
		}else if(input.startsWith("username")){
			input = input.replace("username", "").trim();
			plugin.getConfig().set("SQL.username", input);
		}else if(input.startsWith("password")){
			input = input.replace("password", "").trim();
			plugin.getConfig().set("SQL.password", input);
		}else if(input.startsWith("database")){
			input = input.replace("database", "").trim();
			plugin.getConfig().set("SQL.database", input);
		}else if(input.startsWith("current")){
			String use = (plugin.getConfig().getBoolean("SQL.use") ? ChatColor.GREEN + "ACTIVE" : ChatColor.RED + "INACTIVE") + ChatColor.DARK_AQUA;
			String host = ChatColor.AQUA + plugin.getConfig().getString("SQL.host") + ChatColor.DARK_AQUA;
			String username = ChatColor.AQUA + plugin.getConfig().getString("SQL.username") + ChatColor.DARK_AQUA;
			String password = ChatColor.AQUA + "***** (Censored)" + ChatColor.DARK_AQUA;
			//String port = ChatColor.AQUA + "" + plugin.getConfig().getInt("SQL.port") + ChatColor.DARK_AQUA;
			String database = ChatColor.AQUA + plugin.getConfig().getString("SQL.database") + ChatColor.DARK_AQUA;
			ASUtils.sendToConversable(context.getForWhom(), ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "Current SQL Settings" + ChatColor.DARK_GREEN + " ]=======");
			ASUtils.sendToConversable(context.getForWhom(), ChatColor.DARK_AQUA + "SQL is " + use + " on host " + host + /* " on port " + port + */" with username " + username + " and password " + password + " on the database " + database);
			return new WaitPrompt(new SQLEditor());
		}else if(input.startsWith("back")){
			return new EditConfigurationMenu();
		}
		plugin.getConfig().save();
		plugin.reloadConfig();
		ASUtils.sendToConversable(context.getForWhom(), ChatColor.GREEN + "Value Saved!");
		return new SQLEditor();
	}

}
