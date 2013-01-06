package com.turt2live.antishare.regions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.inventory.ASInventory;
import com.turt2live.antishare.util.ASUtils;

/**
 * An AntiShare Region
 */
public class Region {

	private static AntiShare plugin = AntiShare.getInstance();
	private String worldName, owner, id, enterMessage, exitMessage, name;
	private Cuboid size;
	private boolean showEnterMessage, showExitMessage;
	private ASInventory inventory;
	private Map<String, GameMode> gamemodes = new HashMap<String, GameMode>();
	private RegionConfiguration config;
	private GameMode gamemode;

	public static final File REGION_CONFIGURATIONS = new File(plugin.getDataFolder(), "region_configurations");
	public static final File REGION_INFORMATION = new File(plugin.getDataFolder(), "data" + File.separator + "regions");
	public static final int REGION_VERSION = 2;

	/**
	 * Gets the name of this region
	 * 
	 * @return the region name
	 */
	public String getName(){
		return name;
	}

	/**
	 * Gets the world name this region belongs to
	 * 
	 * @return the world this region resides in
	 */
	public String getWorldName(){
		return worldName;
	}

	/**
	 * Gets the person who made the region
	 * 
	 * @return the region owner
	 */
	public String getOwner(){
		return owner;
	}

	/**
	 * Gets the unique region ID
	 * 
	 * @return the region ID
	 */
	public String getID(){
		return id;
	}

	/**
	 * Gets the region's "enter message"
	 * 
	 * @return the enter message
	 */
	public String getEnterMessage(){
		return enterMessage;
	}

	/**
	 * Gets the region's "exit message"
	 * 
	 * @return the exit message
	 */
	public String getExitMessage(){
		return exitMessage;
	}

	/**
	 * Determines if the enter message is shown to players
	 * 
	 * @return true if shown
	 */
	public boolean isEnterMessageShown(){
		return showEnterMessage;
	}

	/**
	 * Determines if the exit message is shown to players
	 * 
	 * @return true if shown
	 */
	public boolean isExitMessageShown(){
		return showExitMessage;
	}

	/**
	 * Gets a <b>cloned</b> copy of the cuboid this region represents
	 * 
	 * @return the <b>cloned</b> cuboid of this region
	 */
	public Cuboid getCuboid(){
		return size.clone();
	}

	/**
	 * Gets the <b>cloned</b> copy of the inventory for this region. This can be null.
	 * 
	 * @return null for no inventory, otherwise a <b>cloned</b> inventory
	 */
	public ASInventory getInventory(){
		return inventory == null ? null : inventory.clone();
	}

	/**
	 * Gets the Game Mode for this region
	 * 
	 * @return the region's Game Mode
	 */
	public GameMode getGameMode(){
		return gamemode;
	}

	/**
	 * Gets the region's configuration class
	 * 
	 * @return the configuration
	 */
	public RegionConfiguration getConfig(){
		return config;
	}

	/**
	 * Sets the region name
	 * 
	 * @param name the new name
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * Sets the world by which this region resides in
	 * 
	 * @param world the region's world
	 */
	public void setWorld(World world){
		this.worldName = world.getName();
		size.update(world);
	}

	/**
	 * Sets the region's owner (creator)
	 * 
	 * @param owner the new creator
	 */
	public void setOwner(String owner){
		this.owner = owner;
	}

	/**
	 * Sets the regions unique ID. This is not verified to be unique internally and is trusted as such.
	 * 
	 * @param id the new ID
	 */
	public void setID(String id){
		this.id = id;
	}

	/**
	 * Sets the enter message for this region
	 * 
	 * @param enterMessage the new enter message
	 */
	public void setEnterMessage(String enterMessage){
		this.enterMessage = enterMessage;
	}

	/**
	 * Sets the exit message for this region
	 * 
	 * @param exitMessage the new exit message
	 */
	public void setExitMessage(String exitMessage){
		this.exitMessage = exitMessage;
	}

	/**
	 * Sets the area by which this region occupies, this is not verified internally for overlapping regions. <b>The Cuboid passed is cloned before being set</b>
	 * 
	 * @param cuboid the new region area
	 */
	public void setCuboid(Cuboid cuboid){
		this.size = cuboid.clone();
	}

	/**
	 * Sets the boolean status to show or hide the enter message
	 * 
	 * @param showEnterMessage true to show the enter message
	 */
	public void setShowEnterMessage(boolean showEnterMessage){
		this.showEnterMessage = showEnterMessage;
	}

	/**
	 * Sets the boolean status to show or hide the exit message
	 * 
	 * @param showExitMessage true to show the exit message
	 */
	public void setShowExitMessage(boolean showExitMessage){
		this.showExitMessage = showExitMessage;
	}

