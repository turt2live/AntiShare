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
		final AntiShare plugin = AntiShare.instance;
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			@Override
			public void run(){
				try{
					if(AntiShare.isOutdated()){
						String newVersion = AntiShare.getNewVersion();
						String currentVersion = AntiShare.getVersion();
						plugin.getLogger().warning("AntiShare " + newVersion + " is out! You are running AntiShare " + currentVersion);
						plugin.getLogger().warning("Update AntiShare at: http://dev.bukkit.org/server-mods/antishare");
					}
				}catch(Exception e){} // Don't handle
			}
		}, 0, 432000); // 30 minutes
	}

}
