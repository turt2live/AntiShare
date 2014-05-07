package com.turt2live.antishare.dev.api;

/**
 * Reads data
 *
 * @author turt2live
 */
public interface DataReader {

    /**
     * Reads a string (terminated by a newline) from the data source. This
     * will not contain the final newline character.
     *
     * @return the read string
     */
    public String readString();

    /**
     * Reads N number of byte from the data source
     *
     * @param n the number of bytes, cannot be less than or equal to zero
     * @return the bytes read
     * @throws java.lang.IllegalArgumentException thrown for bad arguments
     */
    public byte[] readBytes(int n);

    /**
     * Reads a buffer of bytes from the data source
     *
     * @param buffer the buffer to read into, cannot be null or empty
     * @param offset the starting position of the array to ready data into. Must be length - 2 at minimum
     * @param length the length of data to read, cannot exceed the array length
     * @return the same array, for chaining purposes
     * @throws java.lang.IllegalArgumentException thrown for bad arguments
     */
    public byte[] readBytes(byte[] buffer, int offset, int length);

    /**
     * Reads a single byte from the data source
     *
     * @return the byte read
     */
    public byte readByte();

    /**
     * Reads an int from the data source
     *
     * @return the int read
     */
    public int readInt();

}
