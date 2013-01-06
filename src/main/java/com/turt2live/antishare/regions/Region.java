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

	public String getName(){
		return name;
	}

	public String getWorldName(){
		return worldName;
	}

	public String getOwner(){
		return owner;
	}

	public String getID(){
		return id;
	}

	public String getEnterMessage(){
		return enterMessage;
	}

	public String getExitMessage(){
		return exitMessage;
	}

	public boolean isEnterMessageShown(){
		return showEnterMessage;
	}

	public boolean isExitMessageShown(){
		return showExitMessage;
	}

	public Cuboid getCuboid(){
		return size.clone();
	}

	public ASInventory getInventory(){
		return inventory.clone();
	}

	public GameMode getGameMode(){
		return gamemode;
	}

	public RegionConfiguration getConfig(){
		return config;
	}

	public void setName(String name){
		this.name = name;
	}

	public void setWorld(World world){
		this.worldName = world.getName();
	}

	public void setOwner(String owner){
		this.owner = owner;
	}

	public void setID(String id){
		this.id = id;
	}

	public void setEnterMessage(String enterMessage){
		this.enterMessage = enterMessage;
	}

	public void setExitMessage(String exitMessage){
		this.exitMessage = exitMessage;
	}

	public void setCuboid(Cuboid cuboid){
		this.size = cuboid.clone();
	}

	public void setShowEnterMessage(boolean showEnterMessage){
		this.showEnterMessage = showEnterMessage;
	}

	public void setShowExitMessage(boolean showExitMessage){
		this.showExitMessage = showExitMessage;
	}

	public void setInventory(ASInventory inventory){
		this.inventory = inventory.clone();
	}

	public void setGameMode(GameMode gamemode){
		this.gamemode = gamemode;
	}

	public void setConfig(RegionConfiguration config){
		this.config = config;
	}

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
		}
		region.setEnterMessage(yaml.getString("enterMessage"));
		region.setExitMessage(yaml.getString("exitMessage"));
		region.setShowEnterMessage(yaml.getBoolean("showEnter"));
		region.setShowExitMessage(yaml.getBoolean("showExit"));
		region.setID(yaml.getString("id"));
		region.setGameMode(GameMode.valueOf(yaml.getString("gamemode")));
		region.setWorld(world);
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

}
