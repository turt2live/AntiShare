package com.turt2live.antishare.bukkit.impl;

import com.turt2live.antishare.ABlock;
import com.turt2live.antishare.ASLocation;
import com.turt2live.antishare.AWorld;
import com.turt2live.antishare.bukkit.BukkitUtils;
import org.bukkit.World;

/**
 * Bukkit world
 *
 * @author turt2live
 */
public class BukkitWorld implements AWorld {

    private final World world;

    public BukkitWorld(World world) {
        if (world == null) throw new IllegalArgumentException();

        this.world = world;
    }

    @Override
    public String getName() {
        return world.getName();
    }

    @Override
    public ABlock getBlock(ASLocation location) {
        return new BukkitBlock(world.getBlockAt(BukkitUtils.toLocation(location)));
    }
}