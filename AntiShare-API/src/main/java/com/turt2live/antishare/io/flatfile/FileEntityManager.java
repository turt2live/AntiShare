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
import com.turt2live.antishare.io.generics.GenericEntityManager;
import com.turt2live.antishare.object.attribute.ObjectType;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a file-backed entity manager. This particular manager
 * style uses a single binary file with a simple format to it. This
 * is more of a 'data dump' file as it writes all data in a linear,
 * back to back, fashion. This writes the UUID in the form of most
 * significant before least significant bits (as longs). This means
 * that the UUID occupies 16 bytes in the file. Once the UUID has
 * been written, a simple byte flag to represent the ObjectType is written.
 * <p/>
 * This manager will not store UNKNOWN entries and will actively ignore
 * them when loading as well. This is to reduce the memory footprint used
 * by the manager.
 * <p/>
 * With the combination of the UUID and byte flag, one record occupies 17
 * bytes of space in the file. Considering this manager does not have a
 * header block, the data stored is very similar to a dump.
 * <p/>
 * The suggested maximum number of UUIDs per file is no more than 30 million
 * records. At 17 bytes per record this would equate to 436mb of theoretical
 * file size. An optimal maximum would be to have no more than 500 thousand
 * records (8mb theoretical file size) for performance reasons.
 * <p/>
 * Converting the ObjectTypes to and from byte flags is done by the FileBlockStore
 * methods provided.
 * <p/>
 * The {@link #load()} method will not fail on a file which does not exist. Instead,
 * it will load nothing. Calling {@link #load()} will clear the internal store before
 * activating.
 *
 * @author turt2live
 * @see com.turt2live.antishare.io.flatfile.FileBlockStore#typeToByte(com.turt2live.antishare.object.attribute.ObjectType)
 * @see com.turt2live.antishare.io.flatfile.FileBlockStore#byteToType(byte)
 */
// TODO: Unit test
public class FileEntityManager extends GenericEntityManager {

    private File file;
    private RandomAccessFile raf;

    /**
     * Creates a new FileEntityManager
     *
     * @param file the file to use, cannot be null
     */
    public FileEntityManager(File file) {
        if (file == null) throw new IllegalArgumentException("Cannot load/save from nowhere");

        this.file = file;
    }

    @Override
    public void save() {
        if (file.exists()) file.delete();

        Map<UUID, ObjectType> records = getLiveMap();
        if (!records.isEmpty()) {
            try {
                raf = new RandomAccessFile(file, "rw");

                long requiredSize = getLiveMap().size() * 17;
                raf.setLength(requiredSize);
                MappedByteBuffer out = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, requiredSize);
                out.clear();
                out.order(ByteOrder.BIG_ENDIAN);

                // Write data
                for (Map.Entry<UUID, ObjectType> record : records.entrySet()) {
                    if (record.getValue() == ObjectType.UNKNOWN) continue;

                    long uuidMost = record.getKey().getMostSignificantBits();
                    long uuidLeast = record.getKey().getLeastSignificantBits();
                    byte flag = FileBlockStore.typeToByte(record.getValue());

                    out.putLong(uuidMost);
                    out.putLong(uuidLeast);
                    out.put(flag);
                }

                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void load() {
        clear();

        if (!file.exists()) return; // Load nothing

        try {
            raf = new RandomAccessFile(file, "rw");

            MappedByteBuffer in = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, raf.length());
            in.order(ByteOrder.BIG_ENDIAN);


            Engine engine = Engine.getInstance();
            boolean hasError = false;

            while (in.remaining() > 17) {
                long uuidMost = in.getLong();
                long uuidLeast = in.getLong();
                byte gmbyte = in.get();
                ObjectType type = FileBlockStore.byteToType(gmbyte);
                UUID uuid = new UUID(uuidMost, uuidLeast);
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
                    engine.getLogger().severe("    uuidMost = " + uuidMost);
                    engine.getLogger().severe("    uuidLeast = " + uuidLeast);
                    engine.getLogger().severe("    (calc) UUID = " + uuid.toString());
                    engine.getLogger().severe("    GameMode Byte = " + Integer.toHexString(gmbyte));
                    engine.getLogger().severe("Please ensure that the file you have saved is supported by your version of AntiShare.");
                } else {
                    ObjectType previous = getType(uuid);
                    if (previous != ObjectType.UNKNOWN && previous != type) {
                        // Duplicate entry - Print out both to console and save latest
                        // Note: The above check also ensures the previous type is not the same
                        // as the new type. This is because the data hasn't changed otherwise,
                        // although it is weird there is a duplicate.
                        if (!hasError) engine.getLogger().severe("===========================================");
                        hasError = true;
                        engine.getLogger().warning("Duplicate mismatched data in file: " + file.getAbsolutePath());
                        engine.getLogger().warning("Duplicate data was found in the mentioned file. The data is displayed below for your correction. The last data read is the data AntiShare is using for storage, therefore discarding the 'previous' data.");
                        engine.getLogger().warning("UUID (" + uuid.toString() + " [most=" + uuidMost + ", least=" + uuidLeast + "]) Previous GameMode: " + previous + ", new GameMode: " + type);
                    }

                    // Write the new data, regardless of error state
                    setType(uuid, type);
                }
            }

            if (in.remaining() != 0) {
                hasError = true;
                engine.getLogger().warning("Failed to read entire file: " + file.getAbsolutePath());
                engine.getLogger().warning("All data was loaded with " + in.remaining() + " bytes left.");
            }
            if (hasError) engine.getLogger().severe("===========================================");

            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
