package com.turt2live.antishare.dev.api.serial;

import com.turt2live.antishare.dev.api.Communication;
import com.turt2live.antishare.dev.api.DataReader;
import com.turt2live.antishare.dev.api.DataWriter;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 * Represents communication with a serial port
 *
 * @author turt2live
 */
public class DevSerialPort implements Communication {

    private SerialPort port;

    /**
     * Creates a new development serial port
     *
     * @param portName the port name, cannot be null
     * @throws java.lang.IllegalArgumentException thrown for bad arguments
     * @throws java.lang.RuntimeException         containing a {@link jssc.SerialPortException} upon error
     */
    public DevSerialPort(String portName, int baudRate, int dataBits, int stopBits, int parity) {
        if (portName == null) throw new IllegalArgumentException("must supply a port name");

        this.port = new SerialPort(portName);
        try {
            this.port.openPort();//Open port
            this.port.setParams(baudRate, dataBits, stopBits, parity);//Set params
        } catch (SerialPortException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean isReady() {
        return this.port != null && this.port.isOpened();
    }

    @Override
    public boolean canSend() {
        return isReady();
    }

    @Override
    public boolean canReceive() {
        return isReady();
    }

    @Override
    public DataReader getReader() {
        return new SerialReader(port);
    }

    @Override
    public DataWriter getWriter() {
        return new SerialWriter(port);
    }

    @Override
    public boolean open() {
        return isReady(); // Does an isOpen check
    }

    @Override
    public boolean close() {
        try {
            return port.closePort();
        } catch (SerialPortException e) {
            // Consume
        }
        return true; // Closed, technically
    }
}
