package com.turt2live.antishare.util;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

/**
 * Key class, used for block manager lookups
 * 
 * @author turt2live
 */
public class Key {

	/**
	 * Generates a key class for a location (block)
	 * 
	 * @param location the block location
	 * @param gamemode the gamemode of the block
	 * @return the generated Key object
	 */
	public static Key generate(Location location, GameMode gamemode) {
		return new Key(location.getBlockX(), location.getBlockY(), location.getBlockZ(), gamemode);
	}

	/**
	 * Generates a key class for a location (entity)
	 * 
	 * @param location the location of the entity
	 * @param gamemode the gamemode of the entity
	 * @param entity the entity itself, cannot be null here
	 * @return the generated Key object
	 */
	public static Key generate(Location location, GameMode gamemode, EntityType entity) {
		return new Key(location.getBlockX(), location.getBlockY(), location.getBlockZ(), gamemode, entity);
	}

	/**
	 * X, Y, Z positions
	 */
	public final int x, y, z;
	/**
	 * Gamemode of the object
	 */
	public final GameMode gamemode;
	/**
	 * Entity type, can be null (null means no entity)
	 */
	public final EntityType entity;

	/**
	 * Stores information as a block
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @param gamemode the gamemode of the block
	 */
	public Key(int x, int y, int z, GameMode gamemode) {
		this(x, y, z, gamemode, null);
	}

	/**
	 * Stores information as an entity
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @param gamemode the gamemode of the entity
	 * @param entity the entity itself, cannot be null here
	 */
	public Key(int x, int y, int z, GameMode gamemode, EntityType entity) {
		this.entity = entity;
		this.x = x;
		this.y = y;
		this.z = z;
		this.gamemode = gamemode;
	}

	/**
	 * Converts the position data to a location
	 * 
	 * @param world the world, can be null
	 * @return the location
	 */
	public Location toLocation(World world) {
		return new Location(world, x, y, z);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entity == null) ? 0 : entity.hashCode());
		result = prime * result + ((gamemode == null) ? 0 : gamemode.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Key) {
			Key other = (Key) object;
			return this.x == other.x && this.y == other.y && this.z == other.z && this.gamemode == other.gamemode && this.entity == other.entity;
		}
		return false;
	}
}