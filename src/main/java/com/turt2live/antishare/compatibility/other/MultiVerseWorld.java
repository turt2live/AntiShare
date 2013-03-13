/*******************************************************************************
 * Copyright (c) 2013 Travis Ralston.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.compatibility.other;

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
public class MultiVerseWorld{

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
			Plugin multiverse = AntiShare.p.getServer().getPluginManager().getPlugin("MultiVerse-Core");
			if(multiverse == null){
				return world.getName();
			}
			init(multiverse);
		}
		MultiverseWorld mvworld = multiverse.getMVWorldManager().getMVWorld(world);
		return mvworld.getAlias();
	}
}
