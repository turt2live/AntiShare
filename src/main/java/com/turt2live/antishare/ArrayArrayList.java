package com.turt2live.antishare;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an array list that takes an array
 *
 * @param <T> the type of list
 * @author turt2live
 */
public class ArrayArrayList<T> extends ArrayList<T> {

    private static final long serialVersionUID = 2863735441934925044L;

    /**
     * Creates a new ArrayArrayList
     *
     * @param items the array items to add
     */
    public ArrayArrayList(T... items) {
        if (items != null) {
            for (T item : items) {
                add(item);
            }
        }
    }

    /**
     * Creates a new ArrayArrayList
     *
     * @param items the array items to add
     */
    public ArrayArrayList(List<T> items) {
        if (items != null) {
            addAll(items);
        }
    }

}
