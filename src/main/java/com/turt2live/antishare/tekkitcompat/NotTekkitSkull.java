package com.turt2live.antishare.tekkitcompat;

import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;

public class NotTekkitSkull {

	private Skull skull = null;

	public NotTekkitSkull(BlockState state){
		if(state instanceof Skull){
			skull = (Skull) state;
		}
	}

	public String getOwner(){
		if(skull != null && skull.hasOwner()){
			return skull.getOwner();
		}
		return null;
	}

}
