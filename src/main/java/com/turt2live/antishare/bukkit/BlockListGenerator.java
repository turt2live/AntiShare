package com.turt2live.antishare.bukkit;

import com.turt2live.antishare.ASLocation;
import com.turt2live.antishare.bukkit.inventory.MaterialProvider;
import com.turt2live.antishare.engine.BlockTypeList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a block list from a list
 *
 * @author turt2live
 */
public final class BlockListGenerator implements BlockTypeList {

    private static class BInfo {
        final Material material;
        final short damage;

        BInfo(Material m, short d) {
            material = m;
            damage = d;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BInfo bInfo = (BInfo) o;

            if (damage != bInfo.damage) return false;
            if (material != bInfo.material) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = material.hashCode();
            result = 31 * result + (int) damage;
            return result;
        }

        @Override
        public String toString() {
            return "BInfo{" +
                    "material=" + material +
                    ", damage=" + damage +
                    '}';
        }
    }

    private List<BInfo> information = new ArrayList<BInfo>();
    private String world;

    private BlockListGenerator(String world) {
        this.world = world;
    }

    @Override
    public boolean isTracked(ASLocation location) {
        World world = Bukkit.getWorld(this.world);
        if (world != null) {
            Block block = world.getBlockAt(BukkitUtils.toLocation(location));
            if (block != null) {
                Material material = block.getType();
                if (material != Material.AIR) {
                    byte data = block.getData();

                    BInfo search = new BInfo(material, (short) data);
                    if (information.contains(search)) return true;

                    data = -1;
                    search = new BInfo(material, (short) data);
                    if (information.contains(search)) return true;
                }
            }
        }
        return false;
    }

    /**
     * Converts a string list to a block list generator
     *
     * @param values the list of values
     * @param world  the world. if null, null is returned
     * @return the list. If null or invalid values were passed, an empty list is returned
     */
    public static BlockListGenerator fromList(List<String> values, String world) {
        if (world == null) return null;
        if (values == null) return new BlockListGenerator(world);
        MaterialProvider provider = AntiShare.getInstance().getMaterialProvider();
        BlockListGenerator list = new BlockListGenerator(world);

        for (String value : values) {
            String[] parts = value.split(":");
            boolean remove = false;

            if (parts[0].equalsIgnoreCase("all")) {
                list.information.clear();
                for (Material material : Material.values()) {
                    BInfo info = new BInfo(material, (short) -1); // -1 will restrict everything
                    if (!list.information.contains(info)) list.information.add(info);
                }
                continue;
            } else if (parts[0].equalsIgnoreCase("none")) {
                list.information.clear();
                continue;
            } else if (parts[0].startsWith("-")) remove = true;

            Material material = provider.fromString(parts[0]);
            short d = -1;
            if (parts.length >= 2) {
                String data = parts[1];
                try {
                    d = Short.parseShort(data);
                } catch (NumberFormatException e) {
                    AntiShare.getInstance().getLogger().warning("Unknown value (world:" + world + "): " + value + ". Assuming " + material.name() + " and all sub types.");
                    d = -1;
                }
            }

            if (material == Material.AIR) {
                AntiShare.getInstance().getLogger().warning("Unknown value (world:" + world + "): " + value + ", skipping.");
                continue;
            }

            BInfo info = new BInfo(material, d);
            if (!remove) {
                if (!list.information.contains(info)) list.information.add(info);
            } else {
                if (list.information.contains(info)) list.information.remove(info);
            }
        }

        return list;
    }

}
