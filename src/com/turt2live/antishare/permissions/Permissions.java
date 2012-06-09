package com.turt2live.antishare.permissions;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import com.turt2live.antishare.AntiShare;

/**
 * Permissions handler for AntiShare
 * 
 * @author turt2live
 */
public class Permissions implements Listener {

	private boolean useVault = false;
	private VaultPerms vault;

	/**
	 * Creates a new permissions handler
	 */
	public Permissions(){
		AntiShare plugin = AntiShare.getInstance();
		Plugin vault = plugin.getServer().getPluginManager().getPlugin("Vault");
		if(vault != null){
			handleVault(true);
		}
	}

	/**
	 * Determines if a player has a permission
	 * 
	 * @param player the player
	 * @param permission the permission
	 * @return true if the permission is assigned to the player
	 */
	public boolean has(Player player, String permission){
		return has(player, permission, player.getWorld());
	}

	/**
	 * Determines if a player has a permission
	 * 
	 * @param player the player
	 * @param permission the permission
	 * @param world the world to check in
	 * @return true if the permission is assigned to the player
	 */
	public boolean has(Player player, String permission, World world){
		checkVault();
		if(useVault){
			return vault.has(player, permission, world);
		}
		return player.hasPermission(permission);
	}

	/**
	 * Checks if a command sender has a permission
	 * 
	 * @param sender the sender
	 * @param permission the permission
	 * @return true if they have it
	 */
	public boolean has(CommandSender sender, String permission){
		checkVault();
		if(sender instanceof Player){
			return has((Player) sender, permission);
		}else if(sender instanceof ConsoleCommandSender){
			return true;
		}
		return false;
	}

	/**
	 * Determines if a command sender has a permission
	 * 
	 * @param sender the sender
	 * @param permission the permission
	 * @param world the world to check in
	 * @return true if they have it
	 */
	public boolean has(CommandSender sender, String permission, World world){
		checkVault();
		if(sender instanceof Player){
			return has((Player) sender, permission, world);
		}else if(sender instanceof ConsoleCommandSender){
			return true;
		}
		return false;
	}

	private void checkVault(){
		if(useVault){
			if(!vault.isEnabled()){
				handleVault(false);
			}
		}
	}

	private void handleVault(boolean use){
		if(use){
			vault = new VaultPerms();
			useVault = true;
		}else{
			vault = null;
			useVault = false;
		}
	}

	@EventHandler
	public void onPluginEnable(PluginEnableEvent event){
		if(event.getPlugin().getName().equalsIgnoreCase("Vault")){
			handleVault(true);
			AntiShare.getInstance().getMessenger().info("Vault enabled! AntiShare will use Vault for permissions");
		}
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent event){
		if(event.getPlugin().getName().equalsIgnoreCase("Vault")){
			handleVault(false);
			AntiShare.getInstance().getMessenger().info("Vault disabled! AntiShare will use SuperPerms for permissions");
		}
	}
}
