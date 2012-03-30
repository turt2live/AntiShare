package com.turt2live.antishare.api;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.turt2live.antishare.regions.ASRegion;
import com.turt2live.antishare.regions.RegionHandler;
import com.turt2live.antishare.regions.RegionManager;

public class RegionAPI extends APIBase {

	private RegionHandler regions;
	private RegionManager manager;

	public RegionAPI(){
		regions = getPlugin().getRegionHandler();
		manager = regions.getManager();
	}

	/**
	 * Creates a gamemode region
	 * 
	 * @param selection the area of the region (WorldEdit selection)
	 * @param owner the owner of the region
	 * @param gamemode the gamemode of the region
	 * @param regionName the region name
	 * @return null if the region creation failed, the region otherwise
	 */
	public ASRegion createRegion(Selection selection, String owner, GameMode gamemode, String regionName){
		if(selection == null || owner == null || gamemode == null || regionName == null){
			return null;
		}
		return manager.newRegion(selection, owner, gamemode, regionName);
	}

	/**
	 * Removes a region
	 * 
	 * @param name the name
	 */
	public void removeRegion(String name){
		manager.removeRegionByName(name);
	}

	/**
	 * Removes a region
	 * 
	 * @param location the location of (or a point within) a region
	 */
	public void removeRegionByLocation(Location location){
		manager.removeRegionAtLocation(location);
	}

	/**
	 * Creates a WorldEdit selection
	 * 
	 * @param point1 the first point
	 * @param point2 the second point
	 * @param world the world
	 * @return the Selection
	 */
	public Selection createSelection(Location point1, Location point2, World world){
		return new CuboidSelection(world, point1, point2);
	}

	/**
	 * Gets region by name
	 * 
	 * @param name the name
	 * @return the region
	 */
	public ASRegion getRegionByName(String name){
		return regions.getRegionByName(name);
	}

	/**
	 * Gets a region by ID
	 * 
	 * @param ID the region ID
	 * @return the region
	 */
	public ASRegion getRegionByID(String ID){
		return regions.getRegionByID(ID);
	}

	/**
	 * Gets a region by location
	 * 
	 * @param location the location
	 * @return the region
	 */
	public ASRegion getRegionByLocation(Location location){
		return regions.getRegion(location);
	}

}
