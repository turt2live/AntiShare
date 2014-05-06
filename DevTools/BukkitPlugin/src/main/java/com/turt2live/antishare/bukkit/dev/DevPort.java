package com.turt2live.antishare.bukkit.dev;

import jssc.SerialPort;
import jssc.SerialPortException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DevPort {

    private SerialPort port;

    // TODO: Actually use this

    public DevPort(String port) {
        this.port = new SerialPort(port);
        try {
            this.port.openPort();//Open port
            this.port.setParams(9600, 8, 1, 0);//Set params
            int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
            this.port.setEventsMask(mask);//Set mask
            Bukkit.broadcastMessage(ChatColor.GREEN + "Waiting for data...");
        } catch (SerialPortException ex) {
            Bukkit.broadcastMessage(ChatColor.RED + "Failed to open port: " + ex.getMessage());
            this.port = null;
        }
    }

    public boolean canUse() {
        return this.port != null;
    }
}
