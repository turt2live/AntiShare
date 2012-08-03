package com.turt2live.antishare;

import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

import com.turt2live.antishare.AntiShare.LogType;

public class ConflictThread implements Runnable {

	private AntiShare plugin = AntiShare.getInstance();

	@Override
	public void run(){
		Plugin mvinv = plugin.getServer().getPluginManager().getPlugin("Multiverse-Inventories");
		if(mvinv != null){
			if(plugin.getConfig().getBoolean("handled-actions.gamemode-inventories")){
				AntiShare.log("*************************************************************************************", Level.WARNING, LogType.BYPASS);
				AntiShare.log("* You have Multiverse-Inventories and GameMode Inventories enabled!                 *", Level.WARNING, LogType.BYPASS);
				AntiShare.log("* This may cause issues or unexpected results when both plugins handle inventories. *", Level.WARNING, LogType.BYPASS);
				AntiShare.log("* I suggest you edit my configuration to allow Multiverse to do it's job            *", Level.WARNING, LogType.BYPASS);
				AntiShare.log("*************************************************************************************", Level.WARNING, LogType.BYPASS);
			}
			if(plugin.getConfig().getBoolean("handled-actions.world-transfers")){
				AntiShare.log("*************************************************************************************", Level.WARNING, LogType.BYPASS);
				AntiShare.log("* You have Multiverse-Inventories and World Inventories enabled!                    *", Level.WARNING, LogType.BYPASS);
				AntiShare.log("* This may cause issues or unexpected results when both plugins handle inventories. *", Level.WARNING, LogType.BYPASS);
				AntiShare.log("* I suggest you edit my configuration to allow Multiverse to do it's job            *", Level.WARNING, LogType.BYPASS);
				AntiShare.log("*************************************************************************************", Level.WARNING, LogType.BYPASS);
			}
		}
		Plugin multiinv = plugin.getServer().getPluginManager().getPlugin("MultiInv");
		if(multiinv != null){
			if(plugin.getConfig().getBoolean("handled-actions.gamemode-inventories")){
				AntiShare.log("*************************************************************************************", Level.WARNING, LogType.BYPASS);
				AntiShare.log("* You have MultiInv and GameMode Inventories enabled!                               *", Level.WARNING, LogType.BYPASS);
				AntiShare.log("* This may cause issues or unexpected results when both plugins handle inventories. *", Level.WARNING, LogType.BYPASS);
				AntiShare.log("* I suggest you edit my configuration to allow MultiInv to do it's job              *", Level.WARNING, LogType.BYPASS);
				AntiShare.log("*************************************************************************************", Level.WARNING, LogType.BYPASS);
			}
			if(plugin.getConfig().getBoolean("handled-actions.world-transfers")){
				AntiShare.log("*************************************************************************************", Level.WARNING, LogType.BYPASS);
				AntiShare.log("* You have MultiInv and World Inventories enabled!                                  *", Level.WARNING, LogType.BYPASS);
				AntiShare.log("* This may cause issues or unexpected results when both plugins handle inventories. *", Level.WARNING, LogType.BYPASS);
				AntiShare.log("* I suggest you edit my configuration to allow MultiInv to do it's job              *", Level.WARNING, LogType.BYPASS);
				AntiShare.log("*************************************************************************************", Level.WARNING, LogType.BYPASS);
			}
		}
	}

}
