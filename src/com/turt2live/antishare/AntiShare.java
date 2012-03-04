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
	private int saveTimerThreadID = -1;

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
			saveTimerThreadID = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
				@Override
				public void run(){
					getServer().dispatchCommand(Bukkit.getConsoleSender(), "as rl");
				}
			}, saveTime, saveTime);
			if(saveTimerThreadID == -1){
				log.severe("[AntiShare] Save thread cannot be created.");
			}
		}
	}

	@Override
	public void onDisable(){
		log.info("[" + getDescription().getFullName() + "] Saving virtual storage to disk/SQL");
		storage.saveToDisk();
		regions.saveStatusToDisk();
		if(sql != null){
			sql.disconnect();
		}
		if(saveTimerThreadID != -1){
			Bukkit.getServer().getScheduler().cancelTask(saveTimerThreadID);
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
						log.info("AntiShare Reloaded.");
						if(sender instanceof Player){
							ASUtils.sendToPlayer(sender, ChatColor.GREEN + "AntiShare Reloaded.");
						}
						new Thread(new Runnable(){
							@Override
							public void run(){
								ASMultiWorld.detectWorlds((AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare"));
							}
						});
						storage.reload(sender);
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have permission!");
					}
					return true;
				}else if(args[0].equalsIgnoreCase("region")){
					if(sender.hasPermission("AntiShare.regions")){
						if(args.length < 2){
							ASUtils.sendToPlayer(sender, ChatColor.RED + "Syntax error, try: /as region <gamemode>");
						}else{
							regions.newRegion(sender, args[1]);
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have permission!");
					}
					return true;
				}else if(args[0].equalsIgnoreCase("rmregion")){
					if(sender.hasPermission("AntiShare.regions")){
						if(!(sender instanceof Player)){
							ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You cannot do that from the console!");
						}else{
							regions.removeRegion(((Player) sender).getLocation(), (Player) sender);
						}
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
