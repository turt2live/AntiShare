package com.turt2live.antishare.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.AntiShare.LogType;

/**
 * Used similar to an Event List, but handles block-tracking instead
 * 
 * @author turt2live
 */
public class TrackerList {

	private List<Integer> tracked = new ArrayList<Integer>();

	/**
	 * Creates a new Tracker List
	 * 
	 * @param configurationValue the values
	 */
	public TrackerList(String... configurationValue){
		if(configurationValue.length == 0){
			return;
		}

		// Setup
		AntiShare plugin = AntiShare.instance;
		boolean skip = false;

		// Check if it's an "all or nothing" list
		if(configurationValue.length == 1){
			if(configurationValue[0].startsWith("*") || configurationValue[0].toLowerCase().startsWith("all")){
				for(Material m : Material.values()){
					tracked.add(m.getId());
				}
				skip = true;
			}else if(configurationValue[0].startsWith("none")){
				skip = true;
			}
		}

		// If it's not an "all or nothing", loop it
		if(!skip){
			for(String tracked : configurationValue){
				tracked = tracked.trim();

				// Special cases
				if(tracked.equalsIgnoreCase("sign")
						|| tracked.replaceAll(" ", "").replaceAll("_", "").equalsIgnoreCase("wallsign")
						|| tracked.replaceAll(" ", "").replaceAll("_", "").equalsIgnoreCase("signpost")
						|| tracked.equalsIgnoreCase(String.valueOf(Material.SIGN.getId()))
						|| tracked.equalsIgnoreCase(String.valueOf(Material.WALL_SIGN.getId()))
						|| tracked.equalsIgnoreCase(String.valueOf(Material.SIGN_POST.getId()))){
					this.tracked.add(Material.SIGN.getId());
					this.tracked.add(Material.SIGN_POST.getId());
					this.tracked.add(Material.WALL_SIGN.getId());
					continue;
				}

				// Try to add the item, warn otherwise
				try{
					this.tracked.add(plugin.getItemMap().getItem(tracked) == null ? Integer.parseInt(tracked) : plugin.getItemMap().getItem(tracked).getId());
				}catch(Exception e){
					plugin.getMessenger().log("Configuration Problem: '" + tracked + "' is not valid!", Level.WARNING, LogType.INFO);
				}
			}
		}
	}

	/**
	 * Checks if an item is to be tracked
	 * 
	 * @param item the item
	 * @return true if the block should be tracked
	 */
	public boolean isTracked(Material item){
		if(tracked.size() == 0){
			return false;
		}
		return tracked.contains(item.getId());
	}

}
