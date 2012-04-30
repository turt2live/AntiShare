package com.turt2live.antishare.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.regions.WorldSplit;
import com.turt2live.antishare.regions.WorldSplit.Axis;

/**
 * Represents a per-world configuration
 * 
 * @author turt2live
 */
public class PerWorldConfig {

	/**
	 * An enum to determine what list to pull from
	 * 
	 * @author turt2live
	 */
	public static enum ListType{
		BLOCK_PLACE,
		BLOCK_BREAK,
		RIGHT_CLICK,
		USE,
		DROP,
		PICKUP,
		DEATH,
		COMMAND;
	}

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
	private boolean splitActive = false;
	private World world;
	private WorldSplit split;

	/**
	 * Creates a new world configuration
	 * 
	 * @param world the world
	 */
	public PerWorldConfig(World world){
		AntiShare plugin = AntiShare.instance;
		this.world = world;

		// Setup configuration
		EnhancedConfiguration worldConfig = new EnhancedConfiguration(new File(plugin.getDataFolder(), world.getName() + "_config.yml"), plugin);
		worldConfig.loadDefaults(plugin.getResource("resources/world.yml"));
		if(!worldConfig.fileExists() || !worldConfig.checkDefaults()){
			worldConfig.saveDefaults();
		}
		worldConfig.load();

		// Generate lists
		block_place = getList("block-place", "block-place", worldConfig, false);
		block_break = getList("block-break", "block-break", worldConfig, false);
		death = getList("drop-items-on-death", "dropped-items-on-death", worldConfig, false);
		pickup = getList("pickup-items", "picked-up-items", worldConfig, false);
		drop = getList("drop-items", "dropped-items", worldConfig, false);
		right_click = getList("right-click", "right-click", worldConfig, false);
		use = getList("use-items", "use-items", worldConfig, false);
		commands = getList("commands", "commands", worldConfig, true);

		// Get options
		boolean value = worldConfig.getBoolean("enabled-features.no-drops-when-block-break.inventories");
		if(worldConfig.getString("enabled-features.no-drops-when-block-break.inventories").equalsIgnoreCase("global")){
			value = plugin.getConfig().getBoolean("enabled-features.no-drops-when-block-break.inventories");
		}
		clearInventoriesOnBreak = value;
		value = worldConfig.getBoolean("enabled-features.no-drops-when-block-break.attached-blocks");
		if(worldConfig.getString("enabled-features.no-drops-when-block-break.attached-blocks").equalsIgnoreCase("global")){
			value = plugin.getConfig().getBoolean("enabled-features.no-drops-when-block-break.attached-blocks");
		}
		removeAttachedBlocksOnBreak = value;

		// Check world split
		splitActive = worldConfig.getString("enabled-features.world-split").equalsIgnoreCase("global") ? plugin.getConfig().getBoolean("enabled-features.world-split") : worldConfig.getBoolean("enabled-features.world-split");
		if(splitActive){
			String axisStr = worldConfig.getString("worldsplit.split");
			if(axisStr.equalsIgnoreCase("global")){
				axisStr = plugin.getConfig().getString("worldsplit.split");
			}
			Axis axis = Axis.getAxis(axisStr);
			double split = worldConfig.getString("worldsplit.value").equalsIgnoreCase("global") ? plugin.getConfig().getDouble("worldsplit.value") : worldConfig.getDouble("worldsplit.value");
			double creative = worldConfig.getString("worldsplit.creative").equalsIgnoreCase("global") ? plugin.getConfig().getDouble("worldsplit.creative") : worldConfig.getDouble("worldsplit.creative");
			double survival = worldConfig.getString("worldsplit.survival").equalsIgnoreCase("global") ? plugin.getConfig().getDouble("worldsplit.survival") : worldConfig.getDouble("worldsplit.survival");
			this.split = new WorldSplit(world, split, axis, creative, survival);
			boolean warn = worldConfig.getString("worldsplit.warning.enabled").equalsIgnoreCase("global") ? plugin.getConfig().getBoolean("worldsplit.warning.enabled") : worldConfig.getBoolean("worldsplit.warning.enabled");
			double before = worldConfig.getString("worldsplit.warning.blocks").equalsIgnoreCase("global") ? plugin.getConfig().getDouble("worldsplit.warning.blocks") : worldConfig.getDouble("worldsplit.warning.blocks");
			long warnEvery = worldConfig.getString("worldsplit.warning.warn-every").equalsIgnoreCase("global") ? plugin.getConfig().getLong("worldsplit.warning.warn-every") : worldConfig.getLong("worldsplit.warning.warn-every");
			warnEvery *= 1000;
			this.split.warning(warn, before, warnEvery);
		}
	}

	private EventList getList(String triggerPath, String listPath, EnhancedConfiguration worldConfig, boolean stringsOnly){
		// Setup
		boolean enabled = false;
		AntiShare plugin = AntiShare.instance;

		// Determine if enabled
		if(worldConfig.getString("blocked-actions." + triggerPath).equalsIgnoreCase("global")){
			enabled = plugin.getConfig().getBoolean("blocked-actions." + triggerPath);
		}else{
			enabled = worldConfig.getBoolean("blocked-actions." + triggerPath);
		}

		// Get the list
		String list = "";
		if(enabled){
			list = worldConfig.getString("blocked-lists." + listPath);
			if(list.equalsIgnoreCase("global")){
				list = plugin.getConfig().getString("blocked-lists." + listPath);
			}
		}

		// Generate and return
		return stringsOnly ? new EventList(true, list.split(",")) : new EventList("blocked-actions." + triggerPath, list.split(","));
	}

	/**
	 * Prints this world's configuration to the writer
	 * 
	 * @param out the writer
	 * @throws IOException for internal handling
	 */
	public void print(BufferedWriter out) throws IOException{
		EnhancedConfiguration worldConfig = new EnhancedConfiguration(new File(AntiShare.instance.getDataFolder(), world.getName() + "_config.yml"), AntiShare.instance);
		worldConfig.load();
		for(String key : worldConfig.getKeys(true)){
			out.write(key + ": " + (worldConfig.getString(key).startsWith("MemorySection") ? "" : worldConfig.getString(key, "")) + "\r\n");
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
	 * Checks the player for the World Split
	 * 
	 * @param player the player
	 */
	public void checkSplit(Player player){
		if(splitActive){
			split.checkPlayer(player);
		}
	}

	/**
	 * Warn the player of a World Split boundary if required
	 * 
	 * @param player the player
	 */
	public void warnSplit(Player player){
		if(splitActive){
			split.warn(player);
		}
	}

}
