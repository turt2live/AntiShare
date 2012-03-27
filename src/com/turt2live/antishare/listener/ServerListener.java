package com.turt2live.antishare.listener;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;

public class ServerListener implements Listener {

	private AntiShare plugin;

	public ServerListener(AntiShare plugin){
		this.plugin = plugin;
	}

	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onServerCommand(ServerCommandEvent event){
		String command = event.getCommand().replace("/", "");
		if(command.startsWith("reload") || command.startsWith("rl")){
			warn(event.getSender());
		}
	}

	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onRemoteCommand(RemoteServerCommandEvent event){
		String command = event.getCommand().replace("/", "");
		if(command.startsWith("reload") || command.startsWith("rl")){
			warn(event.getSender());
		}
	}

	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event){
		String command = event.getMessage().replace("/", "");
		if(command.startsWith("reload") || command.startsWith("rl")){
			warn(event.getPlayer());
		}
	}

	private void warn(CommandSender sender){
		plugin.getLogger().severe("****************************");
		plugin.log.severe        ("Reloads may break AntiShare!");
		plugin.getLogger().severe("****************************");
		if(sender instanceof Player){
			ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + (ChatColor.BOLD + "Reloads may break AntiShare!"));
		}
	}
}
