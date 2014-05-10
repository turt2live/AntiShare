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
    public List<Material> getPistonBreak() {
        List<Material> materials = super.getPistonBreak();
        materials.add(Material.LEAVES_2);
        materials.add(Material.DOUBLE_PLANT);
        return materials;
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
