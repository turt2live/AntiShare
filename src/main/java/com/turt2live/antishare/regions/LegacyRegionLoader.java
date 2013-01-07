package com.turt2live.antishare.regions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.cuboid.Cuboid;

public class LegacyRegionLoader {

	public static void loadSQL(RegionManager regionManager, String worldname){
		AntiShare plugin = AntiShare.getInstance();
		try{
			ResultSet results = plugin.getSQL().get("SELECT * FROM `AS_Regions`");
			if(results != null){
				while (results.next()){
					World world = plugin.getServer().getWorld(results.getString("world"));
					Location minimum = new Location(world,
							results.getDouble("mix"),
							results.getDouble("miy"),
							results.getDouble("miz"));
					Location maximum = new Location(world,
							results.getDouble("max"),
							results.getDouble("may"),
							results.getDouble("maz"));
					String setBy = results.getString("creator");
					GameMode gamemode = GameMode.valueOf(results.getString("gamemode"));
					String name = results.getString("regionName");
					boolean enterMessage = results.getInt("showEnter") == 1;
					boolean exitMessage = results.getInt("showExit") == 1;
					Region region = new Region();
					region.setWorld(world);
					region.setEnterMessage(results.getString("enterMessage"));
					region.setExitMessage(results.getString("exitMessage"));
					region.setName(name);
					region.setID(results.getString("uniqueID"));
					region.setShowEnterMessage(enterMessage);
					region.setShowExitMessage(exitMessage);
					Cuboid cuboid = new Cuboid(minimum, maximum);
					region.setCuboid(cuboid);
					region.setOwner(setBy);
					region.setGameMode(gamemode);

					// Inventory is set when the inventory manager loads
					regionManager.inject(region);
				}
			}
		}catch(SQLException e){
			AntiShare.getInstance().log("AntiShare encountered and error. Please report this to turt2live.", Level.SEVERE);
			e.printStackTrace();
		}
	}

	public static void loadSQL(RegionManager regionManager){
		for(World world : Bukkit.getWorlds()){
			loadSQL(regionManager, world.getName());
		}
	}

}
