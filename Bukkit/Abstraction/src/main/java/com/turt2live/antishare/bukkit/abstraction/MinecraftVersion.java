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

package com.turt2live.antishare.bukkit.abstraction;

import com.turt2live.antishare.object.ABlock;
import com.turt2live.antishare.object.attribute.ASGameMode;
import com.turt2live.antishare.object.attribute.BlockType;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

import java.util.List;
import java.util.UUID;

/**
 * Minecraft version abstraction layer representation
 *
 * @author turt2live
 */
public interface MinecraftVersion {

    /**
     * Gets a list of materials that will break if the block under
     * them goes missing.
     *
     * @return a list of materials
     */
    public List<Material> getBrokenOnTop();

    /**
     * Gets the applicable chest type for a specified block
     *
     * @param block the block, cannot be null
     *
     * @return the applicable chest type, never null
     */
    public ABlock.ChestType getChestType(Block block);

    /**
     * Gets the UUID for the specified name
     *
     * @param name the player name to lookup, cannot be null
     *
     * @return the player's UUID, or null if not found
     */
    public UUID getUUID(String name);

    /**
     * Gets the UUID for the specified player
     *
     * @param player the player to lookup, cannot be null
     *
     * @return the player's UUID, or null if not found
     */
    public UUID getUUID(OfflinePlayer player);

    /**
     * Gets the player name for the specified UUID
     *
     * @param uuid the UUID to lookup, cannot be null
     *
     * @return the player's name, or null if not found
     */
    public String getName(UUID uuid);

    /**
     * Gets the player name for the specified player
     *
     * @param player the player to lookup, cannot be null
     *
     * @return the player's name, or null if not found
     */
    public String getName(OfflinePlayer player);


    /**
     * Converts a Bukkit Game Mode to an AntiShare Game Mode
     *
     * @param gamemode the gamemode to convert
     *
     * @return the AntiShare gamemode. If the passed gamemode was null, this will be null
     */
    public ASGameMode toGameMode(GameMode gamemode);

    /**
     * Converts an AntiShare Game Mode to a Bukkit Game Mode
     *
     * @param gamemode the gamemode to convert
     *
     * @return the Bukkit gamemode. If the passed gamemode was null, this will be null
     */
    public GameMode toGamemode(ASGameMode gamemode);

    /**
     * Converts a Bukkit Game Mode to an AntiShare Block Type
     *
     * @param gamemode the gamemode to convert
     *
     * @return the block type
     */
    public BlockType toBlockType(GameMode gamemode);

}
