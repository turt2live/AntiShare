package com.turt2live.antishare.compatibility.other;

import org.bukkit.World;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * Abstract class for PEX
 * 
 * @author turt2live
 */
public class PEXAbstract{

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
		return user.has(permission, world.getName());// || user.has(permission);
	}

}
