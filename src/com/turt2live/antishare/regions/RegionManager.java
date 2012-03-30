package com.turt2live.antishare.regions;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;
import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;

public class RegionManager {

	private RegionHandler handler;
	private AntiShare plugin;

	public RegionManager(RegionHandler handler){
		this.handler = handler;
		plugin = handler.getPlugin();
	}

	public void newRegion(Player player, GameMode gamemode, String name){
		Selection userSelection = handler.getHooks().getWorldEdit().getSelection(player);
		if(userSelection == null){
			ASUtils.sendToPlayer(player, ChatColor.RED + "You have no WorldEdit selection!");
			return;
		}
		ASRegion region = new ASRegion(userSelection, player.getName(), gamemode);
		region.setName(name);
		plugin.storage.saveRegion(region);
	}

	public ASRegion newRegion(Selection selection, String player, GameMode gamemode, String name){
		ASRegion region = new ASRegion(selection, player, gamemode);
		region.setName(name);
		plugin.storage.saveRegion(region);
		return region;
	}

	public void removeRegionAtLocation(Location location){
		plugin.storage.removeRegion(plugin.storage.getRegion(location));
	}

	public void removeRegionByName(String name){
		plugin.storage.removeRegion(plugin.storage.getRegionByName(name));
	}

	public boolean regionNameExists(String name){
		return handler.regionNameExists(name);
	}

}
