package com.turt2live.antishare.bukkit;

import com.turt2live.antishare.ASLocation;
import com.turt2live.antishare.engine.BlockTypeList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class DummyBlockTracker implements BlockTypeList {

    @Override
    public boolean isTracked(ASLocation location) {
        Block block = Bukkit.getWorlds().get(0).getBlockAt(location.X, location.Y, location.Z);
        if (block == null || block.getType() == Material.AIR) return false;
        switch (block.getType()) {
            case DIAMOND_BLOCK:
            case GOLD_BLOCK:
            case IRON_BLOCK:
                return true;
            default:
                return false;
        }
    }
}
