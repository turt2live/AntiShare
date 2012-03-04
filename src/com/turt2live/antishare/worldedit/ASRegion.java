package com.turt2live.antishare.worldedit;

import java.io.File;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.turt2live.antishare.AntiShare;

public class ASRegion {

	private AntiShare plugin;
	private World world;
	private String setBy;
	private GameMode gamemode;
	private Selection region;
	private String id;

	public ASRegion(Selection region, String setBy, GameMode gamemode){
		this.region = region;
		this.setBy = setBy;
		this.gamemode = gamemode;
		this.world = region.getWorld();
		id = String.valueOf(System.currentTimeMillis());
	}

	public void setUniqueID(String ID){
		id = ID;
	}

	public void setGameMode(GameMode gamemode){
		this.gamemode = gamemode;
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
		regionYAML.save();
	}

	public boolean has(Location location){
		return region.contains(location);
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

	public AntiShare getPlugin(){
		return plugin;
	}

}
