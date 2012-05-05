package com.turt2live.antishare.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.AntiShare.LogType;
import com.turt2live.antishare.signs.Sign;

/**
 * Event List
 * 
 * @author turt2live
 */
public class EventList {

	private boolean whitelist = false;
	private boolean useString = false;
	private boolean expBlocked = false;
	private List<Integer> blocked = new ArrayList<Integer>();
	private List<String> blocked_strings = new ArrayList<String>();
	private List<Sign> blockedsigns = new ArrayList<Sign>();

	/**
	 * Creates a new Event List
	 * 
	 * @param file the configuration file name
	 * @param node the configuration node
	 * @param configurationValue the values
	 */
	public EventList(String file, String node, String... configurationValue){
		if(configurationValue.length == 0){
			return;
		}

		// Setup
		AntiShare plugin = AntiShare.getInstance();
		boolean skip = false;

		// Sanity
		if(configurationValue.length <= 0){
			return;
		}

		// Check if it's an "all or nothing" list
		if(configurationValue[0].startsWith("*") || configurationValue[0].toLowerCase().startsWith("all")){
			for(Material m : Material.values()){
				blocked.add(m.getId());
			}
			skip = true;
		}else if(configurationValue[0].startsWith("none")){
			skip = true;
		}

		// If it's not an "all or nothing", loop it
		if(!skip){
			int index = 0;
			for(String blocked : configurationValue){
				blocked = blocked.trim();

				// Sanity
				if(blocked.length() <= 0){
					continue;
				}

				// Whitelist?
				if(blocked.equalsIgnoreCase("whitelist") && index == 0){
					whitelist = true;
					index++;
					continue;
				}

				// Sign?
				if(blocked.toLowerCase().startsWith("sign:")){
					String signname = blocked.split(":").length > 0 ? blocked.split(":")[1] : null;
					if(signname == null){
						plugin.getMessenger().log("Configuration Problem: '" + blocked + "' is not valid! (See '" + node + "' in your " + file + ")", Level.WARNING, LogType.INFO);
						index++;
						continue;
					}
					Sign sign = plugin.getSignManager().getSign(signname);
					if(sign == null){
						plugin.getMessenger().log("Configuration Problem: '" + blocked + "' is not valid! (See '" + node + "' in your " + file + ")", Level.WARNING, LogType.INFO);
						index++;
						continue;
					}
					blockedsigns.add(sign);
					index++;
					continue;
				}else if(blocked.toLowerCase().startsWith("-sign:")){
					String signname = blocked.split(":").length > 0 ? blocked.split(":")[1] : null;
					if(signname == null){
						plugin.getMessenger().log("Configuration Problem: '" + blocked + "' is not valid! (See '" + node + "' in your " + file + ")", Level.WARNING, LogType.INFO);
						index++;
						continue;
					}
					Sign sign = plugin.getSignManager().getSign(signname);
					if(sign == null){
						plugin.getMessenger().log("Configuration Problem: '" + blocked + "' is not valid! (See '" + node + "' in your " + file + ")", Level.WARNING, LogType.INFO);
						index++;
						continue;
					}
					blockedsigns.remove(sign);
					index++;
					continue;
				}

				// Check if experience is to be blocked
				if(blocked.equalsIgnoreCase("exp") || blocked.equalsIgnoreCase("experience") || blocked.equalsIgnoreCase("xp")){
					expBlocked = true;
					continue;
				}

				// Special cases
				if(blocked.equalsIgnoreCase("sign")
						|| blocked.replaceAll(" ", "").replaceAll("_", "").equalsIgnoreCase("wallsign")
						|| blocked.replaceAll(" ", "").replaceAll("_", "").equalsIgnoreCase("signpost")
						|| blocked.equalsIgnoreCase(String.valueOf(Material.SIGN.getId()))
						|| blocked.equalsIgnoreCase(String.valueOf(Material.WALL_SIGN.getId()))
						|| blocked.equalsIgnoreCase(String.valueOf(Material.SIGN_POST.getId()))){
					this.blocked.add(Material.SIGN.getId());
					this.blocked.add(Material.SIGN_POST.getId());
					this.blocked.add(Material.WALL_SIGN.getId());
					index++;
					continue;
				}

				// Try to add the item, warn otherwise
				if(blocked.startsWith("-")){
					// Negation
					blocked = blocked.replaceFirst("-", "");
					if(plugin.getItemMap().getSign(blocked) != null){
						this.blockedsigns.remove(plugin.getItemMap().getSign(blocked));
						index++;
						continue;
					}
					try{
						this.blocked.remove(plugin.getItemMap().getItem(blocked) == null ? Integer.parseInt(blocked) : plugin.getItemMap().getItem(blocked).getId());
					}catch(Exception e){
						plugin.getMessenger().log("Configuration Problem: '" + blocked + "' is not valid! (See '" + node + "' in " + file + ")", Level.WARNING, LogType.INFO);
					}
				}else{
					if(plugin.getItemMap().getSign(blocked) != null){
						this.blockedsigns.add(plugin.getItemMap().getSign(blocked));
						index++;
						continue;
					}
					try{
						this.blocked.add(plugin.getItemMap().getItem(blocked) == null ? Integer.parseInt(blocked) : plugin.getItemMap().getItem(blocked).getId());
					}catch(Exception e){
						plugin.getMessenger().log("Configuration Problem: '" + blocked + "' is not valid! (See '" + node + "' in your " + file + ")", Level.WARNING, LogType.INFO);
					}
				}
				index++;
			}
		}
	}

