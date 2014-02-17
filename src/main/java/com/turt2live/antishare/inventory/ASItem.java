package com.turt2live.antishare.inventory;

/**
 * An AntiShare item
 *
 * @author turt2live
 */
public abstract class ASItem implements Cloneable {

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public abstract ASItem clone();

}
