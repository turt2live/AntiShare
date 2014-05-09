package com.turt2live.antishare.object;

/**
 * Represents an X, Y, Z location
 *
 * @author turt2live
 */
public class ASLocation implements Cloneable {

    /**
     * The X location
     */
    public final int X;

    /**
     * The Y location
     */
    public final int Y;

    /**
     * The Z location
     */
    public final int Z;

    /**
     * The world, if any
     */
    public final AWorld world;

    /**
     * Creates a new location
     *
     * @param x the X location
     * @param y the Y location
     * @param z the Z location
     */
    public ASLocation(int x, int y, int z) {
        this(null, x, y, z);
    }

    /**
     * Creates a new location
     *
     * @param world the world, may be null
     * @param x     the X location
     * @param y     the Y location
     * @param z     the Z location
     */
    public ASLocation(AWorld world, int x, int y, int z) {
        this.X = x;
        this.Y = y;
        this.Z = z;
        this.world = world;
    }

    /**
     * Adds one location to this location. This will ignore the
     * world components and return a copy of this location (including
     * world) with the desired transformation applied. This does
     * not edit the current location and instead creates a new location.
     *
     * @param location the location to add to this one, cannot be null
     *
     * @return the new location, with this world
     */
    public ASLocation add(ASLocation location) {
        if (location == null) throw new IllegalArgumentException();
        int x = this.X + location.X;
        int y = this.Y + location.Y;
        int z = this.Z + location.Z;

        return new ASLocation(world, x, y, z);
    }

    /**
     * @see #add(ASLocation)
     */
    public ASLocation add(int x, int y, int z) {
        return add(new ASLocation(x, y, z));
    }

    @Override
    public ASLocation clone() {
        return new ASLocation(world, X, Y, Z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ASLocation)) return false;

        ASLocation that = (ASLocation) o;

        if (X != that.X) return false;
        if (Y != that.Y) return false;
        if (Z != that.Z) return false;
        if (world != null ? !world.equals(that.world) : that.world != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = X;
        result = 31 * result + Y;
        result = 31 * result + Z;
        result = 31 * result + (world != null ? world.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ASLocation{" +
                "X=" + X +
                ", Y=" + Y +
                ", Z=" + Z +
                ", world=" + world +
                '}';
    }
}
