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

package com.turt2live.antishare.bukkit.commands.command;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.commands.ASCommand;
import com.turt2live.antishare.collections.ArrayArrayList;
import com.turt2live.antishare.io.flatfile.FileInventoryManager;
import com.turt2live.antishare.io.memory.MemoryInventoryManager;
import com.turt2live.antishare.lib.items.AbstractedItem;
import com.turt2live.antishare.lib.items.bukkit.BukkitAbstractItem;
import com.turt2live.antishare.object.AInventory;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.*;

public class TestCommand implements ASCommand {

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean isPlayersOnly() {
        return false;
    }

    @Override
    public String getUsage() {
        return "/as test";
    }

    @Override
    public String getDescription() {
        return "test command";
    }

    @Override
    public String[] getAlternatives() {
        return new String[] {"test"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        createItems();
        File folder = AntiShare.getInstance().getDataFolder().getParentFile();
        FileInventoryManager invMan = new FileInventoryManager(folder);

        List<AbstractedItem> items = new ArrayList<AbstractedItem>();
        for (ItemStack item : this.items) {
            items.add(new BukkitAbstractItem(item));
        }

        Random random = new Random();
        List<AInventory> inventories = new ArrayList<AInventory>();
        for (int i = 0; i < 6; i++) {
            for (ASGameMode gamemode : ASGameMode.values()) {
                AInventory inventory = new MemoryInventoryManager.MemoryInventory("world" + i, gamemode);

                Map<Integer, AbstractedItem> contents = new HashMap<Integer, AbstractedItem>();
                for (AbstractedItem abi : items) {
                    int slot;
                    do {
                        slot = random.nextInt(30);
                    } while (contents.containsKey(slot));

                    contents.put(slot, abi);
                }

                inventory.setContents(contents);

                inventories.add(inventory);
            }
        }

        UUID player = UUID.randomUUID();
        for (AInventory i : inventories) {
            invMan.setInventory(player, i);
        }

        invMan.save(player);

        // Re-load items (for testing)
        for (int i = 0; i < 6; i++) {
            for (ASGameMode gamemode : ASGameMode.values()) {
                AInventory inventory = invMan.getInventory(player, gamemode, "world" + i);
                if (inventory == null) throw new RuntimeException("null inv");
                else {
                    // Check contents
                    for (AbstractedItem item : inventory.getContents().values()) {
                        ItemStack itemStack = ((BukkitAbstractItem) item).getItemStack();

                        boolean similar = false;
                        for (ItemStack itemStack1 : this.items) {
                            if (itemStack1.isSimilar(itemStack)) {
                                similar = true;
                                break;
                            }
                        }

                        if (!similar)
                            System.out.println("Item " + itemStack.getType() + " is NOT okay in world" + i + ", gamemode " + gamemode);
                    }
                }
            }
        }


        sender.sendMessage("Done");
        return true;
    }

    private ItemStack[] items = new ItemStack[0];

    private void createItems() {
        List<ItemStack> items = new ArrayList<ItemStack>();

        // Book
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        bookMeta.setAuthor("test_author");
        bookMeta.setTitle("test title");
        bookMeta.setPages("This is a test page", "this is also a test page", "this is the last test page");
        book.setItemMeta(bookMeta);
        items.add(book);

        // Colored
        ItemStack colored = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta coloredMeta = (LeatherArmorMeta) colored.getItemMeta();
        coloredMeta.setColor(Color.fromRGB(10, 125, 88));
        colored.setItemMeta(coloredMeta);
        items.add(colored);

        // Enchantment Storage
        ItemStack enchStore = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta enchStoreMeta = (EnchantmentStorageMeta) enchStore.getItemMeta();
        enchStoreMeta.addStoredEnchant(Enchantment.OXYGEN, 5, true);
        enchStoreMeta.addStoredEnchant(Enchantment.ARROW_FIRE, 6, true);
        enchStore.setItemMeta(enchStoreMeta);
        items.add(enchStore);

        // Firework
        ItemStack firework = new ItemStack(Material.FIREWORK);
        FireworkMeta fwMeta = (FireworkMeta) firework.getItemMeta();
        fwMeta.setPower(10);
        fwMeta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.CREEPER).withColor(Color.fromRGB(19, 124, 66)).withFlicker().build());
        fwMeta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.fromRGB(19, 124, 66)).withFade(Color.fromRGB(10, 13, 14), Color.AQUA).withFlicker().build());
        firework.setItemMeta(fwMeta);
        items.add(firework);

        // Firework Effect
        ItemStack fwEffect = new ItemStack(Material.FIREWORK_CHARGE);
        FireworkEffectMeta fweMeta = (FireworkEffectMeta) fwEffect.getItemMeta();
        fweMeta.setEffect(FireworkEffect.builder().with(FireworkEffect.Type.STAR).withColor(Color.fromRGB(66, 66, 66)).withFade(Color.fromRGB(10, 13, 14), Color.AQUA).withTrail().withFlicker().build());
        fwEffect.setItemMeta(fweMeta);
        items.add(fwEffect);

        // Map
        ItemStack map = new ItemStack(Material.MAP);
        MapMeta mapMeta = (MapMeta) map.getItemMeta();
        mapMeta.setScaling(true);
        map.setItemMeta(mapMeta);
        items.add(map);

        // Potion
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        potionMeta.setMainEffect(PotionEffectType.BLINDNESS);
        potionMeta.addCustomEffect(PotionEffectType.BLINDNESS.createEffect(2000, 10), true);
        potionMeta.addCustomEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(1234, 5678), true);
        potion.setItemMeta(potionMeta);
        items.add(potion);

        // Skull
        ItemStack skull = new ItemStack(Material.SKULL_ITEM);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwner("turt2live");
        skull.setItemMeta(skullMeta);
        items.add(skull);

        // Standard Item
        ItemStack standard = new ItemStack(Material.DIAMOND);
        ItemMeta stdMeta = standard.getItemMeta();
        stdMeta.addEnchant(Enchantment.ARROW_FIRE, 10, true);
        stdMeta.addEnchant(Enchantment.OXYGEN, 10, true);
        stdMeta.setDisplayName(ChatColor.RED + "This is a display name");
        stdMeta.setLore(new ArrayArrayList<String>(ChatColor.GREEN + "First lore line", ChatColor.LIGHT_PURPLE + "Second lore line"));
        standard.setItemMeta(stdMeta);
        items.add(standard);

        // Textured
        ItemStack textured = new ItemStack(Material.WOOD);
        textured.setDurability((short) 3);
        items.add(textured);

        this.items = items.toArray(new ItemStack[items.size()]);
    }
}
