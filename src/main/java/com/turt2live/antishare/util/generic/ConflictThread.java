package com.turt2live.antishare.util.generic;

import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

import com.turt2live.antishare.AntiShare;

public class ConflictThread implements Runnable {

	private AntiShare plugin = AntiShare.getInstance();

	@Override
	public void run(){
		Plugin mvinv = plugin.getServer().getPluginManager().getPlugin("Multiverse-Inventories");
		if(mvinv != null){
			if(plugin.getConfig().getBoolean("handled-actions.gamemode-inventories")){
				AntiShare.getInstance().log("*************************************************************************************", Level.WARNING);
				AntiShare.getInstance().log("* You have Multiverse-Inventories and GameMode Inventories enabled!                 *", Level.WARNING);
				AntiShare.getInstance().log("* This may cause issues or unexpected results when both plugins handle inventories. *", Level.WARNING);
				AntiShare.getInstance().log("* I suggest you edit my configuration to allow Multiverse to do it's job            *", Level.WARNING);
				AntiShare.getInstance().log("*************************************************************************************", Level.WARNING);
			}
			if(plugin.getConfig().getBoolean("handled-actions.world-transfers")){
				AntiShare.getInstance().log("*************************************************************************************", Level.WARNING);
				AntiShare.getInstance().log("* You have Multiverse-Inventories and World Inventories enabled!                    *", Level.WARNING);
				AntiShare.getInstance().log("* This may cause issues or unexpected results when both plugins handle inventories. *", Level.WARNING);
				AntiShare.getInstance().log("* I suggest you edit my configuration to allow Multiverse to do it's job            *", Level.WARNING);
				AntiShare.getInstance().log("*************************************************************************************", Level.WARNING);
			}
		}
		Plugin multiinv = plugin.getServer().getPluginManager().getPlugin("MultiInv");
		if(multiinv != null){
			if(plugin.getConfig().getBoolean("handled-actions.gamemode-inventories")){
				AntiShare.getInstance().log("*************************************************************************************", Level.WARNING);
				AntiShare.getInstance().log("* You have MultiInv and GameMode Inventories enabled!                               *", Level.WARNING);
				AntiShare.getInstance().log("* This may cause issues or unexpected results when both plugins handle inventories. *", Level.WARNING);
				AntiShare.getInstance().log("* I suggest you edit my configuration to allow MultiInv to do it's job              *", Level.WARNING);
				AntiShare.getInstance().log("*************************************************************************************", Level.WARNING);
			}
			if(plugin.getConfig().getBoolean("handled-actions.world-transfers")){
				AntiShare.getInstance().log("*************************************************************************************", Level.WARNING);
				AntiShare.getInstance().log("* You have MultiInv and World Inventories enabled!                                  *", Level.WARNING);
				AntiShare.getInstance().log("* This may cause issues or unexpected results when both plugins handle inventories. *", Level.WARNING);
				AntiShare.getInstance().log("* I suggest you edit my configuration to allow MultiInv to do it's job              *", Level.WARNING);
				AntiShare.getInstance().log("*************************************************************************************", Level.WARNING);
			}
		}
	}

}
