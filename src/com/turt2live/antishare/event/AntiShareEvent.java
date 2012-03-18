package com.turt2live.antishare.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.turt2live.antishare.enums.NotificationType;

public class AntiShareEvent extends Event {

	private NotificationType type;
	private String variable;
	private Player player;

	/**
	 * Creates a new AntiShareEvent
	 * 
	 * @param type the Event Type
	 * @param affectedBlock the involved block
	 * @param playerInvolved the player involved
	 */
	public AntiShareEvent(NotificationType type, String variable, Player playerInvolved){
		this.type = type;
		this.variable = variable;
		this.player = playerInvolved;
	}

	/**
	 * Gets the type of event
	 * 
	 * @return the type
	 */
	public NotificationType getType(){
		return type;
	}

	/**
	 * Gets the variable, this could be a block name, player name, mob name, or something else
	 * 
	 * @return the variable
	 */
	public String getVariable(){
		return variable;
	}

	/**
	 * Gets the player involved in this event
	 * 
	 * @return the player
	 */
	public Player getInvolvedPlayer(){
		return player;
	}

	// Bukkit stuff
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList(){
		return handlers;
	}

	@Override
	public HandlerList getHandlers(){
		return handlers;
	}
}
