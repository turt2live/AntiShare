package com.turt2live.antishare.compatibility;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.turt2live.antishare.compatibility.type.SignProtection;

public class LWC extends SignProtection {

	private com.griefcraft.lwc.LWC lwc;

	public LWC(){
		lwc = com.griefcraft.lwc.LWC.getInstance();
	}

	@Override
	public boolean isProtected(Block block){
		return lwc.findProtection(block) != null;
	}

	@Override
	public boolean canAccess(Player player, Block block){
		return lwc.canAccessProtection(player, block);
	}

}
