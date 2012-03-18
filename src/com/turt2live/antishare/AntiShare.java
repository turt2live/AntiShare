package com.turt2live.antishare;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

import com.feildmaster.lib.configuration.PluginWrapper;
import com.turt2live.antishare.SQL.SQLManager;
import com.turt2live.antishare.debug.Debugger;
import com.turt2live.antishare.permissions.PermissionsHandler;
import com.turt2live.antishare.regions.RegionHandler;
import com.turt2live.antishare.storage.VirtualStorage;

public class AntiShare extends PluginWrapper {

	/* TODO: All this
	 * Fix 'throw items into regions' [WAITING ON... Something?]
	 * Find a work around to get CommandSender from Conversable
	 *      - This is for the Edit Region inventory/area options
	 *      - For Add Region
	 *      - Permissions check on edit region
	 * TNT Creative Explosions
	 * 
	 * TODO: Add these to config helper
	 * [config option] noTNTDrops
	 * [perm] AntiShare.tnt
	 * 
	 * TODO: This is how I (can) fix the item in regions thing
	 * 1) Tag item as it comes out of player
	 * 2) Set a pickup delay of 20 ticks on item
	 * 3) Add chunk to a scheduler list to scan
	 * 4) On scan, check for item
	 * 5) Check item location and stuff
	 * 6) Return item if required
	 */

	// TODO: SET TO FALSE BEFORE RELEASE
	public static boolean DEBUG_MODE = true;

	private Configuration config;
	public static Logger log = Logger.getLogger("Minecraft");
	private SQLManager sql;
	public VirtualStorage storage;
	private RegionHandler regions;
	private Conflicts conflicts;
	private Debugger debugger;
	private TimedSave timedSave;
	private PermissionsHandler perms;

	@Override
	public void onEnable(){
		config = new Configuration(this);
		config.create();
		config.reload();
		new File(getDataFolder(), "inventories").mkdirs(); // Setup folders
		cleanInventoryFolder();
		getServer().getPluginManager().registerEvents(new ASListener(this), this);
		MultiWorld.detectWorlds(this);
		storage = new VirtualStorage(this);
		log.info("[" + getDescription().getFullName() + "] Converting pre-3.0.0 creative blocks...");
		int converted = storage.convertCreativeBlocks();
		log.info("[" + getDescription().getFullName() + "] Converted " + converted + " blocks!");
		if(getConfig().getBoolean("SQL.use")){
			sql = new SQLManager(this);
			if(sql.attemptConnectFromConfig()){
				sql.checkValues();
			}
		}
		String.format("%.2f", 1.22222);
		regions = new RegionHandler(this);
		debugger = new Debugger(this);
		if(DEBUG_MODE){
			getServer().getPluginManager().registerEvents(debugger, this);
		}
		conflicts = new Conflicts(this);
		perms = new PermissionsHandler(this);
		UsageStatistics.send(this);
		getCommand("as").setExecutor(new CommandHandler(this));
		getCommand("gm").setExecutor(new GameModeCommand(this));
		log.info("[" + getDescription().getFullName() + "] Enabled! (turt2live)");
		if(getConfig().getInt("settings.save-interval") > 0){
			int saveTime = (getConfig().getInt("settings.save-interval") * 60) * 20;
			timedSave = new TimedSave(this, saveTime);
		}
	}

	@Override
	public void onDisable(){
		if(timedSave != null){
			timedSave.cancel();
		}
		log.info("[" + getDescription().getFullName() + "] Saving virtual storage to disk/SQL");
		storage.saveToDisk();
		regions.saveStatusToDisk();
		if(sql != null){
			sql.disconnect();
		}
		log.info("[" + getDescription().getFullName() + "] Disabled! (turt2live)");
	}

	public Configuration config(){
		return config;
	}

	public SQLManager getSQLManager(){
		return sql;
	}

	public RegionHandler getRegionHandler(){
		return regions;
	}

	public Conflicts getConflicts(){
		return conflicts;
	}

	public Debugger getDebugger(){
		return debugger;
	}

	public PermissionsHandler getPermissions(){
		return perms;
	}

	public void cleanInventoryFolder(){
		File sdir = new File(getDataFolder(), "inventories");
		String world = Bukkit.getWorlds().get(0).getName();
		if(sdir.exists()){
			for(File f : sdir.listFiles()){
				if(f.getName().endsWith("CREATIVE.yml")
						|| f.getName().endsWith("SURVIVAL.yml")){
					File newName = new File(f.getParent(), f.getName().replace("SURVIVAL", "SURVIVAL_" + world).replace("CREATIVE", "CREATIVE_" + world));
					f.renameTo(newName);
				}
			}
		}
	}
}
