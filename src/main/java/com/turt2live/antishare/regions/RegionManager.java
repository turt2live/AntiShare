package com.turt2live.antishare.regions;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;

import com.turt2live.antishare.AntiShare;

public class RegionManager {

	private AntiShare plugin = AntiShare.getInstance();
	private Map<String, Set<Region>> regions = new HashMap<String, Set<Region>>();

	public RegionManager(){
		load();
	}

	public void load(){
		File path = Region.REGION_INFORMATION;
		if(!path.exists()){
			path.mkdirs();
		}
		File[] list = path.listFiles();
		if(list != null){
			for(File file : list){
				if(file.getName().endsWith(".yml")){
					Region region = Region.fromFile(file);
					if(region != null){
						Set<Region> set = new HashSet<Region>();
						if(regions.containsKey(region.getWorldName())){
							set.addAll(regions.get(region.getWorldName()));
						}
						set.add(region);
						regions.put(region.getWorldName(), set);
					}
				}
			}
		}
		if(regions.keySet().size() > 0){
			plugin.getLogger().info("Regions Loaded: " + regions.keySet().size());
		}
	}

	public void save(){
		for(String world : regions.keySet()){
			Set<Region> regions = this.regions.get(world);
			for(Region region : regions){
				region.save();
			}
		}
		regions.clear();
	}

	public void reload(){
		save();
		load();
	}

	public boolean isRegion(Location location){
		String worldname = location.getWorld().getName();
		if(!regions.containsKey(worldname)){
			return false;
		}
		Set<Region> regions = this.regions.get(worldname);
		for(Region region : regions){
			if(region.getCuboid().isContained(location)){
				return true;
			}
		}
		return false;
	}

	public Region getRegion(Location location){
		String worldname = location.getWorld().getName();
		if(!regions.containsKey(worldname)){
			return null;
		}
		Set<Region> regions = this.regions.get(worldname);
		for(Region region : regions){
			if(region.getCuboid().isContained(location)){
				return region;
			}
		}
		return null;
	}

	public Region getRegion(String name){
		for(World world : plugin.getServer().getWorlds()){
			Set<Region> regions = this.regions.get(world.getName());
			for(Region region : regions){
				if(region.getName().equalsIgnoreCase(name)){
					return region;
				}
			}
		}
		return null;
	}

	public void addRegion(Cuboid cuboid, String owner, String name, GameMode gamemode){
		Region region = new Region();
		region.setCuboid(cuboid);
		region.setOwner(owner);
		region.setName(name);
		region.setGameMode(gamemode);
		region.setConfig(new RegionConfiguration(region));
		Set<Region> regions = new HashSet<Region>();
		if(this.regions.containsKey(cuboid.getWorld().getName())){
			regions.addAll(this.regions.get(cuboid.getWorld().getName()));
		}
		regions.add(region);
		this.regions.put(cuboid.getWorld().getName(), regions);
	}

	public void removeRegion(String name){
		for(World world : plugin.getServer().getWorlds()){
			Set<Region> regions = this.regions.get(world.getName());
			Iterator<Region> iterator = regions.iterator();
			while (iterator.hasNext()){
				Region region = iterator.next();
				if(region.getName().equalsIgnoreCase(name)){
					regions.remove(region);
				}
			}
			this.regions.put(world.getName(), regions);
		}
	}

	public boolean isRegionNameTaken(String name){
		return getRegion(name) != null;
	}

	public Set<Region> getAllRegions(World world){
		Set<Region> returnableRegions = new HashSet<Region>();
		if(regions.containsKey(world.getName())){
			returnableRegions.addAll(regions.get(world.getName()));
		}
		return returnableRegions;
	}

	public Set<Region> getAllRegions(){
		Set<Region> returnableRegions = new HashSet<Region>();
		for(World world : plugin.getServer().getWorlds()){
			returnableRegions.addAll(getAllRegions(world));
		}
		return returnableRegions;
	}

	public Set<Region> getAllRegions(GameMode gamemode){
		Set<Region> returnableRegions = new HashSet<Region>();
		for(Region region : getAllRegions()){
			if(region.getGameMode() == gamemode){
				returnableRegions.add(region);
			}
		}
		return returnableRegions;
	}

}
