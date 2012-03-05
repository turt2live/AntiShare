package com.turt2live.antishare;

import org.bukkit.Bukkit;

public class TimedSave {

	private int id = -1;

	public TimedSave(final AntiShare plugin, int ticks){
		id = plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable(){
			@Override
			public void run(){
				plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "as rl");
			}
		}, ticks, ticks);
		if(id == -1){
			AntiShare.log.severe("[AntiShare] Save thread cannot be created.");
		}
	}

	public void cancel(){
		if(id != -1){
			Bukkit.getScheduler().cancelTask(id);
		}
	}
}
