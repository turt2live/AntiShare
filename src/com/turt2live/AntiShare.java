package com.turt2live;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.feildmaster.lib.configuration.PluginWrapper;

public class AntiShare extends PluginWrapper {

	private ASConfig config;
	public Logger log = Logger.getLogger("Minecraft");

	public ASConfig config(){
		return config;
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
		getServer().getPluginManager().registerEvents(new AntiShareListener(this), this);
		getServer().getPluginManager().registerEvents(new ASMultiWorld(this), this);
		ASMultiWorld.detectWorlds(this);
		log.info("[" + getDescription().getFullName() + "] Enabled! (turt2live)");
	}
}
