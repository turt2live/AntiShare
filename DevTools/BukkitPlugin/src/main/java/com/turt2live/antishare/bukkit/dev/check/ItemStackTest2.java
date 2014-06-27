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

package com.turt2live.antishare.bukkit.dev.check;

import com.turt2live.antishare.bukkit.dev.AntiShare;
import com.turt2live.antishare.bukkit.dev.CheckBase;
import com.turt2live.antishare.collections.ArrayArrayList;
import com.turt2live.antishare.lib.items.AbstractedItem;
import com.turt2live.antishare.lib.items.ProviderManager;
import com.turt2live.antishare.lib.items.bukkit.BukkitAbstractItem;
import com.turt2live.antishare.lib.items.serialization.ItemSerializer;
import com.turt2live.antishare.lib.minidev.json.JSONObject;
import com.turt2live.antishare.lib.minidev.json.JSONValue;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Map;

public class ItemStackTest2 extends CheckBase {

    private Player player;

    public ItemStackTest2(AntiShare plugin, Player player) {
        super(plugin);
        this.player = player;
    }

    @Override
    public void begin() {
        // Item setup
        Bukkit.broadcastMessage(ChatColor.AQUA + "Item setup");
        ItemStack basic = new ItemStack(Material.DIRT);
        ItemStack enchanted = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack lore = new ItemStack(Material.WOOD);
        ItemStack firework = new ItemStack(Material.FIREWORK);
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        ItemStack wat = new ItemStack(Material.LEATHER_CHESTPLATE);

        // Setup item meta for various items
        Bukkit.broadcastMessage(ChatColor.AQUA + "Setting item meta");
        enchanted.addEnchantment(Enchantment.DAMAGE_ALL, 2);

        ItemMeta meta = lore.getItemMeta();
        meta.setLore(new ArrayArrayList<String>(ChatColor.GOLD + "Colored lore", ChatColor.YELLOW + "is cool"));
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "{\"break\":\"things\"}");
        lore.setItemMeta(meta);

        meta = firework.getItemMeta();
        FireworkMeta effect = (FireworkMeta) meta;
        effect.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.CREEPER).withColor(Color.AQUA).flicker(true).build());
        firework.setItemMeta(effect);

        meta = book.getItemMeta();
        BookMeta pages = (BookMeta) meta;
        pages.addPage("This", "is", "a", "Page");
        pages.addPage("[1,2,3,4]");
        pages.setAuthor("{\"worst\":\"author\"}");
        pages.setTitle("ima book");
        book.setItemMeta(pages);

        meta = wat.getItemMeta();
        LeatherArmorMeta armor = (LeatherArmorMeta) meta;
        for (Enchantment enchant : Enchantment.values()) {
            wat.addUnsafeEnchantment(enchant, 1);
        }
        wat.setDurability((short) 12);
        armor.setColor(Color.fromRGB(12, 60, 34));
        meta.setDisplayName(ChatColor.RED + "stuff");
        wat.setItemMeta(meta);

        // Give items to player
        player.getInventory().clear();
        player.getInventory().addItem(basic, enchanted, lore, firework, book, wat);

        Bukkit.broadcastMessage(ChatColor.AQUA + "Serializing");
        // Serialize to JSON
        ItemStack[] stacks = new ItemStack[] {basic, enchanted, lore, firework, book, wat};
        String[] jsons = new String[stacks.length];

        int n = 0;
        for (ItemStack stack : stacks) {
            Map<String, Object> fullySerialized = serialize(stack);
            String json = new JSONObject(fullySerialized).toJSONString();
            jsons[n] = json;
            n++;
        }

        Bukkit.broadcastMessage(ChatColor.AQUA + "Deserializing");
        // Deserialize from JSON
        for (String json : jsons) {
            Map<String, Object> parsed = ((JSONObject) JSONValue.parse(json));
            ItemStack thing = deserialize(parsed);
            if (thing != null) player.getInventory().addItem(thing);
        }

        Bukkit.broadcastMessage(ChatColor.GREEN + "Done!");
    }

    private ItemStack deserialize(Map<String, Object> map) {
        try {
            AbstractedItem item = ProviderManager.getInstance().deserialize(map);

            if (item instanceof BukkitAbstractItem) {
                return ((BukkitAbstractItem) item).getItemStack();
            }
        } catch (ItemSerializer.InvalidItemException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Map<String, Object> serialize(ItemStack item) {
        AbstractedItem abstracted = new BukkitAbstractItem(item);

        try {
            return ProviderManager.getInstance().serialize(abstracted);
        } catch (ItemSerializer.InvalidItemException e) {
            e.printStackTrace();
        }

        return null;
    }
}
