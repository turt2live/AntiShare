package com.turt2live.antishare;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.feildmaster.lib.configuration.PluginWrapper;
import com.turt2live.antishare.antishare.SQL.SQLManager;

// TODO: Ensuse use of ASUtils.sendToPlayer();
// TODO: Add admin channel, for reporting events
// TODO: ChannelChat implementation for above
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
		if(sender instanceof Player){
			if(((Player) sender).hasPermission("AntiShare.reload")){
				if(cmd.equalsIgnoreCase("antishare") ||
						cmd.equalsIgnoreCase("as") ||
						cmd.equalsIgnoreCase("antis") ||
						cmd.equalsIgnoreCase("ashare")){
					config.reload();
					((Player) sender).sendMessage(ChatColor.GREEN + "AntiShare Reloaded.");
				}
			}
			return false;
		}else{
			if(cmd.equalsIgnoreCase("antishare") ||
					cmd.equalsIgnoreCase("as") ||
					cmd.equalsIgnoreCase("antis") ||
					cmd.equalsIgnoreCase("ashare")){
				reloadConfig();
				log.info("AntiShare Reloaded.");
			}
			return false;
		}
	}

	@Override
	public void onDisable(){
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
		sql = new SQLManager(this);
		if(sql.attemptConnectFromConfig()){
			sql.checkValues();
		}
		log.info("[" + getDescription().getFullName() + "] Enabled! (turt2live)");
	}
}
