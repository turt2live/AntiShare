package com.turt2live;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;

public class ASMultiWorld implements Listener {

	public static void detectWorld(AntiShare plugin, World world){

	}

	public static void detectWorlds(AntiShare plugin){

	}

	public static boolean worldSwap(AntiShare plugin, Player player, Location from, Location to){
		return false; //Cancel or not
	}

	private AntiShare plugin;

	public ASMultiWorld(AntiShare plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void onWorldInit(WorldInitEvent event){
		ASMultiWorld.detectWorld(plugin, event.getWorld());
	}

	@EventHandler
	public void onWorldLoad(WorldLoadEvent event){
		ASMultiWorld.detectWorld(plugin, event.getWorld());
	}

	@EventHandler
	public void onWorldSave(WorldSaveEvent event){
		ASMultiWorld.detectWorld(plugin, event.getWorld());
	}

	@EventHandler
	public void onWorldUnload(WorldSaveEvent event){
		ASMultiWorld.detectWorld(plugin, event.getWorld());
	}
}