	/**
	 * Creates a new Event List
	 * 
	 * @param stringsOnly true to set strings only
	 * @param configurationValue the values
	 */
	public EventList(boolean stringsOnly, String... configurationValue){
		if(configurationValue.length == 0){
			return;
		}
		this.useString = stringsOnly;
		int index = 0;

		// Sanity
		if(configurationValue.length <= 0){
			return;
		}

		// Loop through objects
		for(String blocked : configurationValue){
			blocked = blocked.trim();

			// Sanity
			if(blocked.length() <= 0){
				continue;
			}

			// Whitelist?
			if(blocked.equalsIgnoreCase("whitelist") && index == 0){
				whitelist = true;
				index++;
				continue;
			}

			// Check negation
			boolean negate = false;
			if(blocked.startsWith("-")){
				negate = true;
				blocked = blocked.replaceFirst("-", "");
			}

			// Add a '/' to all strings
			if(!blocked.startsWith("/")){
				blocked = blocked + "/";
			}

			// Add item
			if(negate){
				this.blocked_strings.remove(blocked.trim().toLowerCase());
			}else{
				this.blocked_strings.add(blocked.trim().toLowerCase());
			}
			index++;
		}
	}

	/**
	 * Checks if a block is blocked
	 * 
	 * @param block the block
	 * @return true if blocked
	 */
	public boolean isBlocked(Block block){
		if(block.getState() instanceof org.bukkit.block.Sign){
			org.bukkit.block.Sign cbsign = (org.bukkit.block.Sign) block.getState();
			for(int i = 0; i < blockedsigns.size(); i++){
				Sign s = blockedsigns.get(i);
				if(s.matches(cbsign)){
					return true;
				}
			}
		}
		return isBlocked(block.getType());
	}

	/**
	 * Checks if an item is blocked
	 * 
	 * @param item the item
	 * @return true if blocked
	 */
	public boolean isBlocked(Material item){
		if(blocked.size() == 0){
			return false;
		}
		if(whitelist){
			return !blocked.contains(item.getId());
		}
		return blocked.contains(item.getId());
	}

	/**
	 * Checks if a string (usually command) is blocked
	 * 
	 * @param command the string
	 * @return true if blocked
	 */
	public boolean isBlocked(String command){
		if(!useString){
			return false;
		}
		if(blocked_strings.size() == 0){
			return false;
		}

		// Prefix '/' if needed
		if(!command.startsWith("/")){
			command = "/" + command;
		}

		// Prepare
		command = command.trim().toLowerCase();
		boolean found = false;

		// Loop through list and look for a match
		for(String string : blocked_strings){
			if(command.startsWith(string) || command.equalsIgnoreCase(string)){
				found = true;
				break;
			}
		}

		// Return correct value
		if(whitelist){
			return found == false;
		}
		return found == true;
	}

	/**
	 * Checks if experience if blocked
	 * 
	 * @return true if blocked
	 */
	public boolean isExperienceBlocked(){
		return expBlocked;
	}
}
