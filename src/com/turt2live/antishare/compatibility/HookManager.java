package com.turt2live.antishare.compatibility;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.turt2live.antishare.AntiShare;

public class HookManager {

	private AntiShare plugin = AntiShare.getInstance();
	private List<SignProtection> signs = new ArrayList<SignProtection>();

	public HookManager(){
		Plugin chestshop = plugin.getServer().getPluginManager().getPlugin("ChestShop");
		if(chestshop != null){
			signs.add(new ChestShop());
		}
		Plugin lwc = plugin.getServer().getPluginManager().getPlugin("lwc");
		if(lwc != null){
			signs.add(new LWC());
		}
		Plugin lockette = plugin.getServer().getPluginManager().getPlugin("Lockette");
		if(lockette != null){
			signs.add(new Lockette());
		}
	}

	public boolean checkSourceBlockForProtection(Block block){
		for(SignProtection protection : signs){
			if(protection.isProtected(block)){
				return true;
			}
		}
		return false;
	}

	public boolean checkSourceBlockForProtectionUsability(Player player, Block block){
		for(SignProtection protection : signs){
			if(protection.canAccess(player, block)){
				return true;
			}
		}
		return false;
	}

}
