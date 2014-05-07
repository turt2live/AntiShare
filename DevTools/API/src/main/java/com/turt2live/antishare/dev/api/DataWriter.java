package com.turt2live.antishare.dev.api;

/**
 * Writes data
 *
 * @author turt2live
 */
public interface DataWriter {

    /**
     * Writes a string to the data source
     *
     * @param data the string to write, cannot be null
     * @throws java.lang.IllegalArgumentException thrown for bad arguments
     */
    public void writeString(String data);

    /**
     * Writes a byte array to the data source
     *
     * @param bytes the bytes to write, cannot be null
     * @throws java.lang.IllegalArgumentException thrown for bad arguments
     */
    public void writeBytes(byte[] bytes);

    /**
     * Writes a byte to the data source
     *
     * @param bite the byte to write
     */
    public void writeByte(byte bite);

    /**
     * Writes an integer to the data source
     *
     * @param data the integer to write
     */
    public void writeInt(int data);
}
