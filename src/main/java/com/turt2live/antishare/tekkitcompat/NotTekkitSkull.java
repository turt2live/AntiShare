package com.turt2live.antishare.tekkitcompat;

import org.bukkit.block.Block;
import org.bukkit.block.Skull;

/**
 * Wrapper for Skull Meta in craftBukkit. NOT safe for Tekkit use
 */
public class NotTekkitSkull {

	private Skull skull = null;

	/**
	 * Creates a new Not-Tekkit (CraftBukkit) Skull wrapper
	 * @param block the block to wrap
	 */
	public NotTekkitSkull(Block block){
		if(block.getState() instanceof Skull){
			skull = (Skull) block.getState();
		}
	}

	/**
	 * Gets the owner of the skull
	 * @return the owner, or null if not found/error occurred (not a skull, for example)
	 */
	public String getOwner(){
		if(skull != null && skull.hasOwner()){
			return skull.getOwner();
		}
		return null;
	}

}
