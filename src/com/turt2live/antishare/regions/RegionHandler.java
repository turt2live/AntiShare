package com.turt2live.antishare.regions;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.SQL.SQLManager;
import com.turt2live.antishare.debug.Bug;
import com.turt2live.antishare.debug.BugCheck;
import com.turt2live.antishare.debug.Debugger;
import com.turt2live.antishare.enums.RegionKeyType;
import com.turt2live.antishare.regions.hooks.HookManager;
import com.turt2live.antishare.storage.VirtualInventory;

public class RegionHandler {

	private AntiShare plugin;
	private boolean hasHook = false;
	private boolean hasWorldEdit = false;
	private HookManager hooks;
	private RegionManager manager;
	private HashMap<String, RegionPlayer> player_information = new HashMap<String, RegionPlayer>();
	private RegionScanner scanner;

	public RegionHandler(AntiShare plugin){
		this.plugin = plugin;
		if(plugin.getServer().getPluginManager().getPlugin("WorldEdit") != null){
			hasWorldEdit = true;
		}else{
			plugin.log.warning("[" + plugin.getDescription().getVersion() + "] " + "WorldEdit is not installed!");
			return; // Stop further potential issues
		}
		hooks = new HookManager(plugin);
		hasHook = hooks.hasHook();
		manager = new RegionManager(this);
		scanner = new RegionScanner(this, plugin);
		load();
		// Check player regions
		for(Player player : Bukkit.getOnlinePlayers()){
			if(isRegion(player.getLocation())){
				ASRegion region = getRegion(player.getLocation());
				region.alertEntry(player, this);
			}
		}
	}

	public RegionScanner getScanner(){
		return scanner;
	}

