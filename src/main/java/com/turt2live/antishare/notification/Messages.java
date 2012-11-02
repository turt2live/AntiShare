package com.turt2live.antishare.notification;

import java.io.File;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.lib.feildmaster.configuration.EnhancedConfiguration;

/**
 * Represents messages in AntiShare
 * 
 * @author turt2live
 */
public class Messages {

	private EnhancedConfiguration messages;

	/**
	 * Creates a new Message handler
	 */
	public Messages(){
		reload();
	}

	/**
	 * Reloads messages
	 */
	public void reload(){
		// Setup configuration
		messages = new EnhancedConfiguration(new File(AntiShare.getInstance().getDataFolder(), "messages.yml"), AntiShare.getInstance());
		messages.loadDefaults(AntiShare.getInstance().getResource("resources/messages.yml"));
		if(!messages.fileExists() || !messages.checkDefaults()){
			messages.saveDefaults();
		}
		messages.load();
	}

	/**
	 * Gets a message
	 * 
	 * @param path the message path
	 * @return the message
	 */
	public String getMessage(String path){
		String message = messages.getString(path);
		if(message == null){
			return "no message";
		}
		return message;
	}

}
