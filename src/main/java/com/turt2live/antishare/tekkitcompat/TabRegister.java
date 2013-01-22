package com.turt2live.antishare.tekkitcompat;

import org.bukkit.command.PluginCommand;

import com.turt2live.antishare.TabHandler;

/**
 * Abstraction layer for Tab Complete
 * 
 * @author turt2live
 * 
 */
public class TabRegister {

	/**
	 * Register a command to have the default AntiShare tab handler
	 * 
	 * @param command the command to apply to
	 */
	public static void register(PluginCommand command){
		command.setTabCompleter(new TabHandler());
	}

}
