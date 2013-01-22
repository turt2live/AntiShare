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
package com.turt2live.antishare.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.compatibility.ChestShop;
import com.turt2live.antishare.compatibility.LWC;
import com.turt2live.antishare.compatibility.Lockette;
import com.turt2live.antishare.compatibility.Towny;
import com.turt2live.antishare.compatibility.type.BlockProtection;
import com.turt2live.antishare.compatibility.type.RegionProtection;

public class HookManager extends AntiShareManager {

	private AntiShare plugin = AntiShare.getInstance();
	private List<BlockProtection> blocks = new ArrayList<BlockProtection>();
	private List<RegionProtection> regions = new ArrayList<RegionProtection>();

	public boolean checkForRegion(Location location){
		for(RegionProtection protection : regions){
			if(protection.isRegion(location)){
				return true;
			}
		}
		return false;
	}

	public boolean checkForRegion(Player player, Block block){
		for(RegionProtection protection : regions){
			if(!protection.isAllowed(player, block)){
				return true;
			}
		}
		return false;
	}

	public boolean checkForSignProtection(Block block){
		return checkForBlockProtection(block);
	}

	public boolean checkForBlockProtection(Block block){
		for(BlockProtection protection : blocks){
			if(protection.isProtected(block)){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean load(){
		// Clear
		blocks.clear();
		regions.clear();

		// Find plugins
		Plugin chestshop = plugin.getServer().getPluginManager().getPlugin("ChestShop");
		if(chestshop != null){
			blocks.add(new ChestShop());
		}
		Plugin lwc = plugin.getServer().getPluginManager().getPlugin("lwc");
		if(lwc != null){
			blocks.add(new LWC());
		}
		Plugin lockette = plugin.getServer().getPluginManager().getPlugin("Lockette");
		if(lockette != null){
			blocks.add(new Lockette());
		}
		Plugin towny = plugin.getServer().getPluginManager().getPlugin("Towny");
		if(towny != null){
			regions.add(new Towny());
		}
		return true;
	}

	@Override
	public boolean save(){
		return true;
	}

}
