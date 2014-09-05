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

package com.turt2live.antishare.bukkit.util;

import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.collections.ArrayArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses item input for an item stack
 *
 * @author turt2live
 */
// Taken from Hurtle :D
public final class ItemMatcher {

    private enum MetaKey {
        NAME,
        LORE;

        public void apply(String s, ItemStack i) {
            ItemMeta meta = i.getItemMeta();

            switch (this) {
                case NAME:
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', s));
                    break;
                case LORE:
                    meta.setLore(parse(new ArrayArrayList<>(s.split("\\n"))));
                    break;
            }

            i.setItemMeta(meta);
        }

        private List<String> parse(List<String> strings) {
            List<String> parsed = new ArrayList<>();
            for (int i = 0; i < strings.size(); i++) {
                parsed.add(ChatColor.translateAlternateColorCodes('&', strings.get(i)));
            }
            return parsed;
        }

        public static MetaKey match(String s) {
            if (s == null) return null;

            for (MetaKey key : values()) {
                if (key.name().equalsIgnoreCase(s)) return key;
            }

            return null;
        }
    }

    /**
     * Attempts to match the input String to an item stack by creating
     * the item stack from the string. If the syntax cannot be parsed
     * from the input string, or any arguments fail to pass the validation
     * steps in order to create a valid Bukkit ItemStack, this will return
     * null. This will also return null on null input to keep the contract
     * of the inability to parse strings.
     * <br/>
     * The valid syntax for this method is defined as <code>&lt;id&gt;[:data];&lt;amount&gt;[|key=value[|key=value[...]]</code>.
     * Example: For 12 diamonds the input string would be "diamond;12"
     * Example: For 3 wool of data 5 the input string would be "wool:5;3"
     * Example: A dirt block named "lol" (in green) would be the input string "dirt;1|name=&alol"
     * Example: A dirt block with the lore "Hello, son.\nTesting!" would be the input string "dirt;1|lore=Hello, son.\nTesting!"
     * Example: A dirt block named "lol" (in green) with the lore "hello" would have the input string "dirt;1|name=&alol|lore=hello"
     * <br/>
     * Infinite stacks are not supported.
     *
     * @param input the input string, in the defined syntax
     *
     * @return the generated item stack, or null
     */
    public static ItemStack getItem(String input) {
        if (input == null) return null;

        String[] parts = input.split(";", 2);
        if (parts.length != 2) parts = new String[] {parts[0], "1"}; // AntiShare - For semantics
        if (parts.length == 2) {
            Map<MetaKey, String> attributes = new HashMap<>();
            String[] amountParts = parts[1].split("\\|", 2);
            int amount = tryParse(amountParts[0]);
            if (amount <= 0) return null;

            if (amountParts.length > 1) {
                attributes = parse(amountParts[1]);
            }

            parts = parts[0].split(":");
            String itemName = parts[0];

            // AntiShare start - Change material lookup
            Material type = AntiShare.getInstance().getMaterialProvider().fromString(itemName);
            if (type == null || type == Material.AIR) return null;
            // AntiShare end

            short data = 0;
            if (parts.length > 1) data = (short) tryParse(parts[1]);
            if (data < 0) data = 0;

            ItemStack item = new ItemStack(type, amount);
            item.setDurability(data);

            for (Map.Entry<MetaKey, String> attribute : attributes.entrySet()) {
                attribute.getKey().apply(attribute.getValue(), item);
            }

            return item;
        }

        return null;
    }

    private static Map<MetaKey, String> parse(String s) {
        Map<MetaKey, String> parsed = new HashMap<>();

        String[] parts = s.split("\\|");
        for (String part : parts) {
            String[] parts2 = part.split("=", 2);
            if (parts2.length == 2) {
                MetaKey key = MetaKey.match(parts2[0]);
                if (key != null) {
                    parsed.put(key, parts2[1]);
                }
            }
        }

        return parsed;
    }

    private static int tryParse(String in) {
        try {
            int n = Integer.parseInt(in);
            return n;
        } catch (NumberFormatException ignored) {
        }
        return -1;
    }

}
