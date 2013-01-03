package com.turt2live.antishare.tekkitcompat;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class SkullCompat {

	private Block block;
	private NotTekkitSkull skull;

	public SkullCompat(Block block){
		this.block = block;
		skull = new NotTekkitSkull(block);
	}

	public String getOwner(){
		if(isSkull(block.getState())){
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
