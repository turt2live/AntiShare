package com.turt2live.antishare.bukkit.lists;


import org.bukkit.Material;

public class BlockInformation {
    final Material material;
    final short damage;
    final String world;
    int priority = -1;

    BlockInformation(Material m, short d, String w) {
        material = m;
        damage = d;
        world = w;
    }

    /**
     * Determines if this information is higher priority than the other
     * @param other the other
     * @return true if this is higher, false otherwise
     */
    public boolean isHigher(BlockInformation other){
        if(other==null)return true;
        if(other.priority<=0&&this.priority>0) return true;
        if(other.priority>0&&this.priority<=0)return false;
        return other.priority>this.priority; // Higher number = lower priority
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockInformation)) return false;

        BlockInformation that = (BlockInformation) o;

        if (damage != that.damage) return false;
        if (material != that.material) return false;
        if (world != null ? !world.equals(that.world) : that.world != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = material != null ? material.hashCode() : 0;
        result = 31 * result + (int) damage;
        result = 31 * result + (world != null ? world.hashCode() : 0);
        return result;
    }
}
