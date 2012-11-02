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
public class MVWorlds {

	private static MultiverseCore plugin;

	private static void init(Plugin plugin){
		MVWorlds.plugin = (MultiverseCore) plugin;
	}

	/**
	 * Gets a world alias based on a Bukkit World
	 * 
	 * @param world the world
	 * @return the alias, or the world name if MultiVerse was not found
	 */
	public static String getAlias(World world){
		if(plugin == null){
			Plugin MV = AntiShare.getInstance().getServer().getPluginManager().getPlugin("MultiVerse-Core");
			if(MV == null){
				return world.getName();
			}
			init(MV);
		}
		MultiverseWorld mvworld = plugin.getMVWorldManager().getMVWorld(world);
		return mvworld.getAlias();
	}
}