	/**
	 * Sets the inventory for this region. This can be null for no inventory. <b>The inventory is cloned before being set internally</b>
	 * 
	 * @param inventory
	 */
	public void setInventory(ASInventory inventory){
		this.inventory = inventory != null ? inventory.clone() : null;
	}

	/**
	 * Sets the Game Mode for this region
	 * 
	 * @param gamemode the region's new Game Mode
	 */
	public void setGameMode(GameMode gamemode){
		this.gamemode = gamemode;
	}

	/**
	 * Sets the configuration of this region.
	 * 
	 * @param config the new configuration
	 */
	public void setConfig(RegionConfiguration config){
		this.config = config;
	}

	/**
	 * Saves the region and all of it's information to disk
	 */
	public void save(){
		if(!REGION_INFORMATION.exists()){
			REGION_INFORMATION.mkdirs();
		}
		File saveFile = new File(REGION_INFORMATION, ASUtils.fileSafeName(name) + ".yml");
		EnhancedConfiguration yaml = new EnhancedConfiguration(saveFile, plugin);
		yaml.load();
		yaml.set("name", getName());
		yaml.set("id", getID());
		yaml.set("cuboid", getCuboid().serialize());
		yaml.set("owner", getOwner());
		yaml.set("gamemode", getGameMode().name());
		yaml.set("showEnter", isEnterMessageShown());
		yaml.set("showExit", isExitMessageShown());
		yaml.set("enterMessage", getEnterMessage());
		yaml.set("exitMessage", getExitMessage());
		yaml.set("worldName", getWorldName());
		yaml.set("players", playersAsList());
		yaml.set("version", REGION_VERSION);
		yaml.save();
	}

	/**
	 * Loads a region from a YAML file. The passed file is assumed to be a valid region file
	 * 
	 * @param saveFile the region file
	 * @return the region, or null if there was an error
	 */
	public static Region fromFile(File saveFile){
		Region region = new Region();
		AntiShare plugin = AntiShare.getInstance();
		EnhancedConfiguration yaml = new EnhancedConfiguration(saveFile, plugin);
		yaml.load();
		World world = plugin.getServer().getWorld(yaml.getString("worldName"));
		if(world == null){
			plugin.getLogger().warning("Failed to load world for region '" + region.getName() + "' (world name='" + yaml.getString("worldName") + "')");
			return null;
		}
		region.setEnterMessage(yaml.getString("enterMessage"));
		region.setExitMessage(yaml.getString("exitMessage"));
		region.setShowEnterMessage(yaml.getBoolean("showEnter"));
		region.setShowExitMessage(yaml.getBoolean("showExit"));
		region.setID(yaml.getString("id"));
		region.setGameMode(GameMode.valueOf(yaml.getString("gamemode")));
		region.setWorld(world);
		region.setConfig(new RegionConfiguration(region));
		if(yaml.getInt("version", 0) == REGION_VERSION){
			List<String> players = yaml.getStringList("players");
			region.populatePlayers(players);
			@SuppressWarnings ("unchecked")
			Map<String, Object> cuboid = (Map<String, Object>) yaml.get("cuboid");
			Cuboid area = Cuboid.deserialize(cuboid);
			region.setCuboid(area);
		}else{
			double mix = yaml.getDouble("mi-x"), miy = yaml.getDouble("mi-y"), miz = yaml.getDouble("mi-z");
			double max = yaml.getDouble("ma-x"), may = yaml.getDouble("ma-y"), maz = yaml.getDouble("ma-z");
			Location l1 = new Location(world, mix, miy, miz);
			Location l2 = new Location(world, max, may, maz);
			Cuboid cuboid = new Cuboid(l1, l2);
			region.setCuboid(cuboid);
			loadLegacyPlayerInformation(region);
		}
		return region;
	}

	private List<String> playersAsList(){
		List<String> list = new ArrayList<String>();
		for(String playername : gamemodes.keySet()){
			list.add(playername + " " + gamemodes.get(playername).name());
		}
		return list;
	}

	private void populatePlayers(List<String> list){
		for(String record : list){
			String[] parts = record.split(" ");
			if(parts.length > 1){
				String playerName = parts[0];
				GameMode gamemode = GameMode.valueOf(parts[1]);
				gamemodes.put(playerName, gamemode);
			}
		}
	}

	private static void loadLegacyPlayerInformation(Region region){
		// Check file/folder
		File saveFolder = new File(plugin.getDataFolder(), "data" + File.separator + "region_players");
		File saveFile = new File(saveFolder, region.getID() + ".yml");
		if(!saveFile.exists()){
			return;
		}

		// Load
		EnhancedConfiguration playerInfo = new EnhancedConfiguration(saveFile, plugin);
		playerInfo.load();
		for(String key : playerInfo.getKeys(false)){
			region.gamemodes.put(key, GameMode.valueOf(playerInfo.getString(key)));
		}
	}

}
