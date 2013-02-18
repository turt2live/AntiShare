package com.turt2live.antishare.metrics;

import com.turt2live.antishare.lang.Localization;

/**
 * Tracker type
 * 
 * @author turt2live
 */
public enum TrackerType{
	SPECIAL("Unknown", "Unkown"),
	FEATURE_FINES_REWARDS("Features Used", "Fines/Rewards"),
	FEATURE_SIGNS("Features Used", "Signs"),
	FEATURE_REGIONS("Features Used", "Regions"),
	FEATURE_GM_BLOCKS("Features Used", "GameMode Blocks"),
	FEATURE_INVENTORIES("Features Used", "Inventories"),
	FEATURE_WORLD_SPLIT("Features Used", "World Split"),
	LOCALE("Locale (File Name)", Localization.getLocaleFileName()),
	MCMMO("mcMMO Servers", "default value");

	private String graphname = "DEFAULT";
	private String name = "DEFAULT";

	private TrackerType(String graphname, String name){
		this.graphname = graphname;
		this.name = name;
	}

	/**
	 * Returns the graph name
	 * 
	 * @return the graph name
	 */
	public String getGraphName(){
		return graphname;
	}

	/**
	 * Gets the name of the tracker
	 * 
	 * @return the name
	 */
	public String getName(){
		return name;
	}
}