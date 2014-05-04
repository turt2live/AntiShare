package com.turt2live.antishare.bukkit.abstraction.v1_5_R1;

import com.turt2live.antishare.ABlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.DoubleChestInventory;

public class Minecraft extends com.turt2live.antishare.bukkit.abstraction.v1_4_R1.Minecraft {

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