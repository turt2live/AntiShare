package com.turt2live.antishare.regions.hooks;

import java.util.Vector;

import org.bukkit.entity.Player;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.regions.ASRegion;
import com.turt2live.antishare.regions.Selection;

public class HookManager {

	private Vector<Hook> hooks = new Vector<Hook>();
	private WorldEditHook worldedit; // Special hook
	private AntiShare plugin;

	public HookManager(AntiShare plugin){
		this.plugin = plugin;
		hooks.add(new WorldGuardHook(plugin));
		worldedit = new WorldEditHook(plugin);
		hooks.add(worldedit);
		//hooks.add(new MobArenaHook(plugin));
	}

	public boolean inRegion(Player player){
		for(Hook hook : hooks){
			if(hook.inRegion(player)){
				return true;
			}
		}
		return false;
	}

	public boolean hasRegion(Selection location){
		for(Hook hook : hooks){
			if(hook.hasRegion(location)){
				return true;
			}
		}
		return false;
	}

	public boolean hasHook(){
		for(Hook hook : hooks){
			if(!hook.exists()){
				return false;
			}
		}
		return true;
	}

	public boolean regionExistsInSelection(Player player){
		return hasRegion(new Selection(worldedit.getSelection(player)));
	}

	public boolean regionExistsInSelectionAndNot(Player player, ASRegion region){
		// Don't check other plugins here as this is strictly AntiShare regions
		return plugin.getRegionHandler().getRegion(worldedit.getSelection(player).getMaximumPoint()) != region;
	}

	public WorldEditHook getWorldEdit(){
		return worldedit;
	}
}
