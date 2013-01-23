package com.turt2live.antishare.tekkitcompat;

import org.bukkit.Material;
import org.bukkit.entity.Entity;

public class ItemFrameLayer {

	/**
	 * Determines if an entity is an Item Frame. This method is safe for Tekkit use
	 * 
	 * @param entity the entity
	 * @return true if the entity is an item frame, false otherwise
	 */
	public static boolean isItemFrame(Entity entity){
		return EntityLayer.isEntity(entity, "ItemFrame");
	}

	/**
	 * Determines the material for the Item Frame. This is safe for Tekkit use
	 * 
	 * @return the material for Item Frame, or null if not found (like on Tekkit)
	 */
	public static Material getItemFrame(){
		return Material.getMaterial(389);
	}

}
