package com.turt2live.antishare;

public class UpdateChecker {

	/*
	 * Class function replicated from Vault (thanks Sleaker!)
	 * 
	 * URL: https://github.com/MilkBowl/Vault/blob/master/src/net/milkbowl/vault/Vault.java#L118
	 */

	public UpdateChecker(final AntiShare plugin){
		plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable(){
			@Override
			public void run(){
				try{
					if(plugin.api.getUtilsAPI().isOutdated()){
						String newVersion = plugin.api.getUtilsAPI().getNewVersion();
						String currentVersion = plugin.api.getVersion();
						plugin.getLogger().warning("AntiShare " + newVersion + " is out! You are running AntiShare " + currentVersion);
						plugin.getLogger().warning("Update AntiShare at: http://dev.bukkit.org/server-mods/antishare");
					}
				}catch(Exception e){} // Don't handle
			}
		}, 0, 432000); // 30 minutes
	}

}
