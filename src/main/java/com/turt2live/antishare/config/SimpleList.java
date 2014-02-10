package com.turt2live.antishare.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a simple list for objects
 *
 * @param <T> the simple list type
 * @author turt2live
 */
public class SimpleList<T> {

    private List<T> listing = new ArrayList<T>();

    /**
     * Adds an item to the simple list
     *
     * @param t the item to add
     */
    public void addItem(T t) {
        if (t == null) throw new IllegalArgumentException("item cannot be null");

        listing.add(t);
    }

    /**
     * Removes an item from the simple list
     *
     * @param t the item to remove
     */
    public void removeItem(T t) {
        if (t == null) throw new IllegalArgumentException("item cannot be null");

        listing.remove(t);
    }

    /**
     * Gets whether or not this simple list contains the specified item
     *
     * @param t the item to lookup
     * @return true if contained, false otherwise
     */
    public boolean hasItem(T t) {
        if (t == null) throw new IllegalArgumentException("item cannot be null");

        return listing.contains(t);
    }

    /**
     * Gets a listing of all items on the simple list
     *
     * @return the item listing
     */
    public List<T> getListing() {
        return Collections.unmodifiableList(listing);
    }

}
