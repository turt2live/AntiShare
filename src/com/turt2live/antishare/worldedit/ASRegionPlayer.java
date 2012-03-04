package com.turt2live.antishare.worldedit;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class ASRegionPlayer {

	private Player player;
	private ASRegion lastRegion;
	private GameMode lastGameMode;

	public ASRegionPlayer(Player player){
		this.player = player;
	}

	public Player getPlayer(){
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
