package com.turt2live.antishare;

import java.io.File;
import java.util.Vector;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.feildmaster.lib.configuration.PluginWrapper;
import com.turt2live.antishare.SQL.SQLManager;
import com.turt2live.antishare.debug.Debugger;
import com.turt2live.antishare.permissions.ASPermissionsHandler;
import com.turt2live.antishare.regions.ASRegion;
import com.turt2live.antishare.regions.ASRegionHandler;
import com.turt2live.antishare.regions.RegionKey;
import com.turt2live.antishare.storage.VirtualStorage;

public class AntiShare extends PluginWrapper {

	/* TODO: All this
	 * Exp bottle blocks [WAITING ON API]
	 * Conversation Configuration
	 * (if possible) Per-region configuration
	 * Fix 'throw items into regions'
	 * "Ranked Chests" - Permission-ranked chests (LWC?)
	 * No exp gain in creative
	 */

	// TODO: SET TO FALSE BEFORE RELEASE
	public static boolean DEBUG_MODE = true;

	private ASConfig config;
	public static Logger log = Logger.getLogger("Minecraft");
	private SQLManager sql;
	public VirtualStorage storage;
	private ASRegionHandler regions;
	private Conflicts conflicts;
	private Debugger debugger;
	private TimedSave timedSave;
	private ASPermissionsHandler perms;

