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
import com.turt2live.antishare.lib.minidev.json.JSONObject;
import com.turt2live.antishare.lib.minidev.json.JSONValue;
import org.bukkit.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemStackTest extends CheckBase {

    private Player player;

    public ItemStackTest(AntiShare plugin, Player player) {
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
            Object thing = deserialize(parsed);
            if (thing instanceof ItemStack)
                player.getInventory().addItem((ItemStack) thing);
        }

        Bukkit.broadcastMessage(ChatColor.GREEN + "Done!");
    }

    private ConfigurationSerializable deserialize(Map<String, Object> map) {
        Map<String, Object> modifiable = clone(map);

        for (Map.Entry<String, Object> entry : modifiable.entrySet()) {
            if (entry.getValue() instanceof Map) {
                Map<String, Object> clone = deserializeMap(clone((Map<?, ?>) entry.getValue()));
                if (clone.containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY))
                    entry.setValue(deserialize(clone));
            } else if (entry.getValue() instanceof List) {
                entry.setValue(deserializeList((List<?>) entry.getValue()));
            }
        }

        return ConfigurationSerialization.deserializeObject(modifiable);
    }

    private List<Object> deserializeList(List<?> objects) {
        List<Object> deserialized = new ArrayList<Object>();

        for (Object object : objects) {
            if (object instanceof Map) {
                Map<String, Object> clone = clone((Map<?, ?>) object);
                deserializeMap(clone);
                if (clone.containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY))
                    deserialized.add(deserialize(clone((Map<?, ?>) object)));
                else deserialized.add(clone);
            } else if (object instanceof List) {
                List<Object> des = deserializeList((List<?>) object);
                deserialized.add(des);
            } else deserialized.add(object);
        }

        return deserialized;
    }

    private Map<String, Object> deserializeMap(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map) {
                entry.setValue(deserializeMap(clone((Map<?, ?>) entry.getValue())));
            } else if (entry.getValue() instanceof List) {
                entry.setValue(deserializeList((List<?>) entry.getValue()));
            }
        }
        return map;
    }

    private Map<String, Object> serialize(ConfigurationSerializable serializable) {
        Map<String, Object> clone = clone(serializable.serialize());
        clone.put(ConfigurationSerialization.SERIALIZED_TYPE_KEY, ConfigurationSerialization.getAlias(serializable.getClass()));

        for (Map.Entry<String, Object> entry : clone.entrySet()) {
            if (entry.getValue() instanceof ConfigurationSerializable) {
                entry.setValue(serialize((ConfigurationSerializable) entry.getValue()));
            } else if (entry.getValue() instanceof List) {
                List<?> objects = (List<?>) entry.getValue();
                List<Object> serialized = new ArrayList<Object>();
                for (Object o : objects) {
                    if (o instanceof ConfigurationSerializable) {
                        serialized.add(serialize((ConfigurationSerializable) o));
                    } else serialized.add(o);
                }
                entry.setValue(serialized);
            }
        }

        return clone;
    }

    private Map<String, Object> clone(Map<?, ?> map) {
        Map<String, Object> clone = new LinkedHashMap<String, Object>();

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            clone.put(entry.getKey().toString(), entry.getValue());
        }

        return clone;
    }
}
