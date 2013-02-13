package com.turt2live.antishare.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * A utility class to store messages to be represented as "errors"
 */
public class ErrorStringList extends StringList {

	private String message;

	/**
	 * Creates a new Error String List with an error message
	 * 
	 * @param error the error message
	 * @param strings collection of options for the String List
	 */
	public ErrorStringList(String error, String... strings){
		super(strings);
		this.message = error;
	}

	@Override
	public void print(CommandSender sender){
		ASUtils.sendToPlayer(sender, ChatColor.RED + message, true);
	}

}
