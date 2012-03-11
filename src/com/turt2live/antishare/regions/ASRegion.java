package com.turt2live.antishare.regions;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.turt2live.antishare.ASNotification;
import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.SQL.SQLManager;
import com.turt2live.antishare.enums.NotificationType;
import com.turt2live.antishare.storage.ASVirtualInventory;

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
	private HashMap<Integer, ItemStack> inventory;

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

	public void setRegion(Selection selection){
		region = selection;
	}

	public void setInventory(HashMap<Integer, ItemStack> inventory){
		this.inventory = inventory;
	}

	public void saveToDisk(){
		boolean flatfile = true;
		if(plugin.getConfig().getBoolean("SQL.use") && plugin.getSQLManager() != null){
			if(plugin.getSQLManager().isConnected()){
				flatfile = false;
				SQLManager sql = plugin.getSQLManager();
				double mix = this.region.getMinimumPoint().getX();
				double miy = this.region.getMinimumPoint().getY();
				double miz = this.region.getMinimumPoint().getZ();
				double max = this.region.getMaximumPoint().getX();
				double may = this.region.getMaximumPoint().getY();
				double maz = this.region.getMaximumPoint().getZ();
				sql.insertQuery("INSERT INTO AntiShare_Regions (regionName, mix, miy, miz, max, may, maz, creator, gamemode, showEnter, showExit, world, uniqueID) " +
						"VALUES ('" + name + "', '"
						+ mix + "', '" + miy + "', '" + miz + "', '"
						+ max + "', '" + may + "', '" + maz + "', '"
						+ setBy + "', '" + gamemode.name() + "', '"
						+ (showEnterMessage ? 1 : 0) + "', '" + (showExitMessage ? 1 : 0) + "', '" + world.getName() + "', '" + id + "')");

			}
		}
		if(flatfile){
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
			if(inventory != null){
				File saveFile = new File(plugin.getDataFolder() + "/region_inventories", id + ".yml");
				if(inventory.size() > 0){
					ASVirtualInventory.saveInventoryToDisk(saveFile, inventory, plugin);
				}
			}else{
				File saveFile = new File(plugin.getDataFolder() + "/region_inventories", id + ".yml");
				if(saveFile.exists()){
					saveFile.delete();
				}
			}
		}
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

	public void alertEntry(Player player){
		if(showEnterMessage){
			ASUtils.sendToPlayer(player, ChatColor.GOLD + "You entered '" + name + "'");
			ASNotification.sendNotification(NotificationType.REGION_ENTER, player, name);
			if(!plugin.getPermissions().has(player, "AntiShare.roam", world) && this.inventory != null){
				ASVirtualInventory inventory = plugin.storage.getInventoryManager(player, world);
				inventory.setTemporaryInventory(this.inventory);
				inventory.loadToTemporary();
			}
		}
	}

	public void alertExit(Player player){
		if(showExitMessage){
			ASUtils.sendToPlayer(player, ChatColor.GOLD + "You left '" + name + "'");
			ASNotification.sendNotification(NotificationType.REGION_EXIT, player, name);
			if(!plugin.getPermissions().has(player, "AntiShare.roam", world)){
				ASVirtualInventory inventory = plugin.storage.getInventoryManager(player, world);
				if(inventory.isTemp()){
					inventory.unloadFromTemporary();
				}
			}
		}
	}
}
