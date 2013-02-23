package com.turt2live.antishare;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.feildmaster.lib.configuration.PluginWrapper;
import com.turt2live.antishare.config.ASConfig;
import com.turt2live.antishare.manager.BlockManager;
import com.turt2live.antishare.manager.CuboidManager;
import com.turt2live.antishare.manager.HookManager;
import com.turt2live.antishare.manager.InventoryManager;
import com.turt2live.antishare.manager.MoneyManager;
import com.turt2live.antishare.manager.RegionManager;
import com.turt2live.antishare.manager.WorldConfigManager;
import com.turt2live.antishare.regions.Region;
import com.turt2live.antishare.util.Action;
import com.turt2live.antishare.util.GamemodeAbstraction;
import com.turt2live.antishare.util.Messages;
import com.turt2live.antishare.util.PermissionNodes;
import com.turt2live.antishare.util.SelfCompatibility;
import com.turt2live.antishare.util.UpdateChecker;
import com.turt2live.metrics.EMetrics;
import com.turt2live.metrics.tracker.DonutTracker;
import com.turt2live.metrics.tracker.FixedTracker;
import com.turt2live.metrics.tracker.PieTracker;
import com.turt2live.metrics.tracker.Tracker;
import com.turt2live.metrics.tracker.wedge.EnabledDonutWedge;

/**
 * AntiShare plugin class
 * 
 * @author turt2live
 */
public class AntiShare extends PluginWrapper {

	/**
	 * AntiShare instance
	 */
	public static AntiShare p;
	/**
	 * AntiShare tool material
	 */
	public static final Material ANTISHARE_TOOL = Material.BLAZE_ROD;
	/**
	 * AntiShare tool for creating cuboids
	 */
	public static final Material ANTISHARE_CUBOID_TOOL = Material.SLIME_BALL;
	/**
	 * Used to force-set a block
	 */
	public static final Material ANTISHARE_SET_TOOL = Material.BLAZE_POWDER;

	// Trackers
	public static final PieTracker<Action> LEGAL_ACTIONS = new PieTracker<Action>("5.4.0 Legal Actions", Action.values());
	public static final PieTracker<Action> ILLEGAL_ACTIONS = new PieTracker<Action>("5.4.0 Illegal Actions", Action.values());
	public static final PieTracker<Material> TRACKED_MATERIALS = new PieTracker<Material>("5.4.0 Tracked Materials", Material.values());

	@Override
	public void onLoad(){
		for(Material material : Material.values()){
			DonutTracker mainTracker = new DonutTracker(TRACKED_MATERIALS.getGraphName(), material.name());
			EnabledDonutWedge creative = new EnabledDonutWedge("Creative"), survival = new EnabledDonutWedge("Survival"), adventure = new EnabledDonutWedge("Adventure");
			mainTracker.addWedge(creative);
			mainTracker.addWedge(survival);
			mainTracker.addWedge(adventure);
			TRACKED_MATERIALS.replaceTracker(material, mainTracker);
		}
	}

	// Folder locations
	public File generalDataDirectory;
	public File inventoriesDirectory;
	public File blocksDirectory;

	// Files
	private File simpleNoticeFile;

	// Private stuff
	private EMetrics metrics;
	private String build;
	private Messages messages;
	private ASConfig config;
	private HookManager hooks;
	private WorldConfigManager worldConfigs;
	private MoneyManager money;
	private CuboidManager cuboids;
	private BlockManager blocks;
	private InventoryManager inventories;
	private RegionManager regions;
	private final List<String> disabledSNPlayers = new ArrayList<String>();
	private final List<String> disabledTools = new ArrayList<String>();

