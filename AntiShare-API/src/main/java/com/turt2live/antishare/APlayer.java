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

}
