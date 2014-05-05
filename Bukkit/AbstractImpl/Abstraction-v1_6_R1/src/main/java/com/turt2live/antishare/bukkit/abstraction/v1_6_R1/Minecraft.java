package com.turt2live.antishare.bukkit.abstraction.v1_6_R1;

import org.bukkit.Material;

import java.util.List;

public class Minecraft extends com.turt2live.antishare.bukkit.abstraction.v1_5_R3.Minecraft {

    @Override
    public List<Material> getBrokenOnTop() {
        List<Material> list = super.getBrokenOnTop();
        list.add(Material.CARPET);
        return list;
    }
}
