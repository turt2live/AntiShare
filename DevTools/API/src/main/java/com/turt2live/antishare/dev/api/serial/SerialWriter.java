package com.turt2live.antishare.dev.api.serial;

import com.turt2live.antishare.dev.api.DataWriter;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 * Serial port writer
 *
 * @author turt2live
 */
public class SerialWriter implements DataWriter {

    private SerialPort port;

    SerialWriter(SerialPort port) {
        if (port == null) throw new IllegalArgumentException("port cannot be null");

        this.port = port;
    }

    @Override
    public void writeString(String data) {
        if (data == null) throw new IllegalArgumentException("cannot write nothing");

        try {
            port.writeString(data);
        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeBytes(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) throw new IllegalArgumentException("cannot write nothing");

        try {
            port.writeBytes(bytes);
        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeByte(byte bite) {
        try {
            port.writeByte(bite);
        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeInt(int data) {
        try {
            port.writeInt(data);
        } catch (SerialPortException e) {
            throw new RuntimeException(e);
        }
    }
}