	@Override
	public void onEnable(){
		p = this;

		// Create folder structure
		generalDataDirectory = new File(getDataFolder(), "data");
		inventoriesDirectory = new File(generalDataDirectory, "inventories");
		blocksDirectory = new File(generalDataDirectory, "block_information");
		if(!getDataFolder().exists()){
			getDataFolder().mkdirs();
		}
		if(!generalDataDirectory.exists()){
			generalDataDirectory.mkdirs();
		}
		if(!inventoriesDirectory.exists()){
			inventoriesDirectory.mkdirs();
		}
		if(!blocksDirectory.exists()){
			blocksDirectory.mkdirs();
		}

		// Setup files
		simpleNoticeFile = new File(generalDataDirectory, "disabled-simplenotice-users.txt");

		// Load disable SimpleNotice users
		try{
			if(simpleNoticeFile.exists()){
				BufferedReader in = new BufferedReader(new FileReader(simpleNoticeFile));
				String line;
				while ((line = in.readLine()) != null){
					disabledSNPlayers.add(line);
				}
				in.close();
			}else{
				simpleNoticeFile.createNewFile();
			}
		}catch(IOException e){
			e.printStackTrace();
		}

		// Check configuration
		getConfig().loadDefaults(getResource("config.yml"));
		if(getConfig().needsUpdate()){
			getConfig().saveDefaults();
		}
		getConfig().load();

		// Get build number
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(getResource("plugin.yml")));
			String line;
			while ((line = in.readLine()) != null){
				if(line.startsWith("build: ")){
					line = line.replace("build: ", "");
					build = line;
					break;
				}
			}
		}catch(IOException e){}

		// Set value in configuration
		String val = getDescription().getVersion() + "|" + getServer().getVersion() + "|" + getServer().getOnlineMode() + "|" + build;
		if(!getConfig().getString("other.version_string", "").equalsIgnoreCase(val)){
			getConfig().set("other.version_string", val);
			saveConfig();
		}

		// Start messages
		messages = new Messages();

		// Start configuration
		config = new ASConfig(getConfig());

		// Start SimpleNotice
		getServer().getMessenger().registerOutgoingPluginChannel(this, "SimpleNotice");

		// Start update checker - internal enabled state
		UpdateChecker.start();

		// Pre-load
		blocks = new BlockManager();
		blocks.load();

		// Cleanup / Compatibility
		SelfCompatibility.convertBlocks();
		SelfCompatibility.migrateWorldConfigurations();
		SelfCompatibility.migratePlayerData();
		SelfCompatibility.convert313Inventories();
		SelfCompatibility.cleanup520Inventories();
		SelfCompatibility.cleanup530Inventories();
		SelfCompatibility.cleanupOldInventories();
		SelfCompatibility.cleanupYAML();
		SelfCompatibility.cleanup520blocks();

		// Start managers
		hooks = new HookManager();
		worldConfigs = new WorldConfigManager();
		money = new MoneyManager();
		cuboids = new CuboidManager();
		inventories = new InventoryManager();
		regions = new RegionManager();

		// Load managers
		hooks.load();
		worldConfigs.load();
		money.load();
		cuboids.load();
		inventories.load();
		regions.load();

		// Start listeners
		getServer().getPluginManager().registerEvents(worldConfigs, this);
		getServer().getPluginManager().registerEvents(new ASListener(), this);

		// Start command stuff
		getCommand("antishare").setExecutor(new CommandHandler());

		// Offline Mode Warning
		if(!getServer().getOnlineMode()){
			getLogger().severe("**********************");
			getLogger().severe(messages.getMessage("offlinemode"));
			getLogger().severe("**********************");
		}

		// Spam console
		getLogger().info(messages.getMessage("enabled"));

		// Load player information
		loadPlayerInformation();

		// Start metrics
		try{
			metrics = new EMetrics(this);
			metrics.addTracker(LEGAL_ACTIONS);
			metrics.addTracker(ILLEGAL_ACTIONS);
			Plugin mcmmo = getServer().getPluginManager().getPlugin("mcMMO");
			metrics.addTracker(new FixedTracker("mcMMO Servers", mcmmo != null ? "Found" : "Not Found"));
			// The PieTracker in this case is just the container
			for(Tracker tracker : TRACKED_MATERIALS.getAllTrackers()){
				metrics.addTracker(tracker);
			}
			metrics.startMetrics();
		}catch(IOException e){ // Metrics error
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable(){
		// Save managers
		blocks.save();
		cuboids.save();
		inventories.save();
		money.save();
		regions.save();

		// Save disabled SimpleNotice users
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(simpleNoticeFile, false));
			for(String user : disabledSNPlayers){
				out.write(user + "\r\n");
			}
			out.close();
		}catch(IOException e){
			e.printStackTrace();
		}

		// Shut down tasks
		getServer().getScheduler().cancelTasks(this);

		// Spam console
		getLogger().info(messages.getMessage("disabled"));
	}

	/**
	 * Reload AntiShare
	 */
	public void reload(){
		reloadConfig();
		config = new ASConfig(getConfig());
		messages.reload();
		hooks.reload();
		worldConfigs.reload();
		cuboids.reload();
		regions.reload();
		inventories.reload();
		blocks.reload();
		loadPlayerInformation();
	}

	private void loadPlayerInformation(){
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){

			@Override
			public void run(){
				for(Player player : getServer().getOnlinePlayers()){
					inventories.loadPlayer(player);
					Region playerRegion = regions.getRegion(player.getLocation());
					if(playerRegion != null){
						playerRegion.alertSilentEntry(player);
					}
					if(player.hasPermission(PermissionNodes.TOOL_USE) && !isToolEnabled(player.getName())){
						messages.sendTo(player, ChatColor.RED + messages.getMessage("tool-disabled"), true);
					}
				}
				if(inventories != null){
					int loaded = inventories.getLoaded();
					if(loaded > 0){
						getLogger().info(messages.getMessage("inventories-loaded", String.valueOf(loaded)));
					}
				}
			}
		});
	}

	/**
	 * Gets the active block manager
	 * 
	 * @return the block manager
	 */
	public BlockManager getBlockManager(){
		return blocks;
	}

	/**
	 * Gets the active inventory manager
	 * 
	 * @return the inventory manager
	 */
	public InventoryManager getInventoryManager(){
		return inventories;
	}

	/**
	 * Gets the active region manager
	 * 
	 * @return the region manager
	 */
	public RegionManager getRegionManager(){
		return regions;
	}

	/**
	 * Gets the active money manager
	 * 
	 * @return the money manager
	 */
	public MoneyManager getMoneyManager(){
		return money;
	}

	/**
	 * Gets the active cuboid manager
	 * 
	 * @return the cuboid manager
	 */
	public CuboidManager getCuboidManager(){
		return cuboids;
	}

	/**
	 * Gets the active world configuration manager
	 * 
	 * @return the world configuration manager
	 */
	public WorldConfigManager getWorldConfigs(){
		return worldConfigs;
	}

	/**
	 * Gets the active hook manager
	 * 
	 * @return the hook manager
	 */
	public HookManager getHookManager(){
		return hooks;
	}

	/**
	 * Gets the AntiShare configuration settings
	 * 
	 * @return the AntiShare configuration settings
	 */
	public ASConfig settings(){
		return config;
	}

	/**
	 * Gets the message handler
	 * 
	 * @return the message handler
	 */
	public Messages getMessages(){
		return messages;
	}

	/**
	 * Determines if a player decided to turn off SimpleNotice support
	 * 
	 * @param name the player name
	 * @return true if enabled (gets messages through SimpleNotice)
	 */
	public boolean isSimpleNoticeEnabled(String name){
		return !disabledSNPlayers.contains(name);
	}

	/**
	 * Enables SimpleNotice support for a user
	 * 
	 * @param name the user
	 */
	public void enableSimpleNotice(String name){
		disabledSNPlayers.remove(name);
	}

	/**
	 * Disables SimpleNotice support for a user
	 * 
	 * @param name the user
	 */
	public void disableSimpleNotice(String name){
		disabledSNPlayers.add(name);
	}

	/**
	 * Determines if a player decided to turn off tool support
	 * 
	 * @param name the player name
	 * @return true if enabled
	 */
	public boolean isToolEnabled(String name){
		return !disabledTools.contains(name);
	}

	/**
	 * Enables tool support for a user
	 * 
	 * @param name the user
	 */
	public void enableTools(String name){
		disabledTools.remove(name);
	}

	/**
	 * Disables tool support for a user
	 * 
	 * @param name the user
	 */
	public void disableTools(String name){
		disabledTools.add(name);
	}

	/**
	 * Gets the AntiShare build number
	 * 
	 * @return the build number
	 */
	public String getBuild(){
		return build;
	}

	/**
	 * Gets the message prefix for AntiShare
	 * 
	 * @return the message prefix
	 */
	public String getPrefix(){
		return messages.getMessage("prefix");
	}

	/**
	 * Determines if a player is blocked from doing something
	 * 
	 * @param player the player
	 * @param allowPermission the "allow" permission
	 * @param denyPermission the "deny" permission
	 * @param material the material applied to the permissions (or null for none)
	 * @return true if blocked
	 */
	public boolean isBlocked(Player player, String allowPermission, String denyPermission, Material material){
		return isBlocked(player, allowPermission, denyPermission, material, false);
	}

	/**
	 * Determines if a player is blocked from doing something
	 * 
	 * @param player the player
	 * @param allowPermission the "allow" permission
	 * @param denyPermission the "deny" permission
	 * @param material the material applied to the permissions (or null for none)
	 * @param specialOnly true to only check permission.[item] permissions
	 * @return true if blocked
	 */
	public boolean isBlocked(Player player, String allowPermission, String denyPermission, Material material, boolean specialOnly){
		return isBlocked(player, allowPermission, denyPermission, material == null ? null : material.name(), specialOnly)
				|| isBlocked(player, allowPermission, denyPermission, String.valueOf(material == null ? null : material.getId()), specialOnly);
	}

	/**
	 * Determines if a player is blocked from doing something
	 * 
	 * @param player the player
	 * @param allowPermission the "allow" permission
	 * @param denyPermission the "deny" permission
	 * @param target the target to apply to this permission, spaces will removed
	 * @return true if blocked
	 */
	public boolean isBlocked(Player player, String allowPermission, String denyPermission, String target){
		return isBlocked(player, allowPermission, denyPermission, target, false);
	}

	/**
	 * Determines if a player is blocked from doing something
	 * 
	 * @param player the player
	 * @param allowPermission the "allow" permission
	 * @param denyPermission the "deny" permission
	 * @param target the target to apply to this permission, spaces will removed
	 * @param specialOnly true to only check permission.[item] permissions
	 * @return true if blocked
	 */
	public boolean isBlocked(Player player, String allowPermission, String denyPermission, String target, boolean specialOnly){
		if(target != null){
			if(player.hasPermission(allowPermission + "." + target)){
				return false;
			}
			if(player.hasPermission(allowPermission + "." + target)){
				return false;
			}
			if(denyPermission != null && player.hasPermission(denyPermission + "." + target)){
				return true;
			}
			if(denyPermission != null && player.hasPermission(denyPermission + "." + target)){
				return true;
			}
		}
		if(specialOnly){
			return false;
		}
		if(player.hasPermission(allowPermission)){
			return false;
		}
		if(denyPermission != null && player.hasPermission(denyPermission)){
			return true;
		}
		if(GamemodeAbstraction.isCreative(player.getGameMode())){
			if(player.hasPermission(PermissionNodes.AFFECT_CREATIVE) || player.hasPermission(PermissionNodes.AFFECT_ADVENTURE)){
				return true;
			}
		}
		if(player.hasPermission(PermissionNodes.AFFECT_CREATIVE) && player.getGameMode() == GameMode.CREATIVE){
			return true;
		}
		if(player.hasPermission(PermissionNodes.AFFECT_SURVIVAL) && player.getGameMode() == GameMode.SURVIVAL){
			return true;
		}
		if(player.hasPermission(PermissionNodes.AFFECT_ADVENTURE) && player.getGameMode() == GameMode.ADVENTURE){
			return true;
		}
		return false;
	}

}
