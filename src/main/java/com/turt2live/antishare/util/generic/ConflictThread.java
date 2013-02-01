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

import org.bukkit.plugin.Plugin;

import com.turt2live.antishare.AntiShare;

public class ConflictThread implements Runnable {

	private final AntiShare plugin = AntiShare.getInstance();

	@Override
	public void run(){
		if(plugin.getConfig().getBoolean("other.stop-spam-for-conflicts")){
			return;
		}
		boolean warningIssued = false;
		// TODO: Locale
		Plugin mv = plugin.getServer().getPluginManager().getPlugin("Multiverse");
		if(mv != null){
			if(plugin.getConfig().getBoolean("handled-actions.gamemode-inventories")){
				plugin.getLogger().warning("*************************************************************************************");
				plugin.getLogger().warning("* You have Multiverse installed and GameMode Inventories enabled!                   *");
				plugin.getLogger().warning("* Although bugs are not expected to occur, they are still possible. If unexpected   *");
				plugin.getLogger().warning("* inventory bugs occur, or world bugs, check your configurations in both plugins.   *");
				plugin.getLogger().warning("* Inventory loss bugs have been reported and can be solved with correct configs.    *");
				plugin.getLogger().warning("*************************************************************************************");
				warningIssued = true;
			}
			if(plugin.getConfig().getBoolean("handled-actions.world-transfers")){
				plugin.getLogger().warning("*************************************************************************************");
				plugin.getLogger().warning("* You have Multiverse installed and World Transfers enabled!                        *");
				plugin.getLogger().warning("* Although bugs are not expected to occur, they are still possible. If unexpected   *");
				plugin.getLogger().warning("* inventory bugs occur, or world bugs, check your configurations in both plugins.   *");
				plugin.getLogger().warning("* Inventory loss bugs have been reported and can be solved with correct configs.    *");
				plugin.getLogger().warning("*************************************************************************************");
				warningIssued = true;
			}
		}
		Plugin mvinv = plugin.getServer().getPluginManager().getPlugin("Multiverse-Inventories");
		if(mvinv != null){
			if(plugin.getConfig().getBoolean("handled-actions.gamemode-inventories")){
				plugin.getLogger().warning("*************************************************************************************");
				plugin.getLogger().warning("* You have Multiverse-Inventories and GameMode Inventories enabled!                 *");
				plugin.getLogger().warning("* This may cause issues or unexpected results when both plugins handle inventories. *");
				plugin.getLogger().warning("* I suggest you edit my configuration to allow Multiverse to do it's job            *");
				plugin.getLogger().warning("*************************************************************************************");
				warningIssued = true;
			}
			if(plugin.getConfig().getBoolean("handled-actions.world-transfers")){
				plugin.getLogger().warning("*************************************************************************************");
				plugin.getLogger().warning("* You have Multiverse-Inventories and World Inventories enabled!                    *");
				plugin.getLogger().warning("* This may cause issues or unexpected results when both plugins handle inventories. *");
				plugin.getLogger().warning("* I suggest you edit my configuration to allow Multiverse to do it's job            *");
				plugin.getLogger().warning("*************************************************************************************");
				warningIssued = true;
			}
		}
		Plugin multiinv = plugin.getServer().getPluginManager().getPlugin("MultiInv");
		if(multiinv != null){
			if(plugin.getConfig().getBoolean("handled-actions.gamemode-inventories")){
				plugin.getLogger().warning("*************************************************************************************");
				plugin.getLogger().warning("* You have MultiInv and GameMode Inventories enabled!                               *");
				plugin.getLogger().warning("* This may cause issues or unexpected results when both plugins handle inventories. *");
				plugin.getLogger().warning("* I suggest you edit my configuration to allow MultiInv to do it's job              *");
				plugin.getLogger().warning("*************************************************************************************");
				warningIssued = true;
			}
			if(plugin.getConfig().getBoolean("handled-actions.world-transfers")){
				plugin.getLogger().warning("*************************************************************************************");
				plugin.getLogger().warning("* You have MultiInv and World Inventories enabled!                                  *");
				plugin.getLogger().warning("* This may cause issues or unexpected results when both plugins handle inventories. *");
				plugin.getLogger().warning("* I suggest you edit my configuration to allow MultiInv to do it's job              *");
				plugin.getLogger().warning("*************************************************************************************");
				warningIssued = true;
			}
		}
		if(warningIssued){
			plugin.getLogger().info("You can turn off the above 'spam' in the configuration. Look at the bottom of AntiShare's config.yml.");
		}
	}

}
