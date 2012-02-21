package com.turt2live.antishare;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.feildmaster.lib.configuration.PluginWrapper;
import com.turt2live.antishare.SQL.SQLManager;

public class AntiShare extends PluginWrapper {

	private ASConfig config;
	public Logger log = Logger.getLogger("Minecraft");
	private SQLManager sql;

	public ASConfig config(){
		return config;
	}

	public SQLManager getSQLManager(){
		return sql;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args){
		if(cmd.equalsIgnoreCase("antishare") ||
				cmd.equalsIgnoreCase("as") ||
				cmd.equalsIgnoreCase("antis") ||
				cmd.equalsIgnoreCase("ashare")){
			reloadConfig();
			log.info("AntiShare Reloaded.");
			ASUtils.sendToPlayer(sender, ChatColor.GREEN + "AntiShare Reloaded.");
			return true;
		}else if(cmd.equalsIgnoreCase("astest")){
			new Thread(new Runnable(){
				@Override
				public void run(){
					while (true){
						for(Player p : Bukkit.getOnlinePlayers()){
							if(p.getGameMode() == GameMode.SURVIVAL){
								p.setGameMode(GameMode.CREATIVE);
							}else{
								p.setGameMode(GameMode.SURVIVAL);
							}
						}
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lag");
					}
				}
			}).start();
		}
		return false;
	}

	@Override
	public void onDisable(){
		if(sql != null){
			sql.disconnect();
		}
		log.info("[" + getDescription().getFullName() + "] Disabled! (turt2live)");
	}

	@Override
	public void onEnable(){
		config = new ASConfig(this);
		config.create();
		config.reload();
		ASInventory.cleanup();
		getServer().getPluginManager().registerEvents(new AntiShareListener(this), this);
		getServer().getPluginManager().registerEvents(new ASMultiWorld(this), this);
		ASMultiWorld.detectWorlds(this);
		if(getConfig().getBoolean("SQL.use")){
			sql = new SQLManager(this);
			if(sql.attemptConnectFromConfig()){
				sql.checkValues();
			}
		}
		log.info("[" + getDescription().getFullName() + "] Enabled! (turt2live)");
	}
}
