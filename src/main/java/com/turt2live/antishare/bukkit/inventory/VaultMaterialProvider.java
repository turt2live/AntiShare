package com.turt2live.antishare.bukkit.inventory;

import net.milkbowl.vault.item.ItemInfo;
import net.milkbowl.vault.item.Items;
import org.bukkit.Material;

/**
 * Material provider for Vault-enabled servers
 *
 * @author turt2live
 */
public class VaultMaterialProvider extends MaterialProvider {

    @Override
    public Material fromString(String string) {
        Material su = super.fromString(string);
        if (string == null) return su;

        if (su == Material.AIR) {
            ItemInfo info = Items.itemByString(string);
            if (info == null || info.getType() == Material.AIR) {
                info = Items.itemByName(string);
                if (info == null || info.getType() == Material.AIR) {
                    return Material.AIR;
                }
            }
            su = info.getType();
        }

        return su;
    }

}
