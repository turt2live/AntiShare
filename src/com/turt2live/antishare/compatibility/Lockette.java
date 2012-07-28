package com.turt2live.antishare.compatibility;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Lockette extends SignProtection {

	@Override
	public boolean isProtected(Block block){
		return org.yi.acru.bukkit.Lockette.Lockette.isProtected(block);
	}

	@Override
	public boolean canAccess(Player player, Block block){
		if(!isProtected(block)){
			return true;
		}
		if(org.yi.acru.bukkit.Lockette.Lockette.isEveryone(block)){
			return true;
		}else{
			if(org.yi.acru.bukkit.Lockette.Lockette.isOwner(block, player.getName())){
				return true;
			}else if(org.yi.acru.bukkit.Lockette.Lockette.isUser(block, player.getName(), true)){
				return true;
			}
		}
		return false;
	}

}
