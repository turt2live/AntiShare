package com.turt2live.antishare.listener;

import org.bukkit.event.Listener;

import com.turt2live.antishare.AntiShare;

public class ASListener implements Listener {

	private AntiShare plugin;

	public ASListener(AntiShare p){
		plugin = p;
		p.getServer().getPluginManager().registerEvents(new PlayerListener(plugin), plugin);
		p.getServer().getPluginManager().registerEvents(new EntityListener(plugin), plugin);
		p.getServer().getPluginManager().registerEvents(new BlockListener(plugin), plugin);
		p.getServer().getPluginManager().registerEvents(new HazardListener(plugin), plugin);
	}
}
