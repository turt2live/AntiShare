package com.turt2live.antishare.utils;

/**
 * Represents an X, Y, Z location
 *
 * @author turt2live
 */
// TODO: Unit test
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
     * Creates a new location
     *
     * @param x the X location
     * @param y the Y location
     * @param z the Z location
     */
    public ASLocation(int x, int y, int z) {
        this.X = x;
        this.Y = y;
        this.Z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ASLocation that = (ASLocation) o;

        if (X != that.X) return false;
        if (Y != that.Y) return false;
        if (Z != that.Z) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = X;
        result = 31 * result + Y;
        result = 31 * result + Z;
        return result;
    }

    @Override
    public ASLocation clone() {
        return new ASLocation(X, Y, Z);
    }

    @Override
    public String toString() {
        return "ASLocation{" +
                "X=" + X +
                ", Y=" + Y +
                ", Z=" + Z +
                '}';
    }
}
