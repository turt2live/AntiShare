/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.permissions;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.turt2live.antishare.AntiShare;

/**
 * Special handling of PermissionsEx
 * 
 * @author turt2live
 */
public class PEX {

	/**
	 * Check if a player has a permission in the world
	 * 
	 * @param player the player
	 * @param permission the permission
	 * @param world the world
	 * @return true if found
	 */
	public boolean has(Player player, String permission, World world){
		PermissionManager perms = PermissionsEx.getPermissionManager();
		PermissionUser user = perms.getUser(player);
		boolean has = user.has(permission, world.getName());
		if(!has){
			// Check defaults
			Permission perm = AntiShare.getInstance().getServer().getPluginManager().getPermission(permission);
			switch (perm.getDefault()){
			case OP:
				has = player.isOp();
				break;
			case NOT_OP:
				has = !player.isOp();
				break;
			case TRUE:
				has = true;
				break;
			case FALSE:
				has = false;
				break;
			}
		}
		return has;
	}

}
