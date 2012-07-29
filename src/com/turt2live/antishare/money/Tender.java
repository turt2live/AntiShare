/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.money;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.api.ASGameMode;

/**
 * A class for money (Reward or Fine)
 * 
 * @author turt2live
 */
public abstract class Tender {

	/**
	 * An enum to represent tender type
	 * 
	 * @author turt2live
	 * 
	 */
	public static enum TenderType{
		BLOCK_BREAK("actions.block-break", "Block Break"),
		BLOCK_PLACE("actions.block-place", "Block Place"),
		ITEM_DROP("actions.item-drop", "Item Drop"),
		ITEM_PICKUP("actions.item-pickup", "Item Pickup"),
		DEATH("actions.player-death", "Player Death"),
		RIGHT_CLICK("actions.right-click", "Right Click"),
		USE("actions.use", "Use"),
		COMMAND("actions.command", "Command"),
		HIT_PLAYER("actions.player-hit-player", "Player Hit Player"),
		HIT_MOB("actions.player-hit-mob", "Player Hit Mob"),
		CREATIVE_BLOCK("actions.creative-block-break", "Creative Block Break"),
		SURVIVAL_BLOCK("actions.survival-block-break", "Survival Block Break");

		private String key, name;

		private TenderType(String key, String name){
			this.key = key;
			this.name = name;
		}

		/**
		 * Gets the configuration key from root in the fines.yml
		 * 
		 * @return the configuration path
		 */
		public String getConfigurationKey(){
			return key;
		}

		/**
		 * Gets the name of the action. Used by trackers
		 * 
		 * @return the name
		 */
		public String getName(){
			return name;
		}
	}

	private double amount;
	private TenderType type;
	private boolean enabled;
	private ASGameMode affect;
	protected AntiShare plugin = AntiShare.getInstance();

	/**
	 * Creates a new Tender
	 * 
	 * @param type the type
	 * @param amount the amount
	 * @param enabled true to enable
	 * @param affect the Game Mode(s) to affect
	 */
	public Tender(TenderType type, double amount, boolean enabled, ASGameMode affect){
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
	public TenderType getType(){
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
