/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.util.generic;

import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

import com.turt2live.antishare.AntiShare;

public class ConflictThread implements Runnable {

	private AntiShare plugin = AntiShare.getInstance();

	@Override
	public void run(){
		Plugin mv = plugin.getServer().getPluginManager().getPlugin("Multiverse");
		if(mv != null){
			if(plugin.getConfig().getBoolean("handled-actions.gamemode-inventories")){
				plugin.log("*************************************************************************************", Level.WARNING);
				plugin.log("* You have Multiverse installed and GameMode Inventories enabled!                   *", Level.WARNING);
				plugin.log("* Although bugs are not expected to occur, they are still possible. If unexpected   *", Level.WARNING);
				plugin.log("* inventory bugs occur, or world bugs, check your configurations in both plugins.   *", Level.WARNING);
				plugin.log("* Inventory loss bugs have been reported and can be solved with correct configs.    *", Level.WARNING);
				plugin.log("*************************************************************************************", Level.WARNING);
			}
			if(plugin.getConfig().getBoolean("handled-actions.world-transfers")){
				plugin.log("*************************************************************************************", Level.WARNING);
				plugin.log("* You have Multiverse installed and World Transfers enabled!                        *", Level.WARNING);
				plugin.log("* Although bugs are not expected to occur, they are still possible. If unexpected   *", Level.WARNING);
				plugin.log("* inventory bugs occur, or world bugs, check your configurations in both plugins.   *", Level.WARNING);
				plugin.log("* Inventory loss bugs have been reported and can be solved with correct configs.    *", Level.WARNING);
				plugin.log("*************************************************************************************", Level.WARNING);
			}
		}
		Plugin mvinv = plugin.getServer().getPluginManager().getPlugin("Multiverse-Inventories");
		if(mvinv != null){
			if(plugin.getConfig().getBoolean("handled-actions.gamemode-inventories")){
				plugin.log("*************************************************************************************", Level.WARNING);
				plugin.log("* You have Multiverse-Inventories and GameMode Inventories enabled!                 *", Level.WARNING);
				plugin.log("* This may cause issues or unexpected results when both plugins handle inventories. *", Level.WARNING);
				plugin.log("* I suggest you edit my configuration to allow Multiverse to do it's job            *", Level.WARNING);
				plugin.log("*************************************************************************************", Level.WARNING);
			}
			if(plugin.getConfig().getBoolean("handled-actions.world-transfers")){
				plugin.log("*************************************************************************************", Level.WARNING);
				plugin.log("* You have Multiverse-Inventories and World Inventories enabled!                    *", Level.WARNING);
				plugin.log("* This may cause issues or unexpected results when both plugins handle inventories. *", Level.WARNING);
				plugin.log("* I suggest you edit my configuration to allow Multiverse to do it's job            *", Level.WARNING);
				plugin.log("*************************************************************************************", Level.WARNING);
			}
		}
		Plugin multiinv = plugin.getServer().getPluginManager().getPlugin("MultiInv");
		if(multiinv != null){
			if(plugin.getConfig().getBoolean("handled-actions.gamemode-inventories")){
				plugin.log("*************************************************************************************", Level.WARNING);
				plugin.log("* You have MultiInv and GameMode Inventories enabled!                               *", Level.WARNING);
				plugin.log("* This may cause issues or unexpected results when both plugins handle inventories. *", Level.WARNING);
				plugin.log("* I suggest you edit my configuration to allow MultiInv to do it's job              *", Level.WARNING);
				plugin.log("*************************************************************************************", Level.WARNING);
			}
			if(plugin.getConfig().getBoolean("handled-actions.world-transfers")){
				plugin.log("*************************************************************************************", Level.WARNING);
				plugin.log("* You have MultiInv and World Inventories enabled!                                  *", Level.WARNING);
				plugin.log("* This may cause issues or unexpected results when both plugins handle inventories. *", Level.WARNING);
				plugin.log("* I suggest you edit my configuration to allow MultiInv to do it's job              *", Level.WARNING);
				plugin.log("*************************************************************************************", Level.WARNING);
			}
		}
	}

}
