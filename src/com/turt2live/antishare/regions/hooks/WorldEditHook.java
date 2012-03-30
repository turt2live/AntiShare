package com.turt2live.antishare.regions.hooks;

import java.io.File;

import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.turt2live.antishare.AntiShare;

public class WorldEditHook implements Hook {

	private AntiShare plugin;
	private WorldEditPlugin wePlugin;

	public WorldEditHook(AntiShare plugin){
		this.plugin = plugin;
		wePlugin = (WorldEditPlugin) plugin.getServer().getPluginManager().getPlugin("WorldEdit");
	}

	public Selection getSelection(Player player){
		return wePlugin.getSelection(player);
	}

	public AntiShare getPlugin(){
		return plugin;
	}

	public WorldEditPlugin getWorldEditPlugin(){
		return wePlugin;
	}

	public void clean(){
		File[] listing = new File(plugin.getDataFolder(), "regions").listFiles();
		if(listing != null){
			for(File regionFile : listing){
				regionFile.delete();
			}
		}
	}

	@Override
	public boolean exists(){
		return wePlugin != null;
	}

	@Override
	public boolean inRegion(Player player){
		return false; // Used only by ASRegion on enter
	}

	@Override
	public boolean hasRegion(com.turt2live.antishare.regions.Selection location){
		return false; // WorldEdit has no "regions"
	}

	@Override
	public String getName(){
		return "WorldEdit Hook";
	}
}
