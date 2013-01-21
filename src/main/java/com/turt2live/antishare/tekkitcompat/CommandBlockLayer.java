package com.turt2live.antishare.tekkitcompat;

import org.bukkit.command.CommandSender;

public class CommandBlockLayer {

	/**
	 * Determines if the command sender sent is a Command Block. This is safe for use in Tekkit
	 * @param sender the command sender
	 * @return true if the command sender is a command block, false otherwise
	 */
	public static boolean isCommandBlock(CommandSender sender){
		return sender.getClass().getName().equals("org.bukkit.command.BlockCommandSender");
	}

}
