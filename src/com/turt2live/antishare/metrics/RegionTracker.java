package com.turt2live.antishare.metrics;

import org.bukkit.GameMode;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.metrics.TrackerList.TrackerType;

/**
 * Specific tracker for regions
 * 
 * @author turt2live
 */
public class RegionTracker extends Tracker {

	private GameMode gamemode;
	private AntiShare plugin;

	/**
	 * Creates a new Region Tracker
	 * 
	 * @param name the tracker name
	 * @param type the type
	 * @param gamemode the gamemode
	 */
	public RegionTracker(String name, TrackerType type, GameMode gamemode){
		super(name, type);
		this.gamemode = gamemode;
		this.plugin = AntiShare.getInstance();
	}

	@Override
	public int getValue(){
		return plugin.getRegionManager().getAllRegions(gamemode).size();
	}

}
