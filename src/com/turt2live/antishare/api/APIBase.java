package com.turt2live.antishare.api;

import org.bukkit.Bukkit;

import com.turt2live.antishare.AntiShare;

public abstract class APIBase {

	private AntiShare plugin;

	/**
	 * Gets the instance of AntiShare running on the server
	 * 
	 * @return AntiShare
	 */
	public AntiShare getPlugin(){
		if(plugin == null){
			plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
		}
		return plugin;
	}

	/**
	 * Gets the current version of AntiShare
	 * 
	 * @return current version
	 */
	public String getVersion(){
		return getPlugin().getDescription().getVersion();
	}

	/**
	 * Gets the current version of AntiShare in a double format
	 * 
	 * @return current version as a number
	 */
	public double getVersionAsNumber(){
		return Double.valueOf(getPlugin().getDescription().getVersion().split("-")[0].replaceFirst("\\.", ""));
	}
}
