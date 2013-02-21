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
package com.turt2live.antishare.money;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.util.ASGameMode;
import com.turt2live.antishare.util.Action;

/**
 * A class for money (Reward or Fine)
 * 
 * @author turt2live
 */
//TODO: Schedule for rewrite
public abstract class Tender {

	private double amount;
	private Action type;
	private boolean enabled;
	private ASGameMode affect;
	protected AntiShare plugin = AntiShare.p;

	/**
	 * Creates a new Tender
	 * 
	 * @param type the type
	 * @param amount the amount
	 * @param enabled true to enable
	 * @param affect the Game Mode(s) to affect
	 */
	public Tender(Action type, double amount, boolean enabled, ASGameMode affect){
		this.type = type;
		this.amount = amount;
		this.enabled = enabled;
		this.affect = affect;
	}

	/**
	 * Determines if this tender is enabled
	 * 
	 * @return true if enabled
	 */
	public boolean isEnabled(){
		return enabled;
	}

	/**
	 * Gets the amount of this tender
	 * 
	 * @return the amount
	 */
	public double getAmount(){
		if(!enabled){
			return 0;
		}
		return amount;
	}

	/**
	 * Gets the type of this tender
	 * 
	 * @return the type
	 */
	public Action getType(){
		return type;
	}

	/**
	 * Gets the affect Game Mode(s)
	 * 
	 * @return the Game Mode(s)
	 */
	public ASGameMode getAffectedGameMode(){
		return affect;
	}

	/**
	 * Determines if this tender should affect a Game Mode
	 * 
	 * @param gamemode the Game Mode
	 * @return true if this tender should affect this Game Mode
	 */
	public boolean affect(GameMode gamemode){
		return affect.matches(gamemode);
	}

	/**
	 * Applies the tender to the player
	 * 
	 * @param player
	 */
	public abstract void apply(Player player);
}
