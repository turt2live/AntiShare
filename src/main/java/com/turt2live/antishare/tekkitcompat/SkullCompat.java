package com.turt2live.antishare.tekkitcompat;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class SkullCompat {

	private Block block;
	private NotTekkitSkull skull;

	/**
	 * Creates a new skull compatibility class
	 * 
	 * @param block the skull
	 */
	public SkullCompat(Block block){
		this.block = block;
		skull = new NotTekkitSkull(block);
	}

	/**
	 * Gets the owner of the skull, if any
	 * 
	 * @return the owner of the skull or null if not found/server does not support skulls
	 */
	public String getOwner(){
		if(isSkull(block.getState())){
			return skull.getOwner();
		}
		return null;
	}

	/**
	 * Determines if a particular block state is a skull
	 * 
	 * @param state the block state
	 * @return true if the block state is a skull, false otherwise
	 */
	public static boolean isSkull(BlockState state){
		return state.getClass().getName().contains("org.bukkit") &&
				state.getClass().getName().contains("block") &&
				state.getClass().getName().contains("Skull");
	}

}
