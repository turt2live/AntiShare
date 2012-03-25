package com.turt2live.antishare.regions.hooks;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.garbagemule.MobArena.MobArenaHandler;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.regions.Selection;

public class MobArenaHook implements Hook {

	private boolean hasMobArena = false;
	private Plugin mobarena;

	public MobArenaHook(AntiShare plugin){
		hasMobArena = (mobarena = plugin.getServer().getPluginManager().getPlugin("MobArena")) != null;
	}

	public Plugin getMobArenaPlugin(){
		return mobarena;
	}

	@Override
	public boolean inRegion(Player player){
		if(!hasMobArena){
			return false;
		}
		MobArenaHandler handle = new MobArenaHandler();
		return handle.getArenaWithPlayer(player) != null;
	}

	@Override
	public boolean hasRegion(Selection location){
		if(!hasMobArena){
			return false;
		}
		// Can't really do anything here due to lack of an API,
		// although the ASRegion alertEntry() has a
		// region check in it to ensure that if the player is 
		// in a MobArena they will not be tortured by bugs.
		// This fixes the issue first discovered where
		// if a player enters an ovrlapping GM Region with a
		// MA then the GM Region would override (and ignore)
		// the MA region without further thought. Now AS gives
		// priority to MA before playing with the player.

		// MobArenaHandler handle = new MobArenaHandler();
		// MobArena p = (MobArena) mobarena;
		return false;
	}

	@Override
	public boolean exists(){
		return hasMobArena;
	}

	@Override
	public String getName(){
		return "MobArena Hook";
	}

}
