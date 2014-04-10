package com.turt2live.antishare.io;

import com.turt2live.antishare.inventory.ASItem;
import com.turt2live.antishare.inventory.InventorySerializer;
import org.bukkit.GameMode;

import java.util.Map;
import java.util.UUID;

/**
 * Represents a single inventory file. The data stored in the file is representing
 * that of a single player (UUID) and the contents of the various gamemodes within.
 *
 * @author turt2live
 */
public interface InventoryStore {

    /**
     * Saves the inventory store to disk
     */
    public void save();

    /**
     * Loads the inventory store from disk
     */
    public void load();

    /**
     * Gets the UUID this inventory store uses
     *
     * @return the UUID
     */
    public UUID getUUID();

    /**
     * Gets the class String of the serializer as represented in the file. Using {@link
     * #setSerializer(com.turt2live.antishare.inventory.InventorySerializer)} will not change
     * the output of this method. This is pulled directly from the file header.
     *
     * @return the serializer class as represented in the file header
     */
    public String getSerializerClass();

    /**
     * Sets the inventory serializer to be used by this store
     *
     * @param serializer the serializer to use, cannot be null
     * @throws java.lang.IllegalArgumentException thrown for illegal arguments
     */
    public void setSerializer(InventorySerializer serializer);

    /**
     * Gets the inventory for a specified GameMode. This returns an empty map if the
     * gamemode cannot be found within the file.
     *
     * @param gamemode the gamemode, cannot be null
     * @return the inventory, should never be null
     * @throws java.lang.IllegalArgumentException thrown for illegal arguments
     */
    public Map<Integer, ASItem> getInventory(GameMode gamemode);

    /**
     * Sets the inventory for a specified gamemode. Setting the map to null will
     * represent "deleting" the entire gamemode from the file.
     *
     * @param gamemode the gamemode, cannot be null
     * @param items    the items to use. Null indicates removal
     * @throws java.lang.IllegalArgumentException thrown for illegal arguments
     */
    public void setInventory(GameMode gamemode, Map<Integer, ASItem> items);

}
