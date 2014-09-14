/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.bukkit.dev.check;

import com.turt2live.antishare.bukkit.dev.AntiShare;
import com.turt2live.antishare.bukkit.dev.CheckBase;
import net.minecraft.server.v1_7_R3.Block;
import net.minecraft.server.v1_7_R3.Item;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class MaterialStringsCheck extends CheckBase {

    public MaterialStringsCheck(AntiShare plugin) {
        super(plugin);
    }

    @Override
    public void begin() {
        try {
            File f = new File(plugin.getDataFolder(), "out1.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            String[] values = value.split("\n");
            for (String s : values) {
                s = s.toLowerCase();
                if (!s.startsWith("minecraft:")) s = "minecraft:" + s;
                int id = Block.REGISTRY.b(Block.REGISTRY.a(s));
                if (id == 0) {
                    // Try items
                    id = Item.REGISTRY.b(Item.REGISTRY.a(s));
                }
                Material m = Material.getMaterial(id);
                if (m == null) {
                    Bukkit.broadcastMessage(ChatColor.RED + "Error: Unknown lookup for " + s + " (ID: " + id + ")");
                    continue;
                }
                writer.write("BY_STRING.put(\"" + s + "\", \"" + m.name() + "\");");
                writer.newLine();
                writer.write("BY_MATERIAL.put(\"" + m.name() + "\", \"" + s + "\");");
                writer.newLine();

                if (id == 0) Bukkit.broadcastMessage(ChatColor.YELLOW + "Warning: Air for " + s);
            }
            writer.close();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        Bukkit.broadcastMessage(ChatColor.GREEN + "Done");
    }

    // TODO: Replace with file
    // Sourced from minecraft string ids. Eg: minectraft:air
    public static String value = "AIR\n" +
            "STONE\n" +
            "GRASS\n" +
            "DIRT\n" +
            "COBBLESTONE\n" +
            "PLANKS\n" +
            "SAPLING\n" +
            "BEDROCK\n" +
            "FLOWING_WATER\n" +
            "WATER\n" +
            "FLOWING_LAVA\n" +
            "LAVA\n" +
            "SAND\n" +
            "GRAVEL\n" +
            "GOLD_ORE\n" +
            "IRON_ORE\n" +
            "COAL_ORE\n" +
            "LOG\n" +
            "LEAVES\n" +
            "SPONGE\n" +
            "GLASS\n" +
            "LAPIS_ORE\n" +
            "LAPIS_BLOCK\n" +
            "DISPENSER\n" +
            "SANDSTONE\n" +
            "NOTEBLOCK\n" +
            "BED\n" +
            "GOLDEN_RAIL\n" +
            "DETECTOR_RAIL\n" +
            "STICKY_PISTON\n" +
            "WEB\n" +
            "TALLGRASS\n" +
            "DEADBUSH\n" +
            "PISTON\n" +
            "PISTON_HEAD\n" +
            "WOOL\n" +
            "PISTON_EXTENSION\n" +
            "YELLOW_FLOWER\n" +
            "RED_FLOWER\n" +
            "BROWN_MUSHROOM\n" +
            "RED_MUSHROOM\n" +
            "GOLD_BLOCK\n" +
            "IRON_BLOCK\n" +
            "DOUBLE_STONE_SLAB\n" +
            "STONE_SLAB\n" +
            "BRICK_BLOCK\n" +
            "TNT\n" +
            "BOOKSHELF\n" +
            "MOSSY_COBBLESTONE\n" +
            "OBSIDIAN\n" +
            "TORCH\n" +
            "FIRE\n" +
            "MOB_SPAWNER\n" +
            "OAK_STAIRS\n" +
            "CHEST\n" +
            "REDSTONE_WIRE\n" +
            "DIAMOND_ORE\n" +
            "DIAMOND_BLOCK\n" +
            "CRAFTING_TABLE\n" +
            "WHEAT\n" +
            "FARMLAND\n" +
            "FURNACE\n" +
            "LIT_FURNACE\n" +
            "STANDING_SIGN\n" +
            "WOODEN_DOOR\n" +
            "LADDER\n" +
            "RAIL\n" +
            "STONE_STAIRS\n" +
            "WALL_SIGN\n" +
            "LEVER\n" +
            "STONE_PRESSURE_PLATE\n" +
            "IRON_DOOR\n" +
            "WOODEN_PRESSURE_PLATE\n" +
            "REDSTONE_ORE\n" +
            "LIT_REDSTONE_ORE\n" +
            "UNLIT_REDSTONE_TORCH\n" +
            "REDSTONE_TORCH\n" +
            "STONE_BUTTON\n" +
            "SNOW_LAYER\n" +
            "ICE\n" +
            "SNOW\n" +
            "CACTUS\n" +
            "CLAY\n" +
            "REEDS\n" +
            "JUKEBOX\n" +
            "FENCE\n" +
            "PUMPKIN\n" +
            "NETHERRACK\n" +
            "SOUL_SAND\n" +
            "GLOWSTONE\n" +
            "PORTAL\n" +
            "LIT_PUMPKIN\n" +
            "CAKE\n" +
            "UNPOWERED_REPEATER\n" +
            "POWERED_REPEATER\n" +
            "STAINED_GLASS\n" +
            "TRAPDOOR\n" +
            "MONSTER_EGG\n" +
            "STONEBRICK\n" +
            "BROWN_MUSHROOM_BLOCK\n" +
            "RED_MUSHROOM_BLOCK\n" +
            "IRON_BARS\n" +
            "GLASS_PANE\n" +
            "MELON_BLOCK\n" +
            "PUMPKIN_STEM\n" +
            "MELON_STEM\n" +
            "VINE\n" +
            "FENCE_GATE\n" +
            "BRICK_STAIRS\n" +
            "STONE_BRICK_STAIRS\n" +
            "MYCELIUM\n" +
            "WATERLILY\n" +
            "NETHER_BRICK\n" +
            "NETHER_BRICK_FENCE\n" +
            "NETHER_BRICK_STAIRS\n" +
            "NETHER_WART\n" +
            "ENCHANTING_TABLE\n" +
            "BREWING_STAND\n" +
            "CAULDRON\n" +
            "END_PORTAL\n" +
            "END_PORTAL_FRAME\n" +
            "END_STONE\n" +
            "DRAGON_EGG\n" +
            "REDSTONE_LAMP\n" +
            "LIT_REDSTONE_LAMP\n" +
            "DOUBLE_WOODEN_SLAB\n" +
            "WOODEN_SLAB\n" +
            "COCOA\n" +
            "SANDSTONE_STAIRS\n" +
            "EMERALD_ORE\n" +
            "ENDER_CHEST\n" +
            "TRIPWIRE_HOOK\n" +
            "TRIPWIRE\n" +
            "EMERALD_BLOCK\n" +
            "SPRUCE_STAIRS\n" +
            "BIRCH_STAIRS\n" +
            "JUNGLE_STAIRS\n" +
            "COMMAND_BLOCK\n" +
            "BEACON\n" +
            "COBBLESTONE_WALL\n" +
            "FLOWER_POT\n" +
            "CARROTS\n" +
            "POTATOES\n" +
            "WOODEN_BUTTON\n" +
            "SKULL\n" +
            "ANVIL\n" +
            "TRAPPED_CHEST\n" +
            "LIGHT_WEIGHTED_PRESSURE_PLATE\n" +
            "HEAVY_WEIGHTED_PRESSURE_PLATE\n" +
            "UNPOWERED_COMPARATOR\n" +
            "POWERED_COMPARATOR\n" +
            "DAYLIGHT_DETECTOR\n" +
            "REDSTONE_BLOCK\n" +
            "QUARTZ_ORE\n" +
            "HOPPER\n" +
            "QUARTZ_BLOCK\n" +
            "QUARTZ_STAIRS\n" +
            "ACTIVATOR_RAIL\n" +
            "DROPPER\n" +
            "STAINED_HARDENED_CLAY\n" +
            "STAINED_GLASS_PANE\n" +
            "LEAVES2\n" +
            "LOG2\n" +
            "ACACIA_STAIRS\n" +
            "DARK_OAK_STAIRS\n" +
            "SLIME\n" +
            "BARRIER\n" +
            "IRON_TRAPDOOR\n" +
            "HAY_BLOCK\n" +
            "CARPET\n" +
            "HARDENED_CLAY\n" +
            "COAL_BLOCK\n" +
            "PACKED_ICE\n" +
            "DOUBLE_PLANT\n" +
            "IRON_SHOVEL\n" +
            "IRON_PICKAXE\n" +
            "IRON_AXE\n" +
            "FLINT_AND_STEEL\n" +
            "APPLE\n" +
            "BOW\n" +
            "ARROW\n" +
            "COAL\n" +
            "DIAMOND\n" +
            "IRON_INGOT\n" +
            "GOLD_INGOT\n" +
            "IRON_SWORD\n" +
            "WOODEN_SWORD\n" +
            "WOODEN_SHOVEL\n" +
            "WOODEN_PICKAXE\n" +
            "WOODEN_AXE\n" +
            "STONE_SWORD\n" +
            "STONE_SHOVEL\n" +
            "STONE_PICKAXE\n" +
            "STONE_AXE\n" +
            "DIAMOND_SWORD\n" +
            "DIAMOND_SHOVEL\n" +
            "DIAMOND_PICKAXE\n" +
            "DIAMOND_AXE\n" +
            "STICK\n" +
            "BOWL\n" +
            "MUSHROOM_STEW\n" +
            "GOLDEN_SWORD\n" +
            "GOLDEN_SHOVEL\n" +
            "GOLDEN_PICKAXE\n" +
            "GOLDEN_AXE\n" +
            "STRING\n" +
            "FEATHER\n" +
            "GUNPOWDER\n" +
            "WOODEN_HOE\n" +
            "STONE_HOE\n" +
            "IRON_HOE\n" +
            "DIAMOND_HOE\n" +
            "GOLDEN_HOE\n" +
            "WHEAT_SEEDS\n" +
            "WHEAT\n" +
            "BREAD\n" +
            "LEATHER_HELMET\n" +
            "LEATHER_CHESTPLATE\n" +
            "LEATHER_LEGGINGS\n" +
            "LEATHER_BOOTS\n" +
            "CHAINMAIL_HELMET\n" +
            "CHAINMAIL_CHESTPLATE\n" +
            "CHAINMAIL_LEGGINGS\n" +
            "CHAINMAIL_BOOTS\n" +
            "IRON_HELMET\n" +
            "IRON_CHESTPLATE\n" +
            "IRON_LEGGINGS\n" +
            "IRON_BOOTS\n" +
            "DIAMOND_HELMET\n" +
            "DIAMOND_CHESTPLATE\n" +
            "DIAMOND_LEGGINGS\n" +
            "DIAMOND_BOOTS\n" +
            "GOLDEN_HELMET\n" +
            "GOLDEN_CHESTPLATE\n" +
            "GOLDEN_LEGGINGS\n" +
            "GOLDEN_BOOTS\n" +
            "FLINT\n" +
            "PORKCHOP\n" +
            "COOKED_PORKCHOP\n" +
            "PAINTING\n" +
            "GOLDEN_APPLE\n" +
            "SIGN\n" +
            "WOODEN_DOOR\n" +
            "BUCKET\n" +
            "WATER_BUCKET\n" +
            "LAVA_BUCKET\n" +
            "MINECART\n" +
            "SADDLE\n" +
            "IRON_DOOR\n" +
            "REDSTONE\n" +
            "SNOWBALL\n" +
            "BOAT\n" +
            "LEATHER\n" +
            "MILK_BUCKET\n" +
            "BRICK\n" +
            "CLAY_BALL\n" +
            "REEDS\n" +
            "PAPER\n" +
            "BOOK\n" +
            "SLIME_BALL\n" +
            "CHEST_MINECART\n" +
            "FURNACE_MINECART\n" +
            "EGG\n" +
            "COMPASS\n" +
            "FISHING_ROD\n" +
            "CLOCK\n" +
            "GLOWSTONE_DUST\n" +
            "FISH\n" +
            "COOKED_FISH\n" +
            "DYE\n" +
            "BONE\n" +
            "SUGAR\n" +
            "CAKE\n" +
            "BED\n" +
            "REPEATER\n" +
            "COOKIE\n" +
            "FILLED_MAP\n" +
            "SHEARS\n" +
            "MELON\n" +
            "PUMPKIN_SEEDS\n" +
            "MELON_SEEDS\n" +
            "BEEF\n" +
            "COOKED_BEEF\n" +
            "CHICKEN\n" +
            "COOKED_CHICKEN\n" +
            "ROTTEN_FLESH\n" +
            "ENDER_PEARL\n" +
            "BLAZE_ROD\n" +
            "GHAST_TEAR\n" +
            "GOLD_NUGGET\n" +
            "NETHER_WART\n" +
            "POTION\n" +
            "GLASS_BOTTLE\n" +
            "SPIDER_EYE\n" +
            "FERMENTED_SPIDER_EYE\n" +
            "BLAZE_POWDER\n" +
            "MAGMA_CREAM\n" +
            "BREWING_STAND\n" +
            "CAULDRON\n" +
            "ENDER_EYE\n" +
            "SPECKLED_MELON\n" +
            "SPAWN_EGG\n" +
            "EXPERIENCE_BOTTLE\n" +
            "FIRE_CHARGE\n" +
            "WRITABLE_BOOK\n" +
            "WRITTEN_BOOK\n" +
            "EMERALD\n" +
            "ITEM_FRAME\n" +
            "FLOWER_POT\n" +
            "CARROT\n" +
            "POTATO\n" +
            "BAKED_POTATO\n" +
            "POISONOUS_POTATO\n" +
            "MAP\n" +
            "GOLDEN_CARROT\n" +
            "SKULL\n" +
            "CARROT_ON_A_STICK\n" +
            "NETHER_STAR\n" +
            "PUMPKIN_PIE\n" +
            "FIREWORKS\n" +
            "FIREWORK_CHARGE\n" +
            "ENCHANTED_BOOK\n" +
            "COMPARATOR\n" +
            "NETHERBRICK\n" +
            "QUARTZ\n" +
            "TNT_MINECART\n" +
            "HOPPER_MINECART\n" +
            "IRON_HORSE_ARMOR\n" +
            "GOLDEN_HORSE_ARMOR\n" +
            "DIAMOND_HORSE_ARMOR\n" +
            "LEAD\n" +
            "NAME_TAG\n" +
            "COMMAND_BLOCK_MINECART\n" +
            "RECORD_13\n" +
            "RECORD_CAT\n" +
            "RECORD_BLOCKS\n" +
            "RECORD_CHIRP\n" +
            "RECORD_FAR\n" +
            "RECORD_MALL\n" +
            "RECORD_MELLOHI\n" +
            "RECORD_STAL\n" +
            "RECORD_STRAD\n" +
            "RECORD_WARD\n" +
            "RECORD_11\n" +
            "RECORD_WAIT\n";
}
