package com.turt2live.antishare.dev.api;

/**
 * A class used for communicating with the outside world
 *
 * @author turt2live
 */
public interface Communication {

    /**
     * Determines if this communication layer is in a ready state
     *
     * @return true if this connection is ready
     */
    public boolean isReady();

    /**
     * Determines if this communication is capable of sending data
     *
     * @return true if this can send data
     */
    public boolean canSend();

    /**
     * Determines if this communication is capable of receiving data
     *
     * @return true if this can read data
     */
    public boolean canReceive();

    /**
     * Gets the data reader applicable to this communication. May return
     * null if {@link #canReceive()} is false.
     *
     * @return the data reader, or null
     */
    public DataReader getReader();

    /**
     * Gets the data writer applicable to this communication. May return
     * null if {@link #canSend()} ()} is false.
     *
     * @return the data writer, or null
     */
    public DataWriter getWriter();

    /**
     * Opens this communication
     *
     * @return true if successful
     */
    public boolean open();

    /**
     * Closes this communication
     *
     * @return true if successful
     */
    public boolean close();

}
