package com.turt2live.antishare.debug;

import org.bukkit.command.CommandSender;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.enums.AlertType;

public class AlertTimer {

	private CommandSender sender;
	private long lastSent = 0L;
	private AlertType type;

	public AlertTimer(AlertType type, CommandSender target){
		this.sender = target;
		this.type = type;
	}

	public void sendMessage(String message){
		if((System.currentTimeMillis() - lastSent) >= 1000){
			ASUtils.sendToPlayer(sender, message);
			lastSent = System.currentTimeMillis();
		}
	}

	public CommandSender getTarget(){
		return sender;
	}

	public AlertType getType(){
		return type;
	}
}
