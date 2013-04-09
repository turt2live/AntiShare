/*******************************************************************************
 * Copyright (c) 2013 Travis Ralston.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.io;

import java.io.File;
import java.io.IOException;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;

public class GameModeIdentity{

	private static EnhancedConfiguration getFile(){
		AntiShare plugin = AntiShare.p;
		File file = new File(plugin.getDataFolder(), "data" + File.separator + "gamemodeswitches.yml");
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
	 * Determines if a player has changed GameMode before
	 * 
	 * @param player the player to check
	 * @return true if they have changed Game Mode before
	 */
	public static boolean hasChangedGameMode(String player){
		EnhancedConfiguration yaml = getFile();
		return yaml.getBoolean(player, false);
	}

	/**
	 * Sets a player as "has changed Game Mode"
	 * 
	 * @param player the player
	 */
	public static void setChangedGameMode(String player){
		EnhancedConfiguration yaml = getFile();
		yaml.set(player, true);
		yaml.save();
	}

}
