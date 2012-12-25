package com.turt2live.antishare.tekkitcompat;

import org.bukkit.block.BlockState;

public class SkullCompat {

	public static String getOwner(BlockState state){
		if(isSkull(state)){
			NotTekkitSkull skull = new NotTekkitSkull(state);
			return skull.getOwner();
		}
		return null;
	}

	public static boolean isSkull(BlockState state){
		return state.getClass().getName().contains("org.bukkit") &&
				state.getClass().getName().contains("block") &&
				state.getClass().getName().contains("Skull");
	}

}
