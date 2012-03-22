package com.turt2live.antishare.debug;

import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.enums.AlertType;
import com.turt2live.antishare.event.AntiShareBugEvent;
import com.turt2live.antishare.log.ASLog;

/*
 * This is used mostly while developing. There may be remaining code here
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

	private Vector<AlertTimer> alertTimers = new Vector<AlertTimer>();

	public Debugger(){} // Not Required

	public static void sendBug(Bug bug){
		AntiShareBugEvent bugEvent = new AntiShareBugEvent(bug);
		Bukkit.getServer().getPluginManager().callEvent(bugEvent);
		displayBug(bug);
		AntiShare plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
		plugin.log.warning("An error has occured.");
		if(bugEvent.getPrintTrace()){
			bugEvent.getBug().getException().printStackTrace();
		}else{
			plugin.log.warning("A plugin has chosen not to display the stack trace to you. (Do you have the debugger?)");
		}
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
}
