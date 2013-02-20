/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.permissions;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.compatibility.other.PEX;

/**
 * Permissions handler for AntiShare
 * 
 * @author turt2live
 */
public class Permissions implements Listener {

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

	private void checkPlugins(){
		AntiShare plugin = AntiShare.getInstance();
		Plugin pex = plugin.getServer().getPluginManager().getPlugin("PermissionsEx");
		if(pex != null){
			handlePEX(pex.isEnabled());
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
