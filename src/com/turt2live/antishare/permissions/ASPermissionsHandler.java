package com.turt2live.antishare.permissions;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.turt2live.antishare.AntiShare;

public class ASPermissionsHandler {

	private VaultPermission vault;

	public ASPermissionsHandler(AntiShare plugin){
		if(plugin.getServer().getPluginManager().getPlugin("Vault") != null){
			vault = new VaultPermission(plugin);
		}
	}

	public String[] getGroups(Player player){
		if(vault != null){
			return vault.getGroups(player);
		}else{
			String[] group = new String[1];
			group[0] = player.isOp() ? "OP" : "NON-OP";
			return group;
		}
	}

	public boolean has(Player player, String node, World world){
		if(vault != null){
			return vault.has(player, node, world);
		}else{
			return player.hasPermission(node);
		}
	}

	public boolean has(CommandSender sender, String node){
		if(sender instanceof ConsoleCommandSender){
			return true;
		}
		if(sender instanceof Player){
			return has((Player) sender, node, ((Player) sender).getWorld());
		}
		return false;
	}

	public boolean has(CommandSender sender, String node, World world){
		if(sender instanceof ConsoleCommandSender){
			return true;
		}
		if(sender instanceof Player){
			return has((Player) sender, node, world);
		}
		return false;
	}

	private class VaultPermission {

		private Permission perms;

		public VaultPermission(AntiShare plugin){
			RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
			perms = rsp.getProvider();
		}

		public String[] getGroups(Player player){
			return perms.getPlayerGroups(player);
		}

		public boolean has(OfflinePlayer player, String node, World world){
			try{
				return perms.has(world, player.getName(), node);
			}catch(UnsupportedOperationException e){
				return player.isOp();
			}
		}

	}
}
