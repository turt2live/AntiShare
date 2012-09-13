/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.notification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;

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

	/**
	 * Prints the entire contents of the messages.yml to the writer
	 * 
	 * @param out the writer
	 * @throws IOException for external handling
	 */
	public void print(BufferedWriter out) throws IOException{
		for(String key : messages.getKeys(true)){
			out.write(key + ": " + (messages.getString(key).startsWith("MemorySection") ? "" : messages.getString(key, "")) + "\r\n");
		}
	}
}
