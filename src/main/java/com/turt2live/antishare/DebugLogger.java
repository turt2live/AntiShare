package com.turt2live.antishare;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// TODO: Remove from production
public class DebugLogger {

    public static void log(String message) {
        try {
            File f = new File(AntiShare.p.getDataFolder(), "DEBUG.LOG");
            BufferedWriter writer = new BufferedWriter(new FileWriter(f, true));
            writer.write(message);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
