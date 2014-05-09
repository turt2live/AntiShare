package com.turt2live.antishare.bukkit.abstraction.v1_7_R1;

import com.turt2live.antishare.object.ABlock;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;

public class Minecraft extends com.turt2live.antishare.bukkit.abstraction.v1_6_R3.Minecraft {

    @Override
    public List<Material> getBrokenOnTop() {
        List<Material> list = super.getBrokenOnTop();
        list.add(Material.DOUBLE_PLANT);
        return list;
    }

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
