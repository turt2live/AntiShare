/*******************************************************************************
 * Copyright (c) 2013 Travis Ralston.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.util;

import java.io.File;
import java.io.IOException;

import org.bukkit.GameMode;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;

public class MoneySaver {

	private static EnhancedConfiguration getFile(){
		AntiShare plugin = AntiShare.p;
		File file = new File(plugin.getDataFolder(), "data" + File.separator + "balance.yml");
		if(!file.exists()){
			try{
				file.createNewFile();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		EnhancedConfiguration yamlFile = new EnhancedConfiguration(file, plugin);
		yamlFile.load();
		return yamlFile;
	}

	/**
	 * Gets the balance for a player
	 * 
	 * @param player the player
	 * @param gamemode the gamemode to get the balance for
	 * @return a double (balance). If not found this will return 0
	 */
	public static double getLevel(String player, GameMode gamemode){
		EnhancedConfiguration yaml = getFile();
		double balance = yaml.getDouble(player + "." + gamemode.name(), 0.0);
		return balance;
	}

	/**
	 * Saves a balance for a player
	 * 
	 * @param player the player
	 * @param gamemode the gamemode to save as
	 * @param balance the balance to save
	 */
	public static void saveLevel(String player, GameMode gamemode, double balance){
		if(balance <= 0){
			return;
		}
		EnhancedConfiguration yaml = getFile();
		yaml.set(player + "." + gamemode.name(), balance);
		yaml.save();
	}

}
