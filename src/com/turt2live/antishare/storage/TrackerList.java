package com.turt2live.antishare.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.AntiShare.LogType;

/**
 * Used similar to an Event List, but handles block-tracking instead
 * 
 * @author turt2live
 */
public class TrackerList {

	private List<String> tracked = new ArrayList<String>();

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
		AntiShare plugin = AntiShare.getInstance();
		boolean skip = false;

		// Check if it's an "all or nothing" list
		if(configurationValue.length == 1){
			if(configurationValue[0].startsWith("*") || configurationValue[0].toLowerCase().startsWith("all")){
				for(Material m : Material.values()){
					StringBuilder ret = new StringBuilder();
					ret.append(m.getId());
					ret.append(":");
					ret.append("*");
					tracked.add(ret.toString());
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
					this.tracked.add(ASUtils.materialToString(Material.SIGN, false));
					this.tracked.add(ASUtils.materialToString(Material.SIGN_POST, false));
					this.tracked.add(ASUtils.materialToString(Material.WALL_SIGN, false));
					continue;
				}

				// Try to add the item, warn otherwise
				try{
					if(plugin.getItemMap().getItem(tracked, false) == null){
						throw new Exception("");
					}
					this.tracked.add(plugin.getItemMap().getItem(tracked, false));
				}catch(Exception e){
					plugin.getMessenger().log("Configuration Problem: '" + tracked + "' is not valid!", Level.WARNING, LogType.INFO);
				}
			}
		}
	}

	/**
	 * Determines if a block is tracked or not
	 * 
	 * @param block the block
	 * @return true if tracked
	 */
	public boolean isTracked(Block block){
		if(tracked.size() == 0){
			return false;
		}
		String defaultID = ASUtils.blockToString(block, false);
		if(tracked.contains(defaultID)){
			return true;
		}
		defaultID = block.getTypeId() + ":*";
		if(tracked.contains(defaultID)){
			return true;
		}
		return false;
	}

}
