package com.turt2live.antishare.inventory;

import org.bukkit.GameMode;
import org.bukkit.World;

public class LinkedInventory {

	private String[] worlds;
	private GameMode gamemode;

	public LinkedInventory(GameMode affectedGameMode, String... worlds){
		this.gamemode = affectedGameMode;
		this.worlds = worlds;
	}

	public boolean isWorldAffected(World world){
		for(String w : worlds){
			if(world.getName().equalsIgnoreCase(w)){
				return true;
			}
		}
		return false;
	}

	public boolean isGameModeAffected(GameMode gamemode){
		return gamemode == this.gamemode;
	}

	public String[] getAffectedWorlds(){
		return worlds;
	}

}
