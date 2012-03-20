package com.turt2live.antishare.debug;

import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.enums.AlertType;
import com.turt2live.antishare.event.AntiShareBugEvent;
import com.turt2live.antishare.log.ASLog;

/*
 * This is used mostly while developing. There may be reminaing code here
 * but that code will be disabled if AntiShare.DEBUG_MODE is false
 */
public class Debugger implements Listener {

	public static void displayBug(Bug bug){
		if(!AntiShare.DEBUG_MODE){
			return;
		}
		AntiShare plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
		ASLog log = plugin.log;
		log.severe("[" + plugin.getDescription().getVersion() + "] " + "[Debugger] *** BUG REPORT ***");
		log.severe("[" + plugin.getDescription().getVersion() + "] " + "[Debugger] Class: " + bug.getInvolvedClass().getName());
		log.severe("[" + plugin.getDescription().getVersion() + "] " + "[Debugger] Sender: " + bug.getSenderInvolved());
		log.severe("[" + plugin.getDescription().getVersion() + "] " + "[Debugger] Exception: " + bug.getException().getMessage());
		log.severe("[" + plugin.getDescription().getVersion() + "] " + "[Debugger] Message: " + bug.getMessage());
		log.severe("[" + plugin.getDescription().getVersion() + "] " + "[Debugger] World: " + bug.getWorld());
	}

	private AntiShare plugin;
	private Vector<AlertTimer> alertTimers = new Vector<AlertTimer>();

	public Debugger(AntiShare plugin){
		this.plugin = plugin;
	}

	public void sendBug(Bug bug){
		Bukkit.getServer().getPluginManager().callEvent(new AntiShareBugEvent(bug));
		displayBug(bug);
	}

	public void alert(String message, CommandSender target, AlertType type){
		if(!AntiShare.DEBUG_MODE){
			return;
		}
		boolean foundtimer = false;
		for(AlertTimer timer : alertTimers){
			if(timer.getTarget().equals(target)){
				if(timer.getType().equals(type)){
					timer.sendMessage(message);
					foundtimer = true;
				}
			}
		}
		if(!foundtimer){
			AlertTimer timer = new AlertTimer(type, target);
			timer.sendMessage(message);
			alertTimers.add(timer);
		}
	}

	// Used by exp gained event
	public void alertOverrideDebug(String message, CommandSender target, AlertType type){
		boolean foundtimer = false;
		for(AlertTimer timer : alertTimers){
			if(timer.getTarget().equals(target)){
				if(timer.getType().equals(type)){
					timer.sendMessage(message);
					foundtimer = true;
				}
			}
		}
		if(!foundtimer){
			AlertTimer timer = new AlertTimer(type, target);
			timer.sendMessage(message);
			alertTimers.add(timer);
		}
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event){
		if(!AntiShare.DEBUG_MODE){
			return;
		}
		if(event.getMessage().equalsIgnoreCase("/astest")){
			if(AntiShare.DEBUG_MODE){
				//plugin.getRegionHandler().getWorldEditHandler().forceDisplayWorldEditInformation(event.getPlayer());
				//new ConfigurationConversation(plugin, event.getPlayer());
				// Lazy solution to "unused" members
				ASUtils.sendToPlayer(event.getPlayer(), plugin.getDescription().getFullName());
				event.setCancelled(true);
			}
		}
	}
}
