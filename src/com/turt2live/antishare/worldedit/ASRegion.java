package com.turt2live.antishare.worldedit;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.regions.Region;
import com.turt2live.antishare.AntiShare;

public class ASRegion {

	private AntiShare plugin;
	private LocalWorld world;
	private Player setBy;
	private GameMode gamemode;
	private Region region;

	public ASRegion(Region region, Player setBy, GameMode gamemode){
		this.region = region;
		this.setBy = setBy;
		this.gamemode = gamemode;
		this.world = region.getWorld();
	}

	public void setGameMode(GameMode gamemode){
		this.gamemode = gamemode;
	}

	public void saveToDisk(){
		// TODO
	}

	public World getWorld(){
		return plugin.getServer().getWorld(world.getName());
	}

	public Player getWhoSet(){
		return setBy;
	}

	public GameMode getGameModeSwitch(){
		return gamemode;
	}

	public Region getRegion(){
		return region;
	}

	public AntiShare getPlugin(){
		return plugin;
	}

}
