package com.turt2live.antishare.tekkitcompat;

import org.bukkit.command.CommandSender;

public class CommandBlockLayer {

	public static boolean isCommandBlock(CommandSender sender){
		return sender.getClass().getName().equals("org.bukkit.command.BlockCommandSender");
	}

}
