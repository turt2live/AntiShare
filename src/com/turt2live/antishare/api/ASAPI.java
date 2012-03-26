package com.turt2live.antishare.api;

import org.bukkit.entity.Player;

import com.turt2live.antishare.Conflicts;
import com.turt2live.antishare.SQL.SQLManager;
import com.turt2live.antishare.conversations.ConfigurationConversation;
import com.turt2live.antishare.permissions.PermissionsHandler;
import com.turt2live.antishare.regions.RegionHandler;
import com.turt2live.antishare.storage.VirtualStorage;

/**
 * AntiShare API
 * 
 * @author <a href='https://github.com/turt2live'>turt2live</a>
 */
public class ASAPI extends APIBase {

	private ConflictAPI conflicts;
	private EventAPI events;
	private RegionAPI regions;
	private SettingsAPI settings;
	private UtilsAPI utils;

	/**
	 * Gets the conflict API
	 * 
	 * @return the conflict API
	 */
	public ConflictAPI getConflictAPI(){
		if(conflicts == null){
			conflicts = new ConflictAPI();
		}
		return conflicts;
	}

	/**
	 * Gets the event API
	 * 
	 * @return the event API
	 */
	public EventAPI getEventAPI(){
		if(events == null){
			events = new EventAPI();
		}
		return events;
	}

	/**
	 * Gets the region API
	 * 
	 * @return the region API
	 */
	public RegionAPI getRegionAPI(){
		if(regions == null){
			regions = new RegionAPI();
		}
		return regions;
	}

	/**
	 * Gets the settings API
	 * 
	 * @return the settings API
	 */
	public SettingsAPI getSettingsAPI(){
		if(settings == null){
			settings = new SettingsAPI();
		}
		return settings;
	}

	/**
	 * Get the utilities API
	 * 
	 * @return the utilities API
	 */
	public UtilsAPI getUtilsAPI(){
		if(utils == null){
			utils = new UtilsAPI();
		}
		return utils;
	}

	/**
	 * Gets the conflicts handler
	 * 
	 * @return AntiShare's conflict handler
	 */
	public Conflicts getConflictHandler(){
		return getPlugin().getConflicts();
	}

	/**
	 * Gets the region handler used by AntiShare
	 * 
	 * @return the region handler
	 */
	public RegionHandler getRegionHandler(){
		return getPlugin().getRegionHandler();
	}

	/**
	 * Gets the SQL Manager being used
	 * 
	 * @return the SQL Manager instance
	 */
	public SQLManager getSQLManager(){
		return getPlugin().getSQLManager();
	}

	/**
	 * Gets the virtual storage system
	 * 
	 * @return the virtual storage
	 */
	public VirtualStorage getStorageHandler(){
		return getPlugin().storage;
	}

	/**
	 * Gets the permission handler used by AntiShare
	 * 
	 * @return the handler
	 */
	public PermissionsHandler getPermissionsHandler(){
		return getPlugin().getPermissions();
	}

	/**
	 * Force starts a configuration helper upon a Player
	 * 
	 * @param player the player to send the helper to
	 */
	public void startConfigurationHelper(Player player){
		new ConfigurationConversation(getPlugin(), player);
	}
}
