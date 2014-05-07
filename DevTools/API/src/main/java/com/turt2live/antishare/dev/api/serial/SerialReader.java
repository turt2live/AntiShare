package com.turt2live.antishare.dev.api.serial;

import com.turt2live.antishare.dev.api.DataReader;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 * Serial port reader
 *
 * @author turt2live
 */
public class SerialReader implements DataReader {

    private SerialPort port;

    SerialReader(SerialPort port) {
        if (port == null) throw new IllegalArgumentException("cannot have no port");

        this.port = port;
    }

    @Override
    public String readString() {
        StringBuilder stringBuilder = new StringBuilder();
        String newline = "\n";
        boolean hasNewline = false;
        while (!hasNewline) {
            try {
                String temp = port.readString();
                if (temp.contains(newline)) {
                    String[] parts = temp.split(newline);
                    stringBuilder.append(parts[0]);
                    hasNewline = true;
                } else {
                    stringBuilder.append(temp);
                }
            } catch (SerialPortException e) {
                throw new RuntimeException(e);
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public byte[] readBytes(int n) {
        if (n <= 0) throw new IllegalArgumentException("cannot read nothing");

        return readBytes(new byte[n], 0, n);
    }

    @Override
    public byte[] readBytes(byte[] buffer, int offset, int length) {
        if (buffer == null) throw new IllegalArgumentException("must have a buffer");
        if (offset <= 0 || offset > buffer.length - 2) throw new IllegalArgumentException("offset out of range");
        if (length <= 0 || length + offset >= buffer.length) throw new IllegalArgumentException("length out of range");

        for (int i = 0; i < length; i++) {
            int put = offset + i;
            buffer[put] = readByte();
        }

        return buffer;
    }

    @Override
    public byte readByte() {
        try {
            return port.readBytes(1)[0];
        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int readInt() {
        try {
            return port.readIntArray(1)[0];
        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }
    }
}
