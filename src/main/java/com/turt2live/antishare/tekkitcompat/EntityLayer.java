package com.turt2live.antishare.tekkitcompat;

import org.bukkit.entity.Entity;

public class EntityLayer {

	/**
	 * Determines if an entity is of a specific type
	 * 
	 * @param entity the entity
	 * @param name the entity type (name of class)
	 * @return true if there is a match, false otherwise
	 */
	public static boolean isEntity(Entity entity, String name){
		return entity.getClass().getName().contains("org.bukkit") &&
				entity.getClass().getName().contains("entity") &&
				entity.getClass().getName().contains(name);
	}

}
