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

package com.turt2live.antishare.utils;

import com.turt2live.antishare.engine.DevEngine;
import com.turt2live.lib.items.AbstractedItem;
import org.jnbt.*;

import java.util.*;

/**
 * Represents an abstract item as an NBT item. Changes made to the item between
 * creation of this object and the call to {@link #getTag(java.lang.String)} are represented in
 * the returned Tag.
 *
 * @author turt2live
 */
public class NBTItem {

    private AbstractedItem item;

    /**
     * Creates a new NBT item
     *
     * @param item the item to represent, cannot be null.
     */
    public NBTItem(AbstractedItem item) {
        if (item == null) throw new IllegalArgumentException();

        this.item = item;
    }

    /**
     * Gets the NBT tag that represents this item
     *
     * @param name an optional name for the tag. If null, a random one will be chosen
     *
     * @return the NBT tag
     */
    public Tag getTag(String name) {
        Map<String, Object> serialized = item.serialize();
        return createCompoundTag(serialized, name);
    }

    private CompoundTag createCompoundTag(Map<String, Object> serialized, String name) {
        Map<String, Tag> tags = new HashMap<String, Tag>();

        for (Map.Entry<String, Object> entry : serialized.entrySet()) {
            Object value = entry.getValue();

            Tag created = createTag(value, entry.getKey());

            if (created != null) {
                tags.put(entry.getKey(), created);
            } else {
                DevEngine.log("Unknown type, cannot convert to an NBT tag: " + entry.getKey() + " = " + value.getClass().getName() + " (" + value + ")");
            }
        }

        return new CompoundTag(name == null ? UUID.randomUUID().toString() : name, tags);
    }

    private Tag createTag(Object value, String name) {
        Tag created = null;

        if (value instanceof Map) {
            // Assume Map<String,Object>
            created = createCompoundTag((Map<String, Object>) value, name);
        } else if (value instanceof Integer) {
            created = new IntTag(name, (Integer) value);
        } else if (value instanceof String) {
            created = new StringTag(name, (String) value);
        } else if (value instanceof Short) {
            created = new ShortTag(name, (Short) value);
        } else if (value instanceof Boolean) {
            created = new StringTag(name, "BOOLEAN|" + value);
        } else if (value instanceof Double) {
            created = new DoubleTag(name, (Double) value);
        } else if (value instanceof List) {
            List<Tag> list = new ArrayList<Tag>();
            List<?> objs = (List<?>) value;
            Class<? extends Tag> tagClass = CompoundTag.class;

            for (Object s : objs) {
                Tag tag = createTag(s, "");
                tagClass = tag.getClass();
                list.add(tag);
            }

            created = new ListTag(name, tagClass, list);
        } else if (value instanceof Enum) {
            Enum e = (Enum) value;

            Map<String, Tag> constants = new HashMap<String, Tag>();
            constants.put("enum_class", new StringTag("ENUM_CLASS", e.getClass().getName()));
            constants.put("enum_value", new StringTag("ENUM_VALUE", e.name()));

            created = new CompoundTag(name, constants);
        }

        return created;
    }

    /**
     * Reconstructs this item from the specified tag
     *
     * @param tag the tag, cannot be null
     *
     * @return the reconstructed item, or null if one could not be made
     */
    public static AbstractedItem reconstructItem(Tag tag) {
        if (tag == null) throw new IllegalArgumentException();

        // TODO

        return null;
    }

}
