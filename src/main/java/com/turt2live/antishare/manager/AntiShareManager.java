package com.turt2live.antishare.manager;

import com.turt2live.antishare.AntiShare;

/**
 * AntiShare manager abstract class
 * 
 * @author turt2live
 */
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
		boolean saved = false, loaded = false;
		saved = save();
		loaded = load();
		return saved && loaded;
	}

}
