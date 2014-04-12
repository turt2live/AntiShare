package com.turt2live.antishare.inventory.defaults;

import com.turt2live.antishare.inventory.ASItem;

/**
 * A generic AntiShare item. This does nothing but provide basic support for
 * testing materials and default attributes.
 *
 * @author turt2live
 */
public class DefaultASItem extends ASItem {

    private int id;

    /**
     * Creates a new default ASItem. The passed ID is not validated in any way.
     *
     * @param id the ID to use, can be anything
     */
    public DefaultASItem(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof DefaultASItem)) return false;
        return ((DefaultASItem) o).id == this.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public ASItem clone() {
        return new DefaultASItem(this.id);
    }

    /**
     * Gets the ID of this ASItem
     *
     * @return the ID
     */
    public int getId() {
        return id;
    }
}
