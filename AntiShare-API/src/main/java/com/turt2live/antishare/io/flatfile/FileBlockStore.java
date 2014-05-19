/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.io.flatfile;

import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.io.generics.GenericBlockStore;
import com.turt2live.antishare.object.ASLocation;
import com.turt2live.antishare.object.attribute.ObjectType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Represents a file used for storing block information.
 * <br/><br/>
 * This uses a binary file to store the information contained within as a continuous
 * data format. The file contains a header before the continuous data. Internally
 * nothing is saved to disk until {@link #save()} is called, therefore everything
 * is stored within memory. It should be noted that {@link #load()} will NOT clear
 * the store, instead, {@link #clear()} should be used to clear the store of data
 * before loading. However, {@link #load()} will overwrite the data, if present, in
 * the store while loading. This functionality may be useful in the event of a needed
 * merge between the file system and the memory, where the file system gets the priority
 * on the data stored. The entire file is ordered with big endian data.
 * <br/><br/>
 * The block size is the number of permitted blocks per dimension. Therefore, the block
 * size taken to the power of 3 will represent the total number of blocks the file can
 * store. To calculate the theoretical size of the resulting file, take the block size
 * to the power 3, multiply the result by 13, and add 16 to the final result. This
 * calculation determines the total number of blocks, calculates the size for the blocks
 * (at 13 bytes per block) and adds the header size to the result (16 bytes). A block
 * size of 50 will result in 125,000 possible block locations which takes up (if filled)
 * a theoretical total of 1,625,000 bytes (about 1.55 MB). When adding the header, the
 * total theoretical file size becomes 1,625,016 (about 1.55 MB).
 * <br/><br/>
 * The header consists of 4 4 byte integers (16 bytes total) representing the "store
 * location". This location is an X/Y/Z location alongside the block size. The X/Y/Z
 * location is the position for the  store when using the block size as a reference.
 * The header is formatted as X, Y, Z, then the block size. To calculate the store
 * location using the block size, floor the result of dividing the desired component
 * (X, Y, or Z) by the block size.
 * <br/><br/>
 * After the header is continuous data representing block information. The block
 * information consists of a byte flag for the {@link com.turt2live.antishare.object.attribute.ObjectType} and 3 4 byte integers
 * for the location of the block (13 bytes total). The data is formatted as flag, block X,
 * block Y, block Z with no leading or trailing bits. The methods {@link #byteToType(byte)}
 * and {@link #typeToByte(com.turt2live.antishare.object.attribute.ObjectType)} can be used to convert
 * between the byte flag and block type.
 * <br/><br/>
 * This type of data store is designed to be used with the {@link FileBlockManager}
 *
 * @author turt2live
 */
public class FileBlockStore extends GenericBlockStore {

    private int sx, sy, sz, size;
    private boolean headerRead = false;
    private File file;
    private RandomAccessFile raf;

    /**
     * Creates a new file block store using a specified header
     *
     * @param file   the file to load, must not be null
     * @param sx     the header X
     * @param sy     the header Y
     * @param sz     the header Z
     * @param blocks the number of permitted blocks per dimension
     */
    public FileBlockStore(File file, int sx, int sy, int sz, int blocks) {
        if (file == null) throw new IllegalArgumentException("file cannot be null");

        this.file = file;
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
        this.size = blocks;
        headerRead = true;
    }

    /**
     * Creates a new file block store using no header (all zeros)
     *
     * @param file the file to load, must not be null
     */
    public FileBlockStore(File file) {
        if (file == null) throw new IllegalArgumentException("file cannot be null");

        this.file = file;
    }

    @Override
    public void save() {
        try {
            if (getLiveMap().size() <= 0) {
                if (file.exists())
                    file.delete();
                return; // Don't save nothing
            }

            raf = new RandomAccessFile(file, "rw");
            long requiredSize = (getLiveMap().size() * 13) + 16;
            raf.setLength(requiredSize);
            MappedByteBuffer out = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, requiredSize);
            out.clear();
            out.order(ByteOrder.BIG_ENDIAN);

            // Write header
            out.putInt(sx);
            out.putInt(sy);
            out.putInt(sz);
            out.putInt(size);

            // Write blocks
            ConcurrentMap<ASLocation, ObjectType> blocks = getLiveMap();
            for (Map.Entry<ASLocation, ObjectType> entry : blocks.entrySet()) {
                // Don't save "unknown" or otherwise "free" blocks
                if (entry.getValue() != ObjectType.UNKNOWN) {
                    byte typeByte = typeToByte(entry.getValue());
                    ASLocation location = entry.getKey();

                    // Write the buffer
                    out.put(typeByte);
                    out.putInt(location.X);
                    out.putInt(location.Y);
                    out.putInt(location.Z);
                }
            }

            raf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadHeader() {
        try {
            if (!file.exists()) return;

            raf = new RandomAccessFile(file, "rw");
            MappedByteBuffer in = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, raf.length());
            in.order(ByteOrder.BIG_ENDIAN);

            loadHeader(in);

            raf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        try {
            if (!file.exists()) return;

            raf = new RandomAccessFile(file, "rw");
            MappedByteBuffer in = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, raf.length());
            in.order(ByteOrder.BIG_ENDIAN);

            if (in.remaining() < 16) return;

            // Read header
            Engine engine = Engine.getInstance();
            loadHeader(in);
            boolean hasError = false;
            // Read blocks
            while (in.remaining() >= 13) {
                byte gmbyte = in.get();
                ObjectType type = byteToType(gmbyte);
                int x = in.getInt();
                int y = in.getInt();
                int z = in.getInt();

                if (type == null) {
                    // This is bad. This means the gamemode byte is invalid and cannot
                    // be read correctly. Maybe half a block was stored? Maybe some weird data
                    // was stored? Tampering? Who knows. We'll simply record the incident
                    // and ignore it.
                    if (!hasError) engine.getLogger().severe("===========================================");
                    hasError = true;
                    engine.getLogger().severe("Invalid gamemode flag for data in file: " + file.getAbsolutePath());
                    engine.getLogger().severe("Error correction has NOT been performed.");
                    engine.getLogger().severe("Here is the known information. It should be noted that the following information is directly read from the file and may not actually represent the data expected in any way.");
                    engine.getLogger().severe("    X = " + x);
                    engine.getLogger().severe("    Y = " + y);
                    engine.getLogger().severe("    Z = " + z);
                    engine.getLogger().severe("    GameMode Byte = " + Integer.toHexString(gmbyte));
                    engine.getLogger().severe("Please ensure that the file you have saved is supported by your version of AntiShare.");
                } else {
                    ObjectType previous = getType(x, y, z);
                    if (previous != ObjectType.UNKNOWN && previous != type) {
                        // Duplicate entry - Print out both to console and save latest
                        // Note: The above check also ensures the previous type is not the same
                        // as the new type. This is because the data hasn't changed otherwise,
                        // although it is weird there is a duplicate.
                        if (!hasError) engine.getLogger().severe("===========================================");
                        hasError = true;
                        engine.getLogger().warning("Duplicate mismatched data in file: " + file.getAbsolutePath());
                        engine.getLogger().warning("Duplicate data was found in the mentioned file. The data is displayed below for your correction. The last data read is the data AntiShare is using for storage, therefore discarding the 'previous' data.");
                        engine.getLogger().warning("LOCATION (" + x + ", " + y + ", " + z + ") Previous GameMode: " + previous + ", new GameMode: " + type);
                    }

                    // Write the new data, regardless of error state
                    setType(x, y, z, type);
                }
            }
            if (in.remaining() != 0) {
                hasError = true;
                engine.getLogger().warning("Failed to read entire file: " + file.getAbsolutePath());
                engine.getLogger().warning("All data was loaded with " + in.remaining() + " bytes left.");
            }
            if (hasError) engine.getLogger().severe("===========================================");

            raf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int[] header() {
        if (!headerRead) loadHeader();
        if (!headerRead) throw new RuntimeException("Failed to load header for file: " + file.getName());

        return new int[] {sx, sy, sz, size};
    }

    private void loadHeader(MappedByteBuffer buffer) throws IOException {
        sx = buffer.getInt();
        sy = buffer.getInt();
        sz = buffer.getInt();
        size = buffer.getInt();
        headerRead = true;
    }

    @Override
    public ObjectType getType(ASLocation location) {
        if (location == null) throw new IllegalArgumentException("location cannot be null");

        // Validate block size range
        int sx = (int) Math.floor(location.X / (double) size);
        int sy = (int) Math.floor(location.Y / (double) size);
        int sz = (int) Math.floor(location.Z / (double) size);

        if (sx != this.sx || sy != this.sy || sz != this.sz)
            throw new IllegalArgumentException("location is out of range");

        return super.getType(location);
    }

    @Override
    public void setType(ASLocation location, ObjectType type) {
        if (location == null) throw new IllegalArgumentException("location cannot be null");

        // Validate block size range
        int sx = (int) Math.floor(location.X / (double) size);
        int sy = (int) Math.floor(location.Y / (double) size);
        int sz = (int) Math.floor(location.Z / (double) size);

        if (sx != this.sx || sy != this.sy || sz != this.sz)
            throw new IllegalArgumentException("location is out of range");

        super.setType(location, type);
    }

    /**
     * Converts a block type to a byte flag
     *
     * @param type the type to convert. Null is assumed to be {@link com.turt2live.antishare.object.attribute.ObjectType#UNKNOWN}
     *
     * @return the byte flag representation of the block type
     */
    public static byte typeToByte(ObjectType type) {
        if (type == null) type = ObjectType.UNKNOWN;
        switch (type) {
            case CREATIVE:
                return 0x01;
            case SURVIVAL:
                return 0x02;
            case ADVENTURE:
                return 0x03;
            case SPECTATOR:
                return 0x04;
            case UNKNOWN:
                return 0x05;
            default:
                return 0x09;
        }
    }

    /**
     * Converts a byte flag to a block type
     *
     * @param type the byte flag to convert. Unknown values return {@link com.turt2live.antishare.object.attribute.ObjectType#UNKNOWN}
     *
     * @return the block type, never null
     */
    public static ObjectType byteToType(byte type) {
        switch (type) {
            case 0x01:
                return ObjectType.CREATIVE;
            case 0x02:
                return ObjectType.SURVIVAL;
            case 0x03:
                return ObjectType.ADVENTURE;
            case 0x04:
                return ObjectType.SPECTATOR;
            case 0x05:
                return ObjectType.UNKNOWN;
            default:
                return null;
        }
    }
}
