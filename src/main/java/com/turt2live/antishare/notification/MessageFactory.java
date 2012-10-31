/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.notification;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.api.MVWorlds;
import com.turt2live.antishare.money.Tender.TenderType;

/**
 * Creates messages to send to the client
 * 
 * @author turt2live
 */
public class MessageFactory {

	public static final String BLOCK = "\\{BLOCK\\}";
	public static final String PLAYER = "\\{PLAYER\\}";
	public static final String DISPLAY_NAME = "\\{DNAME\\}";
	public static final String WORLD = "\\{WORLD\\}";
	public static final String MVWORLD = "\\{MVWORLD\\}";
	public static final String FINE = "\\{FINE\\}";
	public static final String REWARD = "\\{REWARD\\}";
	public static final String CLICKED = "\\{CLICKED\\}";
	public static final String AMOUNT = "\\{AMOUNT\\}";
	public static final String COMMAND = "\\{COMMAND\\}";
	public static final String HIT_PLAYER = "\\{HIT PLAYER\\}";
	public static final String HIT_MOB = "\\{HIT MOB\\}";
	public static final String DEFAULT_MESSAGE = "no message";

	private final AntiShare plugin = AntiShare.getInstance();
	private String message;

	/**
	 * Creates a new Message Factory with a base message
	 * 
	 * @param message the message
	 */
	public MessageFactory(String message){
		this.message = message;
	}

	/**
	 * Creates a new Message Factory with the message as MessageFactory.DEFAULT_MESSAGE
	 */
	public MessageFactory(){
		this.message = DEFAULT_MESSAGE;
	}

	/**
	 * Sets the message, overwriting any previous changes
	 * 
	 * @param message the new message
	 * @return the active message factory
	 */
	public MessageFactory setMessage(String message){
		this.message = message;
		return this;
	}

	/**
	 * Replace a value
	 * 
	 * @param key the key
	 * @param value the value
	 * @return the active message factory
	 */
	public MessageFactory replace(String key, String value){
		message = message.replace(key, value);
		return this;
	}

	/**
	 * Replace all of a value
	 * 
	 * @param key the key
	 * @param value the value
	 * @return the active message factory
	 */
	public MessageFactory replaceAll(String key, String value){
		message = message.replaceAll(key, value);
		return this;
	}

	/**
	 * Inserts a block into the message where needed
	 * 
	 * @param block the block
	 * @return the active message factory
	 */
	public MessageFactory insertBlock(Block block){
		message = message.replaceAll(BLOCK, ASUtils.capitalize(block.getType().name()));
		return this;
	}

	/**
	 * Inserts a block into the message where needed
	 * 
	 * @param block the block
	 * @return the active message factory
	 */
	public MessageFactory insertBlock(Material block){
		message = message.replaceAll(BLOCK, ASUtils.capitalize(block.name()));
		return this;
	}

	/**
	 * Inserts a player into the message where needed
	 * 
	 * @param player the player
	 * @return the active message factory
	 */
	public MessageFactory insertPlayer(Player player){
		message = message.replaceAll(PLAYER, player.getName());
		message = message.replaceAll(DISPLAY_NAME, player.getDisplayName());
		return this;
	}

	/**
	 * Inserts a world into the message where needed
	 * 
	 * @param world the world
	 * @return the active message factory
	 */
	public MessageFactory insertWorld(World world){
		message = message.replaceAll(WORLD, world.getName());
		message = message.replaceAll(MVWORLD, MVWorlds.getAlias(world));
		return this;
	}

	/**
	 * Inserts a fine and reward into the message where needed
	 * 
	 * @param type the tender type
	 * @return the active message factory
	 */
	public MessageFactory insertTender(TenderType type){
		message = message.replaceAll(FINE, plugin.getMoneyManager().formatAmount(plugin.getMoneyManager().getFine(type).getAmount()));
		message = message.replaceAll(REWARD, plugin.getMoneyManager().formatAmount(plugin.getMoneyManager().getReward(type).getAmount()));
		return this;
	}

	/**
	 * Inserts something that was clicked into the message where needed
	 * 
	 * @param clicked the thing that was clicked
	 * @return the active message factory
	 */
	public MessageFactory insertClicked(String clicked){
		message = message.replaceAll(CLICKED, clicked);
		return this;
	}

	/**
	 * Inserts an amount into the message where needed
	 * 
	 * @param amount the amount
	 * @return the active message factory
	 */
	public MessageFactory insertAmount(int amount){
		message = message.replaceAll(AMOUNT, String.valueOf(amount));
		return this;
	}

	/**
	 * Inserts a command into the message where needed
	 * 
	 * @param command the command
	 * @return the active message factory
	 */
	public MessageFactory insertCommand(String command){
		message = message.replaceAll(COMMAND, command);
		return this;
	}

	/**
	 * Inserts a hit mob name into the message where needed
	 * 
	 * @param mobName the hit mob
	 * @return the active message factory
	 */
	public MessageFactory insertHitMob(String mobName){
		message = message.replaceAll(HIT_MOB, mobName);
		return this;
	}

	/**
	 * Inserts a hit player name into the message where needed
	 * 
	 * @param playerName the hit player
	 * @return the active message factory
	 */
	public MessageFactory insertHitPlayer(String playerName){
		message = message.replaceAll(HIT_PLAYER, playerName);
		return this;
	}

	/**
	 * Inserts fields into the message where needed
	 * 
	 * @param block the block
	 * @param player the player
	 * @param world the world
	 * @param tender the tender
	 * @param clicked the thing that was clicked
	 * @return the active message factory
	 */
	public MessageFactory insert(Block block, Player player, World world, TenderType tender, String clicked){
		if(block != null){
			insertBlock(block);
		}
		if(player != null){
			insertPlayer(player);
		}
		if(world != null){
			insertWorld(world);
		}
		if(tender != null){
			insertTender(tender);
		}
		if(clicked != null){
			insertClicked(clicked);
		}
		return this;
	}

	/**
	 * Inserts fields into the message where needed
	 * 
	 * @param block the block
	 * @param player the player
	 * @param world the world
	 * @param tender the tender
	 * @return the active message factory
	 */
	public MessageFactory insert(Block block, Player player, World world, TenderType tender){
		if(block != null){
			insertBlock(block);
		}
		if(player != null){
			insertPlayer(player);
		}
		if(world != null){
			insertWorld(world);
		}
		if(tender != null){
			insertTender(tender);
		}
		return this;
	}

	/**
	 * Returns the raw, uncolored, message
	 * 
	 * @return the raw message
	 */
	public String getRaw(){
		return message;
	}

	/**
	 * Returns the colored message
	 */
	@Override
	public String toString(){
		return ChatColor.translateAlternateColorCodes('&', message);
	}
}
