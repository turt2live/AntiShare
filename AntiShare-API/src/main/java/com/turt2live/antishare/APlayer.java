package com.turt2live.antishare;

import java.util.UUID;

/**
 * Represents an AntiShare player
 *
 * @author turt2live
 */
public interface APlayer {

    /**
     * Gets the player's name
     *
     * @return the player's name
     */
    public String getName();

    /**
     * Gets the player's UUID
     *
     * @return the player's UUID
     */
    public UUID getUUID();

    /**
     * Gets the player's Game Mode
     *
     * @return the player's Game Mode
     */
    public ASGameMode getGameMode();

    /**
     * Sets the player's Game Mode
     *
     * @param gameMode the new Game Mode to use, cannot be null
     */
    public void setGameMode(ASGameMode gameMode);

    /**
     * Determines if a player has a permission
     *
     * @param permission the permission to check, cannot be null
     * @return true if the player has the permission, false otherwise
     */
    public boolean hasPermission(String permission);

    /**
     * Sends a chat message to the player. Internally chat colors are converted as
     * appropriate.
     *
     * @param message the message to send, cannot be null
     */
    public void sendMessage(String message);

}
