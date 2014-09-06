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

package com.turt2live.antishare.io.flatfile;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.io.generics.GenericInventoryManager;
import com.turt2live.antishare.io.memory.MemoryInventoryManager;
import com.turt2live.antishare.object.AInventory;
import com.turt2live.antishare.object.AWorld;
import com.turt2live.antishare.utils.NBTItem;
import com.turt2live.lib.items.AbstractedItem;
import org.jnbt.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Represents an inventory manager that saves to disk
 *
 * @author turt2live
 */
// TODO: Unit test
public class FileInventoryManager extends GenericInventoryManager {

    private File folder;

    /**
     * Creates a new inventory manager
     *
     * @param folder the folder to store files in. Cannot be null and will be created if it
     *               does not exist. If the folder does exist, it must be a folder and not
     *               any other type of file system object.
     */
    public FileInventoryManager(File folder) {
        if (folder == null) throw new IllegalArgumentException();
        if (folder.exists() && !folder.isDirectory()) throw new IllegalArgumentException("Folder must be a folder");

        this.folder = folder;
        if (!folder.exists()) folder.mkdirs();
    }

    @Override
    protected void saveInventories(Map<UUID, List<AInventory>> inventories) {
        for (UUID player : inventories.keySet()) {
            try {
                List<AInventory> playerInventories = inventories.get(player);
                Tag tag = createTag(playerInventories);

                Map<String, Tag> tags = new HashMap<>();
                tags.put("inventory", tag);

                CompoundTag compoundTag = new CompoundTag(player.toString(), tags);

                File fileName = new File(folder, player.toString() + ".dat");
                NBTOutputStream stream = new NBTOutputStream(new FileOutputStream(fileName, false));
                stream.writeTag(compoundTag);
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<AInventory> getInventories(UUID player) {
        List<AInventory> inventories = new ArrayList<>();

        File expected = new File(folder, player.toString() + ".dat");
        if (expected.exists()) {
            try {
                NBTInputStream inputStream = new NBTInputStream(new FileInputStream(expected));
                Tag baseTag = inputStream.readTag();
                inputStream.close();

                if (baseTag instanceof CompoundTag) {
                    CompoundTag root = (CompoundTag) baseTag;
                    ListTag inventoryList = (ListTag) root.getValue().get("inventories");

                    for (Tag listItem : inventoryList.getValue()) {
                        AInventory inventory = createInventory(((CompoundTag) listItem).getValue());
                        inventories.add(inventory);
                    }
                } else throw new RuntimeException("Root tag is not a valid tag");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return inventories;
    }

    @Override
    protected AInventory createEmptyInventory(UUID player, ASGameMode gamemode, AWorld world) {
        return new MemoryInventoryManager.MemoryInventory(world, gamemode);
    }

    private Tag createTag(List<AInventory> inventories) {
        List<Tag> tags = new ArrayList<>();

        for (AInventory inventory : inventories) {
            tags.add(createTag(inventory));
        }

        return new ListTag("inventories", CompoundTag.class, tags);
    }

    private Tag createTag(AInventory inventory) {
        Map<String, Tag> tags = new HashMap<>();

        tags.put("world", new StringTag("world", inventory.getWorld().getName()));
        tags.put("gamemode", new StringTag("gamemode", inventory.getGameMode().name()));

        Map<String, Tag> items = new HashMap<>();
        for (Map.Entry<Integer, AbstractedItem> content : inventory.getContents().entrySet()) {
            int slot = content.getKey();
            AbstractedItem item = content.getValue();

            Tag itemTag = new NBTItem(item).getTag("slot " + slot);
            items.put("item " + slot, itemTag);
        }

        tags.put("items", new CompoundTag("items", items));

        return new CompoundTag("inventory", tags);
    }

    private AInventory createInventory(Map<String, Tag> tags) {
        ASGameMode gamemode = ASGameMode.valueOf(((StringTag) tags.get("gamemode")).getValue());
        String world = ((StringTag) tags.get("world")).getValue();
        AWorld aWorld = Engine.getInstance().getWorld(world);

        AInventory inventory = new MemoryInventoryManager.MemoryInventory(aWorld, gamemode);

        // Start reading items
        CompoundTag items = (CompoundTag) tags.get("items");
        Map<Integer, AbstractedItem> itemMap = new HashMap<>();
        for (Tag itemTag : items.getValue().values()) {
            int slot = Integer.parseInt(itemTag.getName().substring("slot ".length()));
            AbstractedItem item = NBTItem.reconstructItem(itemTag);
            itemMap.put(slot, item);
        }

        inventory.setContents(itemMap);

        return inventory;
    }
}