	@Override
	public void onEnable(){
		config = new ASConfig(this);
		config.create();
		config.reload();
		new File(getDataFolder(), "inventories").mkdirs(); // Setup folders
		ASInventory.cleanup();
		getServer().getPluginManager().registerEvents(new ASListener(this), this);
		ASMultiWorld.detectWorlds(this);
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
		regions = new ASRegionHandler(this);
		debugger = new Debugger(this);
		if(DEBUG_MODE){
			getServer().getPluginManager().registerEvents(debugger, this);
		}
		conflicts = new Conflicts(this);
		perms = new ASPermissionsHandler(this);
		UsageStatistics.send(this);
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

	@Override
	public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args){
		if(cmd.equalsIgnoreCase("antishare") ||
				cmd.equalsIgnoreCase("as") ||
				cmd.equalsIgnoreCase("antis") ||
				cmd.equalsIgnoreCase("ashare")){
			if(args.length > 0){
				if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")){
					if(perms.has(sender, "AntiShare.reload")){
						reloadConfig();
						ASMultiWorld.detectWorlds((AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare"));
						storage.reload(sender);
						log.info("AntiShare Reloaded.");
						if(sender instanceof Player){
							ASUtils.sendToPlayer(sender, ChatColor.GREEN + "AntiShare Reloaded.");
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have permission!");
					}
					return true;
				}else if(args[0].equalsIgnoreCase("region")){
					if(perms.has(sender, "AntiShare.regions")){
						if(args.length < 3){
							ASUtils.sendToPlayer(sender, ChatColor.RED + "Syntax error, try: /as region <gamemode> <name>");
						}else{
							regions.newRegion(sender, args[1], args[2]);
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have permission!");
					}
					return true;
				}else if(args[0].equalsIgnoreCase("rmregion")){
					if(perms.has(sender, "AntiShare.regions")){
						if(args.length > 1){
							regions.removeRegion(args[1], sender);
						}else{
							if(!(sender instanceof Player)){
								ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You have not supplied a name, try /as rmregion <name>");
							}else{
								regions.removeRegion(((Player) sender).getLocation(), (Player) sender);
							}
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have permission!");
					}
					return true;
				}else if(args[0].equalsIgnoreCase("editregion")){
					if(perms.has(sender, "AntiShare.regions")){
						boolean valid = false;
						if(args.length >= 3){
							if(RegionKey.isKey(args[2])){
								if(!RegionKey.requiresValue(RegionKey.getKey(args[2]))){
									valid = true; // we have at least 3 values in args[] and the key does not need a value
								}
							}
						}
						if(args.length >= 4){
							valid = true;
						}
						if(!valid){
							if(args.length >= 2){
								if(args[1].equalsIgnoreCase("help")){
									ASUtils.sendToPlayer(sender, ChatColor.GOLD + "/as editregion <name> <key> <value>");
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "name " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "<any name>");
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "ShowEnterMessage " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "true/false");
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "ShowExitMessage " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "true/false");
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "EnterMessage " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "<enter message>");
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "ExitMessage " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "<exit message>");
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "inventory " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "'none'/'set'");
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "gamemode " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "survival/creative");
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "area " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "No Value");
									ASUtils.sendToPlayer(sender, ChatColor.YELLOW + "'Show____Message'" + ChatColor.WHITE + " - True to show the message");
									ASUtils.sendToPlayer(sender, ChatColor.YELLOW + "'____Message'" + ChatColor.WHITE + " - Use {name} to input the region name.");
									ASUtils.sendToPlayer(sender, ChatColor.YELLOW + "'inventory'" + ChatColor.WHITE + " - Sets the region's inventory. 'none' to not have a default inventory, 'set' to mirror yours");
									ASUtils.sendToPlayer(sender, ChatColor.YELLOW + "'area'" + ChatColor.WHITE + " - Sets the area based on your WorldEdit selection");
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "Incorrect syntax, try: /as editregion <name> <key> <value>");
									ASUtils.sendToPlayer(sender, ChatColor.RED + "For keys and values type /as editregion help");
								}
							}else{
								ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "Incorrect syntax, try: /as editregion <name> <key> <value>");
								ASUtils.sendToPlayer(sender, ChatColor.RED + "For keys and values type /as editregion help");
							}
						}else{
							String name = args[1];
							String key = args[2];
							String value = args[3];
							if(args.length > 4){
								for(int i = 4; i < args.length; i++){ // Starts at args[4]
									value = value + args[i] + " ";
								}
								value = value.substring(0, value.length() - 1);
							}
							if(regions.getRegionByName(name) == null){
								ASUtils.sendToPlayer(sender, ChatColor.RED + "That region does not exist!");
							}else{
								if(RegionKey.isKey(key)){
									regions.editRegion(regions.getRegionByName(name), RegionKey.getKey(key), value, sender);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "That is not a valid region key");
									ASUtils.sendToPlayer(sender, ChatColor.RED + "For keys and values type /as editregion help");
								}
							}
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have permission!");
					}
					return true;
				}else if(args[0].equalsIgnoreCase("listregions")){
					if(perms.has(sender, "AntiShare.regions")){
						int page = 1;
						if(args.length >= 2){
							try{
								page = Integer.parseInt(args[1]);
							}catch(Exception e){
								ASUtils.sendToPlayer(sender, ChatColor.RED + "'" + args[1] + "' is not a number!");
								return true;
							}
						}
						page = Math.abs(page);
						int resultsPerPage = 6; // For ease of changing
						Vector<ASRegion> regions = storage.getAllRegions();
						if(regions == null){

						}
						int maxPages = (int) Math.ceil(regions.size() / resultsPerPage);
						if(maxPages < 1){
							maxPages = 1;
						}
						if(maxPages < page){
							ASUtils.sendToPlayer(sender, ChatColor.RED + "Page " + page + " does not exist! The last page is " + maxPages);
							return true;
						}
						String pagenation = ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "AntiShare Regions " + ChatColor.DARK_GREEN + "|" + ChatColor.GREEN + " Page " + page + "/" + maxPages + ChatColor.DARK_GREEN + " ]=======";
						ASUtils.sendToPlayer(sender, pagenation);
						for(int i = ((page - 1) * resultsPerPage); i < (resultsPerPage < regions.size() ? (resultsPerPage * page) : regions.size()); i++){
							ASUtils.sendToPlayer(sender, ChatColor.DARK_AQUA + "#" + (i + 1) + " " + ChatColor.GOLD + regions.get(i).getName()
									+ ChatColor.YELLOW + " Created By: " + ChatColor.AQUA + regions.get(i).getWhoSet()
									+ ChatColor.YELLOW + " World: " + ChatColor.AQUA + regions.get(i).getWorld().getName());
						}
						ASUtils.sendToPlayer(sender, pagenation);
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have permission!");
					}
					return true;
				}else{
					return false; //Shows usage in plugin.yml
				}
			}
			// Unhandled command (such as /as help, or /as asjkdhgasjdg)
			return false; //Shows usage in plugin.yml
		}
		return false; //Shows usage in plugin.yml
	}

	public ASConfig config(){
		return config;
	}

	public SQLManager getSQLManager(){
		return sql;
	}

	public ASRegionHandler getRegionHandler(){
		return regions;
	}

	public Conflicts getConflicts(){
		return conflicts;
	}

	public Debugger getDebugger(){
		return debugger;
	}

	public ASPermissionsHandler getPermissions(){
		return perms;
	}
}
