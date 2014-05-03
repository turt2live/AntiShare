package com.turt2live.antishare.bukkit;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

/**
 * Various material information sources
 *
 * @author turt2live
 */
public class MaterialProvider {

    private static class MaterialInformation {
        public Material material;
        public short data;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MaterialInformation)) return false;

            MaterialInformation that = (MaterialInformation) o;

            if (data != that.data) return false;
            if (material != that.material) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = material != null ? material.hashCode() : 0;
            result = 31 * result + (int) data;
            return result;
        }
    }

    private Map<String, Material> materials = new HashMap<String, Material>();
    private Map<MaterialInformation, String> playerFriendly = new HashMap<MaterialInformation, String>();

    void insertPlayerFriendly(Material material, short data, String name) {
        if (material == null || name == null) return;
        MaterialInformation information = new MaterialInformation();
        information.material = material;
        information.data = data;

        playerFriendly.put(information, name);
    }

    void insertAlias(String alias, Material material) {
        if (alias == null || material == null) return;
        materials.put(alias, material);
    }

    /**
     * Attempts to get the player friendly name for a block
     *
     * @param block the block to lookup. If null, "AIR" is returned
     * @return the player friendly name. never null but will default to the block's type
     */
    public String getPlayerFriendlyName(Block block) {
        if (block == null) return "AIR";

        MaterialInformation info1 = new MaterialInformation(), info2 = new MaterialInformation();
        info1.material = block.getType();
        info1.data = (short) block.getData();
        info2.material = block.getType();
        info2.data = -1;

        String specific = playerFriendly.get(info1);
        String general = playerFriendly.get(info2);
        String def = block.getType().name();

        if (specific != null) return specific;
        if (general != null) return general;
        return def;
    }

    /**
     * Attempts to get a material from a string
     *
     * @param string the string to lookup. If null, AIR is returned
     * @return the material or AIR if not found
     */
    public Material fromString(String string) {
        if (string == null) return Material.AIR;
        if (string.contains(":")) string = string.split(":")[0];

        Material material = Material.matchMaterial(string);
        if (material != null) return material;

        for (Material m : Material.values()) {
            if (m.name().equalsIgnoreCase(string) || m.name().replaceAll("_", " ").equalsIgnoreCase(string)
                    || (m.getId() + "").equalsIgnoreCase(string)) {
                return m;
            }
        }

        Material lookup = materials.get(string.toLowerCase());
        if (lookup != null) return lookup;

        return Material.AIR;
    }

}