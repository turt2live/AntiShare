package com.turt2live.antishare.bukkit.inventory;

import org.bukkit.Material;

/**
 * Attempts to get a Material from a supplied string
 *
 * @author turt2live
 */
// TODO: Move this to generalized scope (hurtle?)
public class MaterialProvider {

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

        return Material.AIR;
    }

}
