package com.turt2live.antishare.collections;

import java.util.Map;

/**
 * Represents a slotted collection. This type of collection is designed to
 * be used similar to an inventory system.
 *
 * @param <T> the collection type
 * @author turt2live
 */
public interface SlottedCollection<T> {

    /**
     * Sets a slot to contain an object
     *
     * @param slot the slot
     * @param t    the object
     */
    public void set(int slot, T t);

    /**
     * Gets the object contained in a slot
     *
     * @param slot the slot to lookup
     * @return the object, or null if not found
     */
    public T get(int slot);

    /**
     * Gets a mapping of all slots and objects
     *
     * @return the mapping, never null but may be empty
     */
    public Map<Integer, T> getAll();

    /**
     * Clears the collection
     */
    public void clear();

}
