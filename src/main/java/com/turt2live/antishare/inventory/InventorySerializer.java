package com.turt2live.antishare.inventory;

/**
 * Represents a class to serialize inventory objects.
 *
 * @author turt2live
 */
public interface InventorySerializer {

    /**
     * Converts an ASItem to JSON format
     *
     * @param item the item to convert, cannot be null
     * @return the JSON string representing the item
     * @throws java.lang.IllegalArgumentException thrown for illegal arguments
     */
    public String toJson(ASItem item);

    /**
     * Converts a JSON string to an ASItem
     *
     * @param json the JSON string representing the ASItem, cannot be null
     * @return the ASItem represented by the JSON. May be null if the JSON cannot be parsed into an ASItem
     * @throws java.lang.IllegalArgumentException thrown for illegal arguments
     */
    public ASItem fromJson(String json);

}
