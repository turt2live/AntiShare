package com.turt2live.antishare.bukkit.abstraction.v1_7_R1;

import com.turt2live.antishare.ABlock;
import org.bukkit.block.Block;

public class Minecraft extends com.turt2live.antishare.bukkit.abstraction.v1_6_R3.Minecraft {

    @Override
    public ABlock.ChestType getChestType(Block block) {
        ABlock.ChestType type = super.getChestType(block);

        // In-use by stained glass
        if (type == ABlock.ChestType.LOCKED) {
            return ABlock.ChestType.NONE;
        }

        return type;
    }
}
