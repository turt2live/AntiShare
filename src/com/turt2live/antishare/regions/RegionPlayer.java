package com.turt2live.antishare.regions;

import org.bukkit.GameMode;

public class RegionPlayer {

	private String player;
	private ASRegion lastRegion;
	private GameMode lastGameMode;

	public RegionPlayer(String playername){
		this.player = playername;
	}

	public String getPlayerName(){
		return player;
	}

	public ASRegion getLastRegion(){
		return lastRegion;
	}

	public GameMode getLastGameMode(){
		return lastGameMode;
	}

	public void setLastRegion(ASRegion region){
		lastRegion = region;
	}

	public void setLastGameMode(GameMode gamemode){
		lastGameMode = gamemode;
	}

}
