package com.turt2live.antishare;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This is for use by a developer to determine bugs<br>
 * <b>NOTE:</b> This is NOT used everywhere, it may only be used in certain places
 * 
 * @author Travis Ralston
 * 
 */
public class ASEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList(){
		return handlers;
	}

	private String eventName = "";
	private String sender;
	private String message;
	private final String timeFormat = "%.20f";

	public ASEvent(String eventName, String sender, long startTime, long endTime, String asevent){
		float e = Float.valueOf(endTime + "");
		float s = Float.valueOf(startTime + "");
		this.eventName = eventName;
		this.sender = sender;
		this.message = asevent + " took " + String.format(timeFormat, (e - s) / 1000) + " seconds";
	}

	public ASEvent(String eventName, String sender, long timeSpent, String asevent){
		float t = Float.valueOf(timeSpent + "");
		this.eventName = eventName;
		this.sender = sender;
		this.message = asevent + " took " + String.format(timeFormat, (t) / 1000) + " seconds";
	}

	public String getASEventName(){
		return eventName;
	}

	@Override
	public HandlerList getHandlers(){
		return handlers;
	}

	public String getMessage(){
		return message;
	}

	public String getSender(){
		return sender;
	}
}
