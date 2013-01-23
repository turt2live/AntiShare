/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.util.generic;

import org.bukkit.GameMode;

import com.turt2live.materials.ServerHas;

/**
 * An enum to represent a Game Mode
 * 
 * @author turt2live
 */
public enum ASGameMode{

	CREATIVE(1),
	SURVIVAL(0),
	ADVENTURE(2),
	ALL(-3),
	BOTH(-1),
	NONE(-2);

	private int numeric;

	private ASGameMode(int numeric){
		this.numeric = numeric;
	}

	/**
	 * Determines if a Bukkit Game Mode matches this AntiShare Game Mode
	 * 
	 * @param gamemode the gamemode
	 * @return true if matches
	 */
	public boolean matches(GameMode gamemode){
		if(numeric == -1 && (gamemode == GameMode.CREATIVE || gamemode == GameMode.SURVIVAL)){
			return true;
		}else if(numeric == 1 && gamemode == GameMode.CREATIVE){
			return true;
		}else if(numeric == 0 && gamemode == GameMode.SURVIVAL){
			return true;
		}else if(numeric == -3){
			return true;
		}else if(ServerHas.adventureMode() && numeric == 2 && gamemode == GameMode.ADVENTURE){
			return true;
		}
		return false; // Covers 'NONE'
	}

	/**
	 * Matches a String to an ASGameMode
	 * 
	 * @param string the string
	 * @return the ASGameMode, or null if not found
	 */
	public static ASGameMode match(String string){
		string = string.trim();
		if(string.equalsIgnoreCase("creative")){
			return ASGameMode.CREATIVE;
		}else if(string.equalsIgnoreCase("survival")){
			return ASGameMode.SURVIVAL;
		}else if(string.equalsIgnoreCase("adventure")){
			return ASGameMode.ADVENTURE;
		}else if(string.equalsIgnoreCase("all")){
			return ASGameMode.ALL;
		}else if(string.equalsIgnoreCase("both")){
			return ASGameMode.BOTH;
		}else if(string.equalsIgnoreCase("none")){
			return ASGameMode.NONE;
		}
		return null;
	}
}
