package com.turt2live.antishare.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.World;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.regions.ASRegion;
import com.turt2live.antishare.storage.PerWorldConfig.ListType;

/**
 * Region configuration
 * 
 * @author turt2live
 */
public class PerRegionConfig {
	private EventList block_break;
	private EventList block_place;
	private EventList right_click;
	private EventList use;
	private EventList drop;
	private EventList pickup;
	private EventList death;
	private EventList commands;
	private boolean clearInventoriesOnBreak = true;
	private boolean removeAttachedBlocksOnBreak = true;
	private World world;
	private ASRegion region;

	/**
	 * Creates a new region configuration
	 * 
	 * @param region the region
	 */
	public PerRegionConfig(ASRegion region){
		AntiShare plugin = AntiShare.instance;
		this.world = region.getWorld();
		this.region = region;

		// Setup configuration
		File path = new File(plugin.getDataFolder(), "region_configurations");
		path.mkdirs();
		EnhancedConfiguration regionConfig = new EnhancedConfiguration(new File(path, region.getName() + ".yml"), plugin);
		regionConfig.loadDefaults(plugin.getResource("resources/region.yml"));
		if(!regionConfig.fileExists() || !regionConfig.checkDefaults()){
			regionConfig.saveDefaults();
		}
		regionConfig.load();

		// Generate lists
		block_place = getList("block-place", "block-place", regionConfig, false);
		block_break = getList("block-break", "block-break", regionConfig, false);
		death = getList("drop-items-on-death", "dropped-items-on-death", regionConfig, false);
		pickup = getList("pickup-items", "picked-up-items", regionConfig, false);
		drop = getList("drop-items", "dropped-items", regionConfig, false);
		right_click = getList("right-click", "right-click", regionConfig, false);
		use = getList("use-items", "use-items", regionConfig, false);
		commands = getList("commands", "commands", regionConfig, true);

		// Get options
		boolean value = regionConfig.getBoolean("enabled-features.no-drops-when-block-break.inventories");
		if(regionConfig.getString("enabled-features.no-drops-when-block-break.inventories").equalsIgnoreCase("global")){
			value = plugin.getConfig().getBoolean("enabled-features.no-drops-when-block-break.inventories");
		}
		if(regionConfig.getString("enabled-features.no-drops-when-block-break.inventories").equalsIgnoreCase("world")){
			value = plugin.getListener().getConfig(world).getRaw().getBoolean("enabled-features.no-drops-when-block-break.inventories");
		}
		clearInventoriesOnBreak = value;
		value = regionConfig.getBoolean("enabled-features.no-drops-when-block-break.attached-blocks");
		if(regionConfig.getString("enabled-features.no-drops-when-block-break.attached-blocks").equalsIgnoreCase("global")){
			value = plugin.getConfig().getBoolean("enabled-features.no-drops-when-block-break.attached-blocks");
		}
		if(regionConfig.getString("enabled-features.no-drops-when-block-break.attached-blocks").equalsIgnoreCase("world")){
			value = plugin.getListener().getConfig(world).getRaw().getBoolean("enabled-features.no-drops-when-block-break.attached-blocks");
		}
		removeAttachedBlocksOnBreak = value;
	}

	private EventList getList(String triggerPath, String listPath, EnhancedConfiguration regionConfig, boolean stringsOnly){
		// Setup
		boolean enabled = false;
		boolean global = false;
		boolean gworld = false;
		AntiShare plugin = AntiShare.instance;

		// Determine if enabled
		if(regionConfig.getString("blocked-actions." + triggerPath).equalsIgnoreCase("global")){
			global = true;
			enabled = plugin.getConfig().getBoolean("blocked-actions." + triggerPath);
		}else if(regionConfig.getString("blocked-actions." + triggerPath).equalsIgnoreCase("world")){
			if(plugin.getListener().getConfig(world).getRaw().getString("blocked-actions." + triggerPath).equalsIgnoreCase("global")){
				enabled = plugin.getConfig().getBoolean("blocked-actions." + triggerPath);
				gworld = true;
			}else{
				enabled = plugin.getListener().getConfig(world).getRaw().getBoolean("blocked-actions." + triggerPath);
				global = true;
			}
		}else{
			enabled = regionConfig.getBoolean("blocked-actions." + triggerPath);
		}

		// Get the list
		String list = "";
		if(enabled){
			list = regionConfig.getString("blocked-lists." + listPath);
			if(list.equalsIgnoreCase("global")){
				list = plugin.getConfig().getString("blocked-lists." + listPath);
			}
			if(list.equalsIgnoreCase("world")){
				list = plugin.getListener().getConfig(world).getRaw().getString("blocked-lists." + listPath);
				if(list.equalsIgnoreCase("global")){
					list = plugin.getConfig().getString("blocked-lists." + listPath);
				}
			}
		}

		// Generate and return
		return stringsOnly ? new EventList(true, list.split(",")) : new EventList(gworld ? world.getName() + ".yml" : (global ? "config.yml" : "region_configurations/" + region.getName() + ".yml"), "blocked-actions." + triggerPath, list.split(","));
	}

	/**
	 * Prints this world's configuration to the writer
	 * 
	 * @param out the writer
	 * @throws IOException for internal handling
	 */
	public void print(BufferedWriter out) throws IOException{
		out.write("## REGION: " + region.getName() + ": \r\n");
		EnhancedConfiguration regionConfig = new EnhancedConfiguration(new File(AntiShare.instance.getDataFolder() + File.separator + "region_configurations", region.getName() + ".yml"), AntiShare.instance);
		regionConfig.load();
		for(String key : regionConfig.getKeys(true)){
			out.write(key + ": " + (regionConfig.getString(key).startsWith("MemorySection") ? "" : regionConfig.getString(key, "")) + "\r\n");
		}
	}

	/**
	 * Checks if an item in this world is blocked
	 * 
	 * @param material the material
	 * @param list the list type
	 * @return true if blocked
	 */
	public boolean isBlocked(Material material, ListType list){
		switch (list){
		case BLOCK_BREAK:
			return block_break.isBlocked(material);
		case BLOCK_PLACE:
			return block_place.isBlocked(material);
		case RIGHT_CLICK:
			return right_click.isBlocked(material);
		case USE:
			return use.isBlocked(material);
		case DROP:
			return drop.isBlocked(material);
		case PICKUP:
			return pickup.isBlocked(material);
		case DEATH:
			return death.isBlocked(material);
		}
		return false;
	}

	/**
	 * Checks if a string (usually command) in this world is blocked
	 * 
	 * @param string the string
	 * @param list the list type
	 * @return true if blocked
	 */
	public boolean isBlocked(String string, ListType list){
		switch (list){
		case COMMAND:
			return commands.isBlocked(string);
		}
		return false;
	}

	/**
	 * Determines if a block's inventory (eg: chest) should be cleared upon it being broken
	 * 
	 * @return true if clearing should be done
	 */
	public boolean clearBlockInventoryOnBreak(){
		return clearInventoriesOnBreak;
	}

	/**
	 * Determines if any attached blocks should be removed upon it being broken
	 * 
	 * @return true if attached blocks are to be removed
	 */
	public boolean removeAttachedBlocksOnBreak(){
		return removeAttachedBlocksOnBreak;
	}

	/**
	 * Gets the region that uses this configuration
	 * 
	 * @return the region
	 */
	public ASRegion getRegion(){
		return region;
	}

	/**
	 * Gets the world for this configuration
	 * 
	 * @return the world
	 */
	public World getWorld(){
		return world;
	}
}
