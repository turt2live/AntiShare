package com.turt2live.antishare.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.turt2live.antishare.notification.Alert.AlertTrigger;
import com.turt2live.antishare.notification.Alert.AlertType;

public class AntiShareAlertEvent extends Event {

	// BUKKIT STUFF

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers(){
		return handlers;
	}

	public static HandlerList getHandlerList(){
		return handlers;
	}

	// END BUKKIT STUFF

	private String message, playerMessage, who;
	private AlertType type;
	private AlertTrigger trigger;
	private boolean rewards, byTimer;

	/**
	 * Creates a new AntiShare event
	 * 
	 * @param message the message (global)
	 * @param playerMessage the message to the player
	 * @param who the target (or sender) of the alert
	 * @param type the type
	 * @param trigger the trigger
	 * @param rewards true if rewards are to be used
	 * @param byTimer true if this alert will be shown (timed show)
	 */
	public AntiShareAlertEvent(String message, String playerMessage, String who, AlertType type, AlertTrigger trigger, boolean rewards, boolean byTimer){
		this.message = message;
		this.playerMessage = playerMessage;
		this.who = who;
		this.trigger = trigger;
		this.type = type;
		this.rewards = rewards;
	}

	public String getMessage(){
		return message;
	}

	public String getPlayerMessage(){
		return playerMessage;
	}

	public String getWho(){
		return who;
	}

	public AlertType getType(){
		return type;
	}

	public AlertTrigger getTrigger(){
		return trigger;
	}

	public boolean isRewardsUsed(){
		return rewards;
	}

	public boolean isShownByTimer(){
		return byTimer;
	}

}
