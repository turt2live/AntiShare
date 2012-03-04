package com.turt2live.antishare.debug;

import java.util.Vector;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.turt2live.antishare.AntiShare;

/*
 * This is used mostly while developing. There may be reminaing code here
 * but that code will be disabled if AntiShare.DEBUG_MODE is false
 */
public class Debugger implements Listener {

	public static void displayBug(Bug bug){
		if(!AntiShare.DEBUG_MODE){
			return;
		}
		Logger log = Logger.getLogger("Minecraft");
		log.severe("[AntiShare Debugger] *** BUG REPORT ***");
		log.severe("[AntiShare Debugger] Class: " + bug.getInvolvedClass().getName());
		log.severe("[AntiShare Debugger] Sender: " + bug.getSenderInvolved());
		log.severe("[AntiShare Debugger] Exception: " + bug.getException().getMessage());
		log.severe("[AntiShare Debugger] Message: " + bug.getMessage());
		log.severe("[AntiShare Debugger] World: " + bug.getWorld());
	}

	private AntiShare plugin;
	private Vector<AlertTimer> alertTimers = new Vector<AlertTimer>();

	public Debugger(AntiShare plugin){
		this.plugin = plugin;
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

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event){
		if(!AntiShare.DEBUG_MODE){
			return;
		}
		if(event.getMessage().equalsIgnoreCase("/astest")){
			if(AntiShare.DEBUG_MODE){
				plugin.getRegionHandler().getWorldEditHandler().forceDisplayWorldEditInformation(event.getPlayer());
				event.setCancelled(true);
			}
		}
	}
}
