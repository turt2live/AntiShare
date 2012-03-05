package com.turt2live.antishare.worldedit;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.turt2live.antishare.ASNotification;
import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.enums.NotificationType;

public class ASRegion {

	private AntiShare plugin;
	private World world;
	private String setBy;
	private GameMode gamemode;
	private Selection region;
	private String id;
	private String name;
	private boolean showEnterMessage = true;
	private boolean showExitMessage = true;

	public ASRegion(Selection region, String setBy, GameMode gamemode){
		this.region = region;
		this.setBy = setBy;
		this.gamemode = gamemode;
		this.world = region.getWorld();
		id = String.valueOf(System.currentTimeMillis());
		plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
		name = id;
	}

	public void setUniqueID(String ID){
		id = ID;
	}

	public void setGameMode(GameMode gamemode){
		this.gamemode = gamemode;
	}

	public void setName(String name){
		this.name = name;
	}

	public void setMessageOptions(boolean showEnter, boolean showExit){
		showEnterMessage = showEnter;
		showExitMessage = showExit;
	}

	public void saveToDisk(){
		File saveFolder = new File(plugin.getDataFolder(), "regions");
		saveFolder.mkdirs();
		File regionFile = new File(saveFolder, id + ".yml");
		if(!regionFile.exists()){
			try{
				regionFile.createNewFile();
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			regionFile.delete();
			try{
				regionFile.createNewFile();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		EnhancedConfiguration regionYAML = new EnhancedConfiguration(regionFile, plugin);
		regionYAML.load();
		regionYAML.set("worldName", world.getName());
		regionYAML.set("mi-x", region.getMinimumPoint().getX());
		regionYAML.set("mi-y", region.getMinimumPoint().getY());
		regionYAML.set("mi-z", region.getMinimumPoint().getZ());
		regionYAML.set("ma-x", region.getMaximumPoint().getX());
		regionYAML.set("ma-y", region.getMaximumPoint().getY());
		regionYAML.set("ma-z", region.getMaximumPoint().getZ());
		regionYAML.set("set-by", setBy);
		regionYAML.set("gamemode", gamemode.name());
		regionYAML.set("name", name);
		regionYAML.set("showEnter", showEnterMessage);
		regionYAML.set("showExit", showExitMessage);
		regionYAML.save();
	}

	public boolean has(Location location){
		return region.contains(location);
	}

	public String getName(){
		return name;
	}

	public boolean isEnterMessageActive(){
		return showEnterMessage;
	}

	public boolean isExitMessageActive(){
		return showExitMessage;
	}

	public World getWorld(){
		return world;
	}

	public String getWhoSet(){
		return setBy;
	}

	public GameMode getGameModeSwitch(){
		return gamemode;
	}

	public Selection getSelection(){
		return region;
	}

	public String getUniqueID(){
		return id;
	}

	public AntiShare getPlugin(){
		return plugin;
	}

	// TODO: Allow the API to create regions WITHOUT players (and etc)
	// TODO: Check gamemode vs region on block place, break, on gm change
	public void alertEntry(Player player){
		if(showEnterMessage){
			ASUtils.sendToPlayer(player, ChatColor.GOLD + "You entered '" + name + "'");
			ASNotification.sendNotification(NotificationType.REGION_ENTER, player, name);
		}
	}

	public void alertExit(Player player){
		if(showExitMessage){
			ASUtils.sendToPlayer(player, ChatColor.GOLD + "You left '" + name + "'");
			ASNotification.sendNotification(NotificationType.REGION_EXIT, player, name);
		}
	}
}
