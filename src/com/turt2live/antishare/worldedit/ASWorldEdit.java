package com.turt2live.antishare.worldedit;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.WorldEditAPI;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.Region;
import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;

public class ASWorldEdit {

	private AntiShare plugin;
	private WorldEditPlugin wePlugin;
	private WorldEditAPI weAPI;

	public ASWorldEdit(AntiShare plugin){
		this.plugin = plugin;
		wePlugin = (WorldEditPlugin) plugin.getServer().getPluginManager().getPlugin("WorldEdit");
	}

	public void newRegion(Player player, GameMode gamemode){
		weAPI = new WorldEditAPI(wePlugin);
		LocalSession session = weAPI.getSession(player);
		if(session.hasExpired()){
			ASUtils.sendToPlayer(player, "Your WorldEdit session expired.");
			return;
		}
		if(session.getSelectionWorld() == null){
			ASUtils.sendToPlayer(player, "You have no WorldEdit selection!");
			return;
		}
		@SuppressWarnings ("unused")
		Region userSelection;
		try{
			if(session.getSelection(session.getSelectionWorld()) == null){
				ASUtils.sendToPlayer(player, "You have no WorldEdit selection!");
				return;
			}
			userSelection = session.getSelection(session.getSelectionWorld());
		}catch(IncompleteRegionException e){
			e.printStackTrace();
			ASUtils.sendToPlayer(player, "Something went wrong in the WorldEdit selection check.");
			return;
		}

	}

	public AntiShare getPlugin(){
		return plugin;
	}

	public WorldEditPlugin getWorldEditPlugin(){
		return wePlugin;
	}

	public void forceDisplayWorldEditInformation(Player player){
		Selection userSelection = wePlugin.getSelection(player);
		if(userSelection == null){
			ASUtils.sendToPlayer(player, ChatColor.RED + "You have no WorldEdit selection!");
			return;
		}
		Location minimum = userSelection.getMinimumPoint();
		Location maximum = userSelection.getMaximumPoint();
		if(minimum == null || maximum == null){
			ASUtils.sendToPlayer(player, ChatColor.RED + "Please complete your WorldEdit selection!");
			return;
		}
		ASUtils.sendToPlayer(player, minimum.toString());
		ASUtils.sendToPlayer(player, maximum.toString());
		ASUtils.sendToPlayer(player, "Area: " + userSelection.getArea());
		ASUtils.sendToPlayer(player, "Height: " + userSelection.getHeight());
		ASUtils.sendToPlayer(player, "Width: " + userSelection.getWidth());
		ASUtils.sendToPlayer(player, "Length: " + userSelection.getLength());
		ASUtils.sendToPlayer(player, "World: " + userSelection.getWorld());
		ASUtils.sendToPlayer(player, "Hash: " + userSelection.hashCode());
		@SuppressWarnings ("unused")
		Selection selection = new CuboidSelection(userSelection.getWorld(), maximum, maximum);
	}

	public static void clean(AntiShare plugin){
		// TODO
	}
}
