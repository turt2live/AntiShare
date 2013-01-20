package com.turt2live.antishare.manager;

import com.turt2live.antishare.AntiShare;

public abstract class AntiShareManager {

	protected AntiShare plugin = AntiShare.getInstance();

	/**
	 * Loads this manager
	 * 
	 * @return true if successful
	 */
	public abstract boolean load();

	/**
	 * Saves this manager
	 * 
	 * @return true if successful
	 */
	public abstract boolean save();

	/**
	 * Reloads this manager
	 * 
	 * @return true if successful
	 */
	public final boolean reload(){
		boolean s = false, l = false;
		s = save();
		l = load();
		return s && l;
	}

}
