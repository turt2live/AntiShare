package com.turt2live.antishare.tekkitcompat;

import org.bukkit.block.Block;
import org.bukkit.block.Skull;

public class NotTekkitSkull {

	private Skull skull = null;

	public NotTekkitSkull(Block block){
		if(block.getState() instanceof Skull){
			skull = (Skull) block.getState();
		}
	}

	public String getOwner(){
		if(skull != null && skull.hasOwner()){
			return skull.getOwner();
		}
		return null;
	}

}
