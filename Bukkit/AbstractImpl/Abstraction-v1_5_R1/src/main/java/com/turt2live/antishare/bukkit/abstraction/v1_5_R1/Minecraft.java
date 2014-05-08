package com.turt2live.antishare.bukkit.abstraction.v1_5_R1;

import com.turt2live.antishare.ABlock;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.inventory.DoubleChestInventory;

import java.util.List;

public class Minecraft extends com.turt2live.antishare.bukkit.abstraction.v1_4_R1.Minecraft {

    @Override
    public List<Material> getBrokenOnTop() {
        List<Material> list = super.getBrokenOnTop();
        list.add(Material.GOLD_PLATE);
        list.add(Material.IRON_PLATE);
        list.add(Material.REDSTONE_COMPARATOR_OFF);
        list.add(Material.REDSTONE_COMPARATOR_ON);
        list.add(Material.ACTIVATOR_RAIL);
        return list;
    }

    @Override
    public ABlock.ChestType getChestType(Block block) {
        ABlock.ChestType type = super.getChestType(block);

        if (type == ABlock.ChestType.NONE) {
            if (block.getType() == Material.TRAPPED_CHEST) {
                BlockState state = block.getState();
                if (state instanceof Chest && ((Chest) state).getInventory() instanceof DoubleChestInventory) {
                    return ABlock.ChestType.DOUBLE_TRAPPED;
                }
                return ABlock.ChestType.TRAPPED;
            }
        }

        return type;
    }
}