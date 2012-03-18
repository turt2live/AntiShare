package com.turt2live.antishare;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameModeCommand implements CommandExecutor {

	private AntiShare plugin;

	public GameModeCommand(AntiShare plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		GameMode to = null;
		Player target = null;
		if(command.getName().equalsIgnoreCase("gm") || command.getName().equalsIgnoreCase("gamemode")){
			if(!(sender instanceof Player)){
				if(args.length < 2){
					ASUtils.sendToPlayer(sender, ChatColor.RED + "You don't have a gamemode to change!");
					return true;
				}
			}
			if(args.length < 1){
				target = (Player) sender;
				to = target.getGameMode() == GameMode.CREATIVE ? GameMode.SURVIVAL : GameMode.CREATIVE;
			}else if(args.length == 1){
				target = (Player) sender;
				to = ASUtils.getGameMode(args[0]);
			}else if(args.length == 2){
				target = Bukkit.getPlayer(args[0]);
				to = ASUtils.getGameMode(args[1]);
			}
			if(target == null || to == null){
				return false;
			}
			// Double check permissions
			if(!plugin.getPermissions().has(sender, "AntiShare.gamemode")){
				ASUtils.sendToPlayer(sender, ChatColor.RED + "You cannot do that!");
				return false;
			}
			target.setGameMode(to);
			// Alert the right people
			ASUtils.sendToPlayer(target, "You were changed to " + to.toString().toLowerCase());
			if(!target.getName().equalsIgnoreCase(sender.getName())){
				ASUtils.sendToPlayer(sender, "You changed " + target.getName() + "'s Game Mode to " + to.toString().toLowerCase());
			}
			return true; // All done
		}
		return false;
	}

}
