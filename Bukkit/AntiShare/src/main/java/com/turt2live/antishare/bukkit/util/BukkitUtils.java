/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.bukkit.util;

import com.turt2live.antishare.bukkit.impl.BukkitWorld;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.engine.WorldEngine;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.io.BlockManager;
import com.turt2live.antishare.io.EntityManager;
import com.turt2live.antishare.object.ASLocation;
import com.turt2live.antishare.object.AWorld;
import com.turt2live.antishare.object.attribute.Facing;
import com.turt2live.antishare.utils.ASUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;

/**
 * AntiShare to/from Bukkit Utilities
 *
 * @author turt2live
 */
public final class BukkitUtils {

    /**
     * Converts a Bukkit location to an AntiShare location
     *
     * @param location the bukkit location
     *
     * @return the AntiShare location. If the passed location was null, this will be null
     */
    public static ASLocation toLocation(Location location) {
        if (location == null) return null;

        AWorld world = null;
        if (location.getWorld() != null) {
            world = new BukkitWorld(location.getWorld());
        }

        return new ASLocation(world, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    /**
     * Converts an AntiShare location to a Bukkit location. This will NOT apply a world to the location.
     *
     * @param location the AntiShare location
     *
     * @return the Bukkit location. If the passed location was null, this will be null
     */
    public static Location toLocation(ASLocation location) {
        if (location == null) return null;

        return new Location(null, location.X, location.Y, location.Z);
    }

    /**
     * Gets the world engine for the supplied world
     *
     * @param world the world, cannot be null
     *
     * @return the world engine
     */
    public static WorldEngine getWorldEngine(World world) {
        if (world == null) throw new IllegalArgumentException("world cannot be null");

        return Engine.getInstance().getEngine(world.getName());
    }

    /**
     * Gets the block manager for the specified world
     *
     * @param world the world, cannot be null
     *
     * @return the block manager
     */
    public static BlockManager getBlockManager(World world) {
        return getWorldEngine(world).getBlockManager();
    }

    /**
     * Gets the entity manager for the specified world
     *
     * @param world the world, cannot be null
     *
     * @return the entity manager
     */
    public static EntityManager getEntityManager(World world) {
        return getWorldEngine(world).getEntityManager();
    }

    /**
     * Gets the facing direction from a block face
     *
     * @param face the block face direction. Null input is a null return
     *
     * @return the facing direction, or null if not found
     */
    public static Facing getFacing(BlockFace face) {
        if (face == null) return null;
        switch (face) {
            case NORTH:
                return Facing.NORTH;
            case SOUTH:
                return Facing.SOUTH;
            case EAST:
                return Facing.EAST;
            case WEST:
                return Facing.WEST;
            case UP:
                return Facing.UP;
            case DOWN:
                return Facing.DOWN;
            default:
                return null;
        }
    }

    /**
     * Gets the block face direction from a facing direction
     *
     * @param face the facing direction. Null input is a null return
     *
     * @return the block face direction, or null if not found
     */
    public static BlockFace getFacing(Facing face) {
        if (face == null) return null;
        switch (face) {
            case NORTH:
                return BlockFace.NORTH;
            case SOUTH:
                return BlockFace.SOUTH;
            case EAST:
                return BlockFace.EAST;
            case WEST:
                return BlockFace.WEST;
            case UP:
                return BlockFace.UP;
            case DOWN:
                return BlockFace.DOWN;
            default:
                return null;
        }
    }

    /**
     * Gets the string name for the rejection list type in terms of configuration.
     *
     * @param listType the list type to lookup
     *
     * @return the string equivalent, or null if no match
     */
    public static String getStringName(RejectionList.ListType listType) {
        if (listType == null) return null;

        switch (listType) {
            case BLOCK_PLACE:
                return "place";
            case BLOCK_BREAK:
                return "break";
            case COMMANDS:
                return "commands";
            case INTERACTION:
                return "interaction";
            case ITEM_USE:
                return "item-use";
            case ITEM_DROP:
                return "drop";
            case ITEM_PICKUP:
                return "pickup";
            case ENTITY_ATTACK:
                return "entity-attack";
            case ENTITY_BREAK:
                return "entity-break";
            case ENTITY_INTERACT:
                return "entity-interact";
            case ENTITY_PLACE:
                return "entity-place";
            case DEATH:
                return "death";
            case MOB_CREATE:
                return "mob-create";
            default:
                return null;
        }
    }

    /**
     * Gets the player friendly name for an entity type
     *
     * @param entityType the entity type, cannot be null
     *
     * @return the player friendly name
     */
    public static String getPlayerFriendlyName(EntityType entityType) {
        if (entityType == null) throw new IllegalArgumentException();

        return ASUtils.toUpperWords(entityType.getName().replace('_', ' '));
    }
}
