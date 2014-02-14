package com.turt2live.antishare.io;

import com.turt2live.antishare.IDRegistry;

import java.io.*;

/**
 * Represents a File Parser for Block information. This loads the stream or file as a text file that must be in the
 * format defined below. Invalid formats will be parsed to their best ability.
 * <p>
 * The file must have a number (with or without a colon) followed by at least one space then the minecraft
 * String ID of the item/block. The number will be parsed first, then the string matching "minecraft".
 * <p/>
 * Example: <code>0 minecraft:air</code>
 * <p/>
 * Other formats are permited, such as the lines below:
 * <code>
 * thisisnotparsed 0         minecraft:air
 * 0 HHHHHHHHH minecraft:air
 * 0 minecraft:air thisisnotparsed
 * </code>
 * <p/>
 * Variations of the examples are still acceptable.
 * </p>
 *
 * @author turt2live
 */
public class BlockInformationParser {

    /**
     * Parses a file, loading the item ids into the IDRegistry
     *
     * @param file the file to parse, cannot be null
     * @return the number of IDs parsed (that were valid)
     */
    public static int parse(File file) throws IOException {
        if (file == null) throw new IllegalArgumentException();
        return parse(new FileInputStream(file));
    }

    /**
     * Parses a stream, loading the item ids into the IDRegistry
     *
     * @param stream the stream to parse, cannot be null
     * @return the number of IDs parsed (that were valid)
     */
    public static int parse(InputStream stream) throws IOException {
        if (stream == null) throw new IllegalArgumentException();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        int numRows = 0;
        String line;

        while ((line = reader.readLine()) != null) {
            int parsedId = 0;
            boolean hasId = false;
            String minecraftName = "";
            String[] parts = line.split(" ");

            for (String s : parts) {
                s = s.trim();
                if (!hasId) {
                    String[] p2 = s.split(":");
                    try {
                        int t = Integer.parseInt(p2[0]);
                        if (p2.length > 1) minecraftName += p2[1];
                        parsedId = t;
                        hasId = true;
                    } catch (NumberFormatException e) {
                    }
                } else {
                    if (s.toLowerCase().startsWith("minecraft:")) {
                        minecraftName += s.toLowerCase();
                        break;
                    }
                }
            }

            if (minecraftName.length() > 0 && hasId) {
                IDRegistry.setMinecraftName(parsedId, minecraftName);
                numRows++;
            }
        }

        reader.close();
        return numRows;
    }

}
