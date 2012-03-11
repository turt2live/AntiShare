package com.turt2live.antishare;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.feildmaster.lib.configuration.EnhancedConfiguration;

public class MultiWorld {

	public static void detectWorlds(AntiShare plugin){
		for(World w : Bukkit.getWorlds()){
			String worldName = w.getName();
			File worldConfig = new File(plugin.getDataFolder(), worldName + "_config.yml");
			EnhancedConfiguration worldSettings = new EnhancedConfiguration(worldConfig, plugin);
			worldSettings.loadDefaults(plugin.getResource("resources/world.yml"));
			if(!worldSettings.fileExists() || !worldSettings.checkDefaults()){
				worldSettings.saveDefaults();
			}
			worldSettings.load();
		}
	}

	// Returns allowance of worldSwap
	public static boolean worldSwap(AntiShare plugin, Player player, Location from, Location to){
		if(plugin.getPermissions().has(player, "AntiShare.worlds", to.getWorld())){
			return true;
		}
		if(plugin.getConflicts().WORLD_MANAGER_CONFLICT_PRESENT){
			return true;
		}
		World worldTo = to.getWorld();
		boolean transfers = plugin.config().getBoolean("other.worldTransfer", worldTo);
		boolean creative = plugin.config().getBoolean("other.only_if_creative", worldTo);
		if(transfers){
			return true;
		}else{
			if(creative){
				if(player.getGameMode() == GameMode.CREATIVE){
					return false;
				}else{
					return true;
				}
			}else{
				return false;
			}
		}
	}
}
