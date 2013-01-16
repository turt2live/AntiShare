package com.turt2live.antishare.manager;

public abstract class ConfigBackedManager extends AntiShareManager {

	@Override
	public final boolean load(){
		loadConfiguration();
		return loadManager();
	}

	/**
	 * Loads the manager
	 * 
	 * @return true if loaded
	 */
	public abstract boolean loadManager();

	/**
	 * Loads this manager's configuration
	 */
	public abstract void loadConfiguration();

}
