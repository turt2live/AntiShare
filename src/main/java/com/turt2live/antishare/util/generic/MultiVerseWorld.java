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
package com.turt2live.antishare.util.generic;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.turt2live.antishare.AntiShare;

/**
 * For getting MultiVerse world aliases
 * 
 * @author turt2live
 */
public class MultiVerseWorld {

	private static MultiverseCore multiverse;

	private static void init(Plugin plugin){
		MultiVerseWorld.multiverse = (MultiverseCore) plugin;
	}

	/**
	 * Gets a world alias based on a Bukkit World
	 * 
	 * @param world the world
	 * @return the alias, or the world name if MultiVerse was not found
	 */
	public static String getAlias(World world){
		if(multiverse == null){
			Plugin multiverse = AntiShare.getInstance().getServer().getPluginManager().getPlugin("MultiVerse-Core");
			if(multiverse == null){
				return world.getName();
			}
			init(multiverse);
		}
		MultiverseWorld mvworld = multiverse.getMVWorldManager().getMVWorld(world);
		return mvworld.getAlias();
	}
}
