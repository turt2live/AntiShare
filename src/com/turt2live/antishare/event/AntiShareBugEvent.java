package com.turt2live.antishare.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.turt2live.antishare.debug.Bug;

public class AntiShareBugEvent extends Event {

	private Bug bug;
	private boolean printTrace = true;

	/**
	 * Creates a new AntiShareBugEvent
	 * 
	 * @param bug The bug
	 */
	public AntiShareBugEvent(Bug bug){
		this.bug = bug;
	}

	/**
	 * Gets the Bug found
	 * 
	 * @return the bug
	 */
	public Bug getBug(){
		return bug;
	}

	/**
	 * Sets whether or not to print the error to the console
	 * 
	 * @param print true to print the error
	 */
	public void setPrintTrace(boolean print){
		printTrace = print;
	}

	/**
	 * Determines if the Debugger will be printing the error to the console
	 * 
	 * @return true if the Debugger will be printing the error
	 */
	public boolean getPrintTrace(){
		return printTrace;
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