	public void newRegion(CommandSender sender, String gamemodeName, String name){
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
		if(hooks.getWorldEdit().getSelection((Player) sender) == null){
			ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You have no selection!");
			return;
		}
		if(hooks.regionExistsInSelection((Player) sender)){
			ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "There is a region where you have selected!");
			return;
		}
		if(manager.regionNameExists(name)){
			ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "That region name already exists!");
			return;
		}
		manager.newRegion((Player) sender, gamemode, name);
		ASUtils.sendToPlayer(sender, ChatColor.GREEN + "Region '" + name + "' added.");
	}

	public void removeRegion(Location location, Player sender){
		if(!hasHook){
			return;
		}
		if(isRegion(location)){
			manager.removeRegionAtLocation(location);
			if(sender != null){
				ASUtils.sendToPlayer(sender, ChatColor.GREEN + "Region removed.");
			}
		}else{
			if(sender != null){
				ASUtils.sendToPlayer(sender, ChatColor.RED + "You are not in a GameMode region.");
			}
		}
	}

	public void removeRegion(String name, CommandSender sender){
		if(!hasWorldEdit){
			ASUtils.sendToPlayer(sender, ChatColor.RED + "WorldEdit is not installed.");
			return;
		}
		if(!regionNameExists(name)){
			ASUtils.sendToPlayer(sender, ChatColor.RED + "Region '" + name + "' does not exist.");
			return;
		}
		manager.removeRegionByName(name);
		ASUtils.sendToPlayer(sender, ChatColor.GREEN + "Region removed.");
	}

	public ASRegion getRegion(Location location){
		if(!hasHook){
			return null;
		}
		return plugin.storage.getRegion(location);
	}

	public boolean isRegion(Location location){
		if(!hasHook){
			return false;
		}
		return plugin.storage.regionExists(getRegion(location));
	}

	public boolean regionNameExists(String name){
		if(!hasHook){
			return false;
		}
		return plugin.storage.getRegionByName(name) != null;
	}

	public ASRegion getRegionByName(String name){
		if(!hasHook){
			return null;
		}
		return plugin.storage.getRegionByName(name);
	}

	public ASRegion getRegionByID(String id){
		if(!hasHook){
			return null;
		}
		return plugin.storage.getRegionByID(id);
	}

	public void editRegion(ASRegion region, RegionKeyType key, String value, CommandSender sender){
		if(!hasWorldEdit){
			ASUtils.sendToPlayer(sender, ChatColor.RED + "WorldEdit is not installed.");
			return;
		}
		boolean changed = false;
		switch (key){
		case NAME:
			if(regionNameExists(value)){
				ASUtils.sendToPlayer(sender, ChatColor.RED + "Region name '" + value + "' already exists!");
			}else{
				region.setName(value);
				changed = true;
			}
			break;
		case ENTER_MESSAGE_SHOW:
			if(ASUtils.getValueOf(value) != null){
				region.setMessageOptions(ASUtils.getValueOf(value), region.isExitMessageActive());
				changed = true;
			}else{
				ASUtils.sendToPlayer(sender, ChatColor.RED + "Value '" + value + "' is unknown, did you mean 'true' or 'false'?");
			}
			break;
		case EXIT_MESSAGE_SHOW:
			if(ASUtils.getValueOf(value) != null){
				region.setMessageOptions(region.isEnterMessageActive(), ASUtils.getValueOf(value));
				changed = true;
			}else{
				ASUtils.sendToPlayer(sender, ChatColor.RED + "Value '" + value + "' is unknown, did you mean 'true' or 'false'?");
			}
			break;
		case INVENTORY:
			if(value.equalsIgnoreCase("none")){
				region.setInventory(null);
				changed = true;
			}else if(value.equalsIgnoreCase("set")){
				if(sender instanceof Player){
					region.setInventory(VirtualInventory.getInventoryFromPlayer((Player) sender));
					changed = true;
				}else{
					ASUtils.sendToPlayer(sender, ChatColor.RED + "You can't set an inventory from the console, only clear.");
				}
			}else{
				ASUtils.sendToPlayer(sender, ChatColor.RED + "Value '" + value + "' is unknown to me, did you mean 'none' or 'set'?");
			}
			break;
		case SELECTION_AREA:
			if(!hasHook){
				ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "WorldEdit is not installed. No region set.");
				break;
			}
			if(!(sender instanceof Player)){
				ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You are not a player, sorry!");
				break;
			}
			if(hooks.regionExistsInSelectionAndNot((Player) sender, region)){
				ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "There is a region where you have selected!");
				break;
			}
			Selection selection = hooks.getWorldEdit().getSelection((Player) sender);
			region.setRegion(selection);
			changed = true;
			break;
		case GAMEMODE:
			if(value.equalsIgnoreCase("creative") || value.equalsIgnoreCase("c") || value.equalsIgnoreCase("1")){
				region.setGameMode(GameMode.CREATIVE);
				changed = true;
			}else if(value.equalsIgnoreCase("survival") || value.equalsIgnoreCase("s") || value.equalsIgnoreCase("0")){
				region.setGameMode(GameMode.SURVIVAL);
				changed = true;
			}else{
				ASUtils.sendToPlayer(sender, ChatColor.RED + "I don't know what Game Mode '" + value + "' is!");
			}
			break;
		case ENTER_MESSAGE:
			region.setEnterMessage(value);
			changed = true;
			break;
		case EXIT_MESSAGE:
			region.setExitMessage(value);
			changed = true;
			break;
		}
		if(changed){
			ASUtils.sendToPlayer(sender, ChatColor.GREEN + "Region saved.");
		}
	}

	public void checkRegion(Player player, Location newLocation, Location fromLocation){
		if(!hasHook){
			return;
		}
		ASRegion region = plugin.getRegionHandler().getRegion(newLocation);
		RegionPlayer asPlayer = player_information.get(player.getName());
		if(asPlayer == null){
			asPlayer = new RegionPlayer(player.getName());
		}
		if(region != null){
			if(!player.getGameMode().equals(region.getGameModeSwitch())
					&& !plugin.getPermissions().has(player, "AntiShare.roam", player.getWorld())){
				asPlayer.setLastGameMode(player.getGameMode());
				player.setGameMode(region.getGameModeSwitch());
			}else if(asPlayer.getLastRegion() == null){
				asPlayer.setLastGameMode(player.getGameMode());
			}
			if(asPlayer.getLastRegion() != null){
				if(!asPlayer.getLastRegion().getUniqueID().equals(region.getUniqueID())){
					region.alertEntry(player);
				}
			}else{
				region.alertEntry(player);
			}
			asPlayer.setLastRegion(region);
		}else{ // Left region/is out of region
			if(asPlayer.getLastRegion() != null){
				asPlayer.getLastRegion().alertExit(player);
				if(!asPlayer.getLastGameMode().equals(player.getGameMode())
						&& !plugin.getPermissions().has(player, "AntiShare.roam", player.getWorld())){
					player.setGameMode(asPlayer.getLastGameMode());
					asPlayer.setLastGameMode(player.getGameMode());
				}
				asPlayer.setLastRegion(null);
			}else{
				asPlayer.setLastGameMode(player.getGameMode());
			}
		}
		if(player_information.containsKey(player.getName())){
			player_information.remove(player.getName());
		}
		player_information.put(player.getName(), asPlayer);
		BugCheck.verifyEqualRegion(asPlayer.getLastRegion(), region, "Region handler not saving regions", this.getClass());
		BugCheck.verifyEqual(asPlayer.getLastGameMode(), player.getGameMode(), "Region handler not saving gamemode", this.getClass());
	}

	public Vector<ASRegion> getRegionsNearby(Location location, int distance){
		if(!hasHook){
			return null;
		}
		return plugin.storage.getRegionsNearby(location, distance);
	}

	public void saveStatusToDisk(){
		if(!hasHook){
			return;
		}
		boolean flatfile = true;
		if(plugin.getConfig().getBoolean("SQL.use") && plugin.getSQLManager() != null){
			if(plugin.getSQLManager().isConnected()){
				flatfile = false;
				SQLManager sql = plugin.getSQLManager();
				sql.deleteQuery("DELETE FROM AntiShare_RegionInfo");
				for(String player : player_information.keySet()){
					RegionPlayer asPlayer = player_information.get(player);
					sql.insertQuery("INSERT INTO AntiShare_RegionInfo (player, region, gamemode) " +
							"VALUES ('" + player + "', '"
							+ (asPlayer.getLastRegion() != null ? asPlayer.getLastRegion().getUniqueID() : "none") + "', '"
							+ asPlayer.getLastGameMode().name() + "')");
				}
			}
		}
		if(flatfile){
			File saveFile = new File(plugin.getDataFolder(), "region_saves.yml");
			if(saveFile.exists()){
				saveFile.delete();
				try{
					saveFile.createNewFile();
				}catch(Exception e){
					Bug bug = new Bug(e, "Region save error", this.getClass(), null);
					Debugger.sendBug(bug);
				}
			}
			EnhancedConfiguration listing = new EnhancedConfiguration(saveFile, plugin);
			listing.load();
			for(String player : player_information.keySet()){
				RegionPlayer asPlayer = player_information.get(player);
				if(asPlayer == null){
					continue;
				}
				listing.set(player + ".gamemode", asPlayer.getLastGameMode().name());
				listing.set(player + ".region", (asPlayer.getLastRegion() != null) ? asPlayer.getLastRegion().getUniqueID() : "none");
				listing.save();
			}
		}
	}

	public void load(){
		if(!hasHook){
			return;
		}
		boolean flatfile = true;
		if(plugin.getConfig().getBoolean("SQL.use") && plugin.getSQLManager() != null){
			if(plugin.getSQLManager().isConnected()){
				SQLManager sql = plugin.getSQLManager();
				ResultSet results = sql.getQuery("SELECT * FROM AntiShare_RegionInfo");
				try{
					if(results != null){
						while (results.next()){
							String playerName = results.getString("player");
							GameMode gamemode = GameMode.valueOf(results.getString("gamemode"));
							ASRegion region = null;
							if(!results.getString("region").equalsIgnoreCase("none")){
								region = getRegionByID(results.getString("region"));
								BugCheck.verifyNotEqualRegion(region, null, "[TYPE 1] Region load failed to find a region", this.getClass());
							}
							RegionPlayer asPlayer = new RegionPlayer(playerName);
							asPlayer.setLastGameMode(gamemode);
							asPlayer.setLastRegion(region);
							player_information.put(playerName, asPlayer);
						}
					}
					flatfile = false;
				}catch(SQLException e){
					Bug bug = new Bug(e, "cannot handle region information", this.getClass(), null);
					Debugger.sendBug(bug);
					plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "Cannot handle region information: " + e.getMessage());
				}
			}
		}
		if(flatfile){
			File saveFile = new File(plugin.getDataFolder(), "region_saves.yml");
			if(!saveFile.exists()){
				return;
			}
			EnhancedConfiguration listing = new EnhancedConfiguration(saveFile, plugin);
			listing.load();
			Set<String> section = listing.getKeys(false);
			for(String path : section){
				String playerName = path;
				GameMode gamemode = GameMode.valueOf(listing.getString(path + ".gamemode"));
				ASRegion region = null;
				if(!listing.getString(path + ".region").equalsIgnoreCase("none")){
					region = getRegionByID(listing.getString(path + ".region"));
					BugCheck.verifyNotEqualRegion(region, null, "[TYPE 2] Region load failed to find a region", this.getClass());
				}
				RegionPlayer asPlayer = new RegionPlayer(playerName);
				asPlayer.setLastGameMode(gamemode);
				asPlayer.setLastRegion(region);
				player_information.put(playerName, asPlayer);
			}
		}
	}

	public AntiShare getPlugin(){
		return plugin;
	}

	public HookManager getHooks(){
		return hooks;
	}
}
