/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare;

/**
 * Update Checker
 * 
 * @author turt2live
 */
public class UpdateChecker {

	/*
	 * Class function replicated from Vault (thanks Sleaker!)
	 * 
	 * URL: https://github.com/MilkBowl/Vault/blob/master/src/net/milkbowl/vault/Vault.java#L118
	 */

	/**
	 * Creates and starts the update checker
	 */
	public static void start(){
		final AntiShare plugin = AntiShare.getInstance();
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			@Override
			public void run(){
				try{
					if(AntiShare.isOutdated()){
						String newVersion = AntiShare.getNewVersionString();
						String currentVersion = AntiShare.getVersion();
						plugin.getLogger().warning("AntiShare " + newVersion + " is out! You are running AntiShare " + currentVersion);
						plugin.getLogger().warning("Update AntiShare at: http://dev.bukkit.org/server-mods/antishare");
					}
				}catch(Exception e){} // Don't handle
			}
		}, 0, 432000); // 30 minutes
	}

}
