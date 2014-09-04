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

package com.turt2live.antishare.bukkit.abstraction.vpre;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.bukkit.abstraction.MinecraftVersion;
import com.turt2live.antishare.object.ABlock;
import com.turt2live.antishare.object.attribute.ObjectType;
import com.turt2live.antishare.uuid.UuidService;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.inventory.DoubleChestInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Minecraft implements MinecraftVersion {

    @Override
    public void initialize() {
        // Nothing to do
    }

    @Override
    public Player getPlayerAttacker(Entity damager) {
        if (damager == null) {
            return null;
        } else if (damager instanceof Player) {
            return (Player) damager;
        } else if (damager instanceof Tameable) {
            AnimalTamer tamer = ((Tameable) damager).getOwner();
            if (tamer instanceof Entity) {
                return getPlayerAttacker((Entity) tamer);
            }
        } else if (damager instanceof Projectile) {
            return getPlayerAttacker(((Projectile) damager).getShooter());
        }
        return null;
    }

    @Override
    public List<Material> getContainerTypes() {
        List<Material> list = new ArrayList<Material>();
        list.add(Material.CHEST);
        list.add(Material.LOCKED_CHEST);
        list.add(Material.ENDER_CHEST);
        list.add(Material.ANVIL);
        list.add(Material.WORKBENCH);
        list.add(Material.FURNACE);
        list.add(Material.BURNING_FURNACE);
        list.add(Material.DISPENSER);
        list.add(Material.ENCHANTMENT_TABLE);
        list.add(Material.BREWING_STAND);
        list.add(Material.BEACON);
        return list;
    }

    @Override
    public List<Material> getBrokenOnTop() {
        List<Material> list = new ArrayList<Material>();
        list.add(Material.SAPLING);
        list.add(Material.POWERED_RAIL);
        list.add(Material.DETECTOR_RAIL);
        list.add(Material.LONG_GRASS);
        list.add(Material.DEAD_BUSH);
        list.add(Material.YELLOW_FLOWER);
        list.add(Material.RED_ROSE);
        list.add(Material.BROWN_MUSHROOM);
        list.add(Material.RED_MUSHROOM);
        list.add(Material.TORCH);
        list.add(Material.FIRE);
        list.add(Material.REDSTONE_WIRE);
        list.add(Material.CROPS);
        list.add(Material.SIGN_POST);
        list.add(Material.WOODEN_DOOR);
        list.add(Material.RAILS);
        list.add(Material.WALL_SIGN);
        list.add(Material.LEVER);
        list.add(Material.STONE_PLATE);
        list.add(Material.IRON_DOOR_BLOCK);
        list.add(Material.WOOD_PLATE);
        list.add(Material.REDSTONE_TORCH_OFF);
        list.add(Material.REDSTONE_TORCH_ON);
        list.add(Material.SNOW);
        list.add(Material.CAKE_BLOCK);
        list.add(Material.DIODE_BLOCK_OFF);
        list.add(Material.DIODE_BLOCK_ON);
        list.add(Material.PUMPKIN_STEM);
        list.add(Material.MELON_STEM);
        list.add(Material.WATER_LILY);
        list.add(Material.NETHER_WARTS);
        list.add(Material.TRIPWIRE);
        list.add(Material.CARROT);
        list.add(Material.POTATO);
        return list;
    }

    @Override
    public List<Material> getPistonVanish() {
        List<Material> materials = new ArrayList<Material>();
        materials.add(Material.STATIONARY_WATER);
        materials.add(Material.WATER);
        materials.add(Material.STATIONARY_LAVA);
        materials.add(Material.LAVA);
        materials.add(Material.LONG_GRASS);
        materials.add(Material.DEAD_BUSH);
        materials.add(Material.FIRE);
        materials.add(Material.SNOW);
        materials.add(Material.CAKE_BLOCK);
        return materials;
    }

    @Override
    public List<Material> getPistonBreak() {
        List<Material> materials = new ArrayList<Material>();
        materials.add(Material.SAPLING);
        materials.add(Material.LEAVES);
        materials.add(Material.BED_BLOCK);
        materials.add(Material.WEB);
        materials.add(Material.YELLOW_FLOWER);
        materials.add(Material.RED_ROSE);
        materials.add(Material.BROWN_MUSHROOM);
        materials.add(Material.RED_MUSHROOM);
        materials.add(Material.TORCH);
        materials.add(Material.REDSTONE_WIRE);
        materials.add(Material.CROPS);
        materials.add(Material.WOODEN_DOOR);
        materials.add(Material.LADDER);
        materials.add(Material.LEVER);
        materials.add(Material.STONE_PLATE);
        materials.add(Material.IRON_DOOR_BLOCK);
        materials.add(Material.WOOD_PLATE);
        materials.add(Material.REDSTONE_TORCH_OFF);
        materials.add(Material.REDSTONE_TORCH_ON);
        materials.add(Material.CACTUS);
        materials.add(Material.SUGAR_CANE_BLOCK);
        materials.add(Material.PUMPKIN);
        materials.add(Material.JACK_O_LANTERN);
        materials.add(Material.DIODE_BLOCK_OFF);
        materials.add(Material.DIODE_BLOCK_ON);
        materials.add(Material.TRAP_DOOR);
        materials.add(Material.MELON_BLOCK);
        materials.add(Material.PUMPKIN_STEM);
        materials.add(Material.MELON_STEM);
        materials.add(Material.WATER_LILY);
        materials.add(Material.NETHER_WARTS);
        materials.add(Material.DRAGON_EGG);
        materials.add(Material.COCOA);
        materials.add(Material.TRIPWIRE_HOOK);
        materials.add(Material.TRIPWIRE);
        materials.add(Material.FLOWER_POT);
        materials.add(Material.CARROT);
        materials.add(Material.POTATO);
        materials.add(Material.WOOD_BUTTON);
        materials.add(Material.SKULL);
        return materials;
    }

    @Override
    @SuppressWarnings("deprecation")
    public ABlock.ChestType getChestType(Block block) {
        if (block == null) throw new IllegalArgumentException();

        if (block.getType() == Material.CHEST) {
            BlockState state = block.getState();
            if (state instanceof Chest && ((Chest) state).getInventory() instanceof DoubleChestInventory) {
                return ABlock.ChestType.DOUBLE_NORMAL;
            }
            return ABlock.ChestType.NORMAL;
        } else if (block.getType() == Material.ENDER_CHEST) {
            return ABlock.ChestType.ENDER;
        } else if (block.getType() == Material.LOCKED_CHEST) {
            return ABlock.ChestType.LOCKED;
        }

        return ABlock.ChestType.NONE;
    }

    @Override
    public UUID getUUID(String name) {
        return getUUID(Bukkit.getOfflinePlayer(name));
    }

    @Override
    public UUID getUUID(OfflinePlayer player) {
        if (player == null) throw new IllegalArgumentException("player cannot be null");
        return UuidService.getInstance().doLookup(player.getName()).getUuid();
    }

    @Override
    public String getName(UUID uuid) {
        if (uuid == null) throw new IllegalArgumentException("uuid cannot be null");
        return UuidService.getInstance().doLookup(uuid).getName();
    }

    @Override
    public String getName(OfflinePlayer player) {
        return player.getName();
    }

    @Override
    public ASGameMode toGameMode(GameMode gamemode) {
        if (gamemode == null) throw new IllegalArgumentException("gamemode cannot be null");
        switch (gamemode) {
            case ADVENTURE:
                return ASGameMode.ADVENTURE;
            case SURVIVAL:
                return ASGameMode.SURVIVAL;
            case CREATIVE:
                return ASGameMode.CREATIVE;
            default:
                return null;
        }
    }

    @Override
    public GameMode toGamemode(ASGameMode gamemode) {
        if (gamemode == null) throw new IllegalArgumentException("gamemode cannot be null");
        switch (gamemode) {
            case ADVENTURE:
                return GameMode.ADVENTURE;
            case SURVIVAL:
                return GameMode.SURVIVAL;
            case CREATIVE:
                return GameMode.CREATIVE;
            default:
                return null;
        }
    }

    @Override
    public ObjectType toBlockType(GameMode gamemode) {
        if (gamemode == null) return ObjectType.UNKNOWN;
        switch (gamemode) {
            case ADVENTURE:
                return ObjectType.ADVENTURE;
            case SURVIVAL:
                return ObjectType.SURVIVAL;
            case CREATIVE:
                return ObjectType.CREATIVE;
            default:
                return ObjectType.UNKNOWN;
        }
    }

}
