package com.turt2live.antishare.worldedit;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.debug.AlertType;

public class ASRegionHandler {

	private AntiShare plugin;
	private boolean hasWorldEdit = false;
	private ASWorldEdit worldedit;
	private HashMap<String, ASRegionPlayer> player_information = new HashMap<String, ASRegionPlayer>();

	public ASRegionHandler(AntiShare plugin){
		this.plugin = plugin;
		if(plugin.getServer().getPluginManager().getPlugin("WorldEdit") != null){
			hasWorldEdit = true;
			worldedit = new ASWorldEdit(plugin);
		}else{
			AntiShare.log.warning("[" + plugin.getDescription().getFullName() + "] WorldEdit is not installed!");
		}
		load();
	}

	public void newRegion(CommandSender sender, String gamemodeName){
		if(!hasWorldEdit){
			ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "WorldEdit is not installed. No region set.");
			return;
		}
		GameMode gamemode;
		if(gamemodeName.equalsIgnoreCase("creative") || gamemodeName.equalsIgnoreCase("c") || gamemodeName.equalsIgnoreCase("1")){
			gamemode = GameMode.CREATIVE;
		}else if(gamemodeName.equalsIgnoreCase("survival") || gamemodeName.equalsIgnoreCase("s") || gamemodeName.equalsIgnoreCase("0")){
			gamemode = GameMode.SURVIVAL;
		}else{
			ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "I don't know what Game Mode '" + gamemodeName + "' is!");
			return;
		}
		if(!(sender instanceof Player)){
			ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You are not a player, sorry!");
			return;
		}
		if(worldedit.regionExistsInSelection((Player) sender)){
			ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "There is a region where you have selected!");
			return;
		}
		worldedit.newRegion((Player) sender, gamemode);
		ASUtils.sendToPlayer(sender, ChatColor.GREEN + "Region added.");
	}

	public void removeRegion(Location location, Player sender){
		if(isRegion(location)){
			worldedit.removeRegionAtLocation(location);
			if(sender != null){
				ASUtils.sendToPlayer(sender, ChatColor.GREEN + "Region removed.");
			}
		}else{
			if(sender != null){
				ASUtils.sendToPlayer(sender, ChatColor.RED + "You are not in a GameMode region.");
			}
		}
	}

	public ASRegion getRegion(Location location){
		return plugin.storage.getRegion(location);
	}

	public boolean isRegion(Location location){
		return plugin.storage.getRegion(location) != null;
	}

	public ASRegion getRegionByID(String id){
		return plugin.storage.getRegionByID(id);
	}

	public void checkRegion(Player player, Location newLocation){
		ASRegion region = plugin.getRegionHandler().getRegion(newLocation);
		if(AntiShare.DEBUG_MODE){
			if(region != null){
				plugin.getDebugger().alert(ChatColor.GOLD + "Welcome to region '" + region.getUniqueID() + "'", player, AlertType.REGION_ENTER);
			}else{
				plugin.getDebugger().alert(ChatColor.GOLD + "You left a region!", player, AlertType.REGION_LEAVE);
			}
		}
		if(player.hasPermission("AntiShare.roam")){
			return;
		}
		ASRegionPlayer asPlayer = player_information.get(player.getName());
		if(asPlayer == null){
			asPlayer = new ASRegionPlayer(player.getName());
		}
		if(region != null){
			if(!player.getGameMode().equals(region.getGameModeSwitch())){
				asPlayer.setLastGameMode(player.getGameMode());
				player.setGameMode(region.getGameModeSwitch());
			}
			if(asPlayer.getLastRegion() != null){
				if(!asPlayer.getLastRegion().equals(region)){
					region.alertEntry(player);
				}
			}else{
				region.alertEntry(player);
			}
			asPlayer.setLastRegion(region);
		}else{ // Left region/is out of region
			if(asPlayer.getLastRegion() != null){
				if(!asPlayer.getLastGameMode().equals(player.getGameMode())){
					player.setGameMode(asPlayer.getLastGameMode());
					asPlayer.getLastRegion().alertExit(player);
					asPlayer.setLastRegion(null);
				}
			}
		}
		if(player_information.containsKey(player.getName())){
			player_information.remove(player.getName());
		}
		player_information.put(player.getName(), asPlayer);
	}

	public void saveStatusToDisk(){
		File saveFile = new File(plugin.getDataFolder(), "region_saves.yml");
		if(saveFile.exists()){
			saveFile.delete();
			try{
				saveFile.createNewFile();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		EnhancedConfiguration listing = new EnhancedConfiguration(saveFile, plugin);
		listing.load();
		for(String player : player_information.keySet()){
			ASRegionPlayer asPlayer = player_information.get(player);
			listing.set(player + ".gamemode", asPlayer.getLastGameMode().name());
			listing.set(player + ".region", (asPlayer.getLastRegion() != null) ? asPlayer.getLastRegion().getUniqueID() : "none");
			listing.save();
		}
	}

	public void load(){
		File saveFile = new File(plugin.getDataFolder(), "region_saves.yml");
		if(!saveFile.exists()){
			return;
		}
		EnhancedConfiguration listing = new EnhancedConfiguration(saveFile, plugin);
		listing.load();
		Set<String> section = listing.getConfigurationSection("").getKeys(false);
		for(String path : section){
			String playerName = path;
			GameMode gamemode = GameMode.valueOf(listing.getString(path + ".gamemode"));
			ASRegion region = null;
			if(!listing.getString(path + ".region").equalsIgnoreCase("none")){
				region = getRegionByID(listing.getString(path + ".region"));
			}
			ASRegionPlayer asPlayer = new ASRegionPlayer(playerName);
			asPlayer.setLastGameMode(gamemode);
			asPlayer.setLastRegion(region);
			player_information.put(playerName, asPlayer);
		}
	}

	public AntiShare getPlugin(){
		return plugin;
	}

	public ASWorldEdit getWorldEditHandler(){
		return worldedit;
	}

}
