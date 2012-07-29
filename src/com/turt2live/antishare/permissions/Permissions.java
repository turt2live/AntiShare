/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.permissions;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
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
	private boolean usePEX = false;
	private PEX pex;

	/**
	 * Creates a new permissions handler
	 */
	public Permissions(){
		checkPlugins();
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
		checkPlugins();
		if(usePEX){
			return pex.has(player, permission, world);
		}
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
		if(sender instanceof Player){
			return has((Player) sender, permission, world);
		}else if(sender instanceof ConsoleCommandSender){
			return true;
		}
		return false;
	}

	private void checkPlugins(){
		AntiShare plugin = AntiShare.getInstance();
		Plugin vault = plugin.getServer().getPluginManager().getPlugin("Vault");
		Plugin pex = plugin.getServer().getPluginManager().getPlugin("PermissionsEx");
		if(vault != null){
			handleVault(vault.isEnabled());
			handlePEX(!vault.isEnabled() && pex != null);
		}
		if(pex != null){
			handlePEX(pex.isEnabled());
			handleVault(!pex.isEnabled() && vault != null);
		}
		if(pex == null && vault == null){
			handlePEX(false);
			handleVault(false);
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

	private void handlePEX(boolean use){
		if(use){
			pex = new PEX();
			usePEX = true;
		}else{
			pex = null;
			usePEX = false;
		}
	}

}
