package com.turt2live.antishare.bukkit.impl;

import com.turt2live.antishare.ABlock;
import com.turt2live.antishare.ASLocation;
import com.turt2live.antishare.AWorld;
import com.turt2live.antishare.bukkit.BukkitUtils;
import org.bukkit.block.Block;

/**
 * Bukkit block
 *
 * @author turt2live
 */
public class BukkitBlock implements ABlock {

    private final Block block;

    public BukkitBlock(Block block) {
        if (block == null) throw new IllegalArgumentException();

        this.block = block;
    }

    @Override
    public ASLocation getLocation() {
        return BukkitUtils.toLocation(block.getLocation());
    }

    @Override
    public AWorld getWorld() {
        return getLocation().world;
    }
}
