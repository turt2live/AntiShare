package com.turt2live.antishare;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.feildmaster.lib.configuration.PluginWrapper;
import com.turt2live.antishare.SQL.SQLManager;
import com.turt2live.antishare.debug.Debugger;
import com.turt2live.antishare.storage.VirtualStorage;
import com.turt2live.antishare.worldedit.ASRegionHandler;
import com.turt2live.antishare.worldedit.RegionKey;

public class AntiShare extends PluginWrapper {

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
		log.info("[" + getDescription().getFullName() + "] Enabled! (turt2live)");
		if(getConfig().getInt("settings.save-interval") > 0){
			int saveTime = (getConfig().getInt("settings.save-interval") * 60 * 1000) / 20;
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
					if(sender.hasPermission("AntiShare.reload")){
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
					if(sender.hasPermission("AntiShare.regions")){
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
					if(sender.hasPermission("AntiShare.regions")){
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
					if(sender.hasPermission("AntiShare.regions")){
						boolean valid = false;
						if(args.length >= 3){
							if(RegionKey.isKey(args[2])){
								if(!RegionKey.requiresValue(RegionKey.getKey(args[2]))){
									valid = true; // we have at least 3 values in args[] and the key does not need a value
								}
							}
						}
						if(!valid){
							if(args.length >= 2){
								if(args[1].equalsIgnoreCase("help")){
									ASUtils.sendToPlayer(sender, ChatColor.GOLD + "/as editregion <name> <key> <value>");
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "name " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "<any name>");
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "ShowEnterMessage " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "true/false");
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "ShowExitMessage " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "true/false");
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "inventory " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "'none'/'set'");
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "gamemode " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "survival/creative");
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "area " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "No Value");
									ASUtils.sendToPlayer(sender, ChatColor.YELLOW + "'ShowEnterMessage'" + ChatColor.WHITE + " - True to show a message when entering a region");
									ASUtils.sendToPlayer(sender, ChatColor.YELLOW + "'ShowExitMessage'" + ChatColor.WHITE + " - True to show a message when leaving a region");
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
					}else if(args[0].equalsIgnoreCase("listregions")){
						// TODO
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
}
