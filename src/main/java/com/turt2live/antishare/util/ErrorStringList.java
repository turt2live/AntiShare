package com.turt2live.antishare.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * A utility class to store messages to be represented as "errors"
 */
public class ErrorStringList extends StringList {

	private String msg;

	/**
	 * Creates a new Error String List with an error message 
	 * @param err the error message
	 * @param list collection of options for the String List
	 */
	public ErrorStringList(String err, String... list){
		super(list);
		this.msg = err;
	}

	@Override
	public void print(CommandSender sender){
		ASUtils.sendToPlayer(sender, ChatColor.RED + msg, true);
	}

}
