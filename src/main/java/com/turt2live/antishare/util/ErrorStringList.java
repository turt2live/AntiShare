package com.turt2live.antishare.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ErrorStringList extends StringList {

	private String msg;

	public ErrorStringList(String err, String... list){
		super(list);
		this.msg = err;
	}

	@Override
	public boolean isError(){
		return true;
	}

	@Override
	public void print(CommandSender sender){
		ASUtils.sendToPlayer(sender, ChatColor.RED + msg, true);
	}

}
