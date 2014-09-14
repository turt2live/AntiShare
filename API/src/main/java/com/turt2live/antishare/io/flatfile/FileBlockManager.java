/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.io.flatfile;

import com.turt2live.antishare.io.BlockStore;
import com.turt2live.antishare.io.generics.GenericBlockManager;
import com.turt2live.antishare.object.ASLocation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A file-based block manager. This manager is a collection of {@link FileBlockStore}s to store
 * block information.
 * <br/><br/>
 * When {@link #loadAll()} is called, any file stores that do not match the block size defined will
 * be silently rejected.
 *
 * @author turt2live
 * @see FileBlockStore
 */
public class FileBlockManager extends GenericBlockManager {

    private ConcurrentMap<ASLocation, String> index = new ConcurrentHashMap<>();
    private File folder;

    /**
     * Creates a new FileBlockManager
     *
     * @param blocksPerStore the blocks per store
     * @param folder         the folder to use. This must exist, be a folder, and cannot be null
     */
    public FileBlockManager(int blocksPerStore, File folder) {
        super(blocksPerStore);
        if (folder == null || !folder.exists() || !folder.isDirectory())
            throw new IllegalArgumentException("folder must be a folder, cannot be null, and must exist");

        this.folder = folder;
        index();
    }

    @Override
    protected BlockStore createStore(int sx, int sy, int sz) {
        // File name is irrelevant. Check index, if not found then use default naming scheme
        ASLocation indexLocation = new ASLocation(sx, sy, sz);
        String fileName = index.get(indexLocation);
        File storeFile = fileName == null ? new File(folder, sx + "." + sy + "." + sz + ".dat") : new File(fileName);

        // Check for invalid index
        if (!storeFile.exists() && fileName != null) storeFile = new File(folder, sx + "." + sy + "." + sz + ".dat");

        // Update index
        if (fileName == null || !fileName.equals(storeFile.getAbsolutePath()) || !storeFile.exists())
            index.put(indexLocation, storeFile.getAbsolutePath());

        return new FileBlockStore(storeFile, sx, sy, sz, blocksPerStore);
    }

    @Override
    public List<BlockStore> loadAll() {
        File[] files = folder.listFiles();
        ConcurrentMap<ASLocation, BlockStore> stores = getLiveStores();
        stores.clear(); // Assume a save has been done
        if (files != null) {
            for (File file : files) {
                FileBlockStore store = new FileBlockStore(file);

                if (store.header()[3] != blocksPerStore) continue; // Ignore anything that does not match our size

                int[] header = store.header();
                ASLocation storeLocation = new ASLocation(header[0], header[1], header[2]);
                stores.put(storeLocation, store);
                store.load(); // Only load data once we know the header is OK
            }
        }

        List<BlockStore> storesList = new ArrayList<>();
        storesList.addAll(stores.values());
        return storesList;
    }

    private void index() {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                FileBlockStore store = new FileBlockStore(file);

                if (store.header()[3] != blocksPerStore) continue; // Ignore anything that does not match our size

                int[] header = store.header();
                ASLocation storeLocation = new ASLocation(header[0], header[1], header[2]);
                index.put(storeLocation, file.getAbsolutePath());
            }
        }
    }

    /**
     * Gets the largest block size from the block stores
     *
     * @return the largest block size, or -1 for none found
     */
    public int getLargestBlockSize() {
        File[] files = folder.listFiles();
        int highHeader = -1;
        if (files != null) {
            for (File file : files) {
                FileBlockStore store = new FileBlockStore(file);
                int[] header = store.header();

                if (header[3] > highHeader) highHeader = header[3];
            }
        }
        return highHeader;
    }

    /**
     * Gets the smallest block size from the block stores
     *
     * @return the smallest block size, or -1 for none found
     */
    public int getSmallestBlockSize() {
        File[] files = folder.listFiles();
        int highHeader = Integer.MAX_VALUE;
        int count = 0;
        if (files != null) {
            for (File file : files) {
                FileBlockStore store = new FileBlockStore(file);
                int[] header = store.header();

                if (header[3] < highHeader) highHeader = header[3];
                count++;
            }
        }
        return count == 0 ? -1 : highHeader;
    }

    /**
     * Gets the most common block size from the block stores
     *
     * @return the most common block size, or -1 for none found
     */
    public int getMostCommonBlockSize() {
        File[] files = folder.listFiles();
        Map<Integer, Integer> counts = new HashMap<>();
        if (files != null) {
            for (File file : files) {
                FileBlockStore store = new FileBlockStore(file);
                int[] header = store.header();

                if (counts.containsKey(header[3]))
                    counts.put(header[3], counts.get(header[3]) + 1);
                else
                    counts.put(header[3], 1);
            }
        }
        if (!counts.isEmpty()) {
            int header = -1;
            int count = -1;
            for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
                if (entry.getValue() > count) {
                    header = entry.getKey();
                    count = entry.getValue();
                }
            }
            return header;
        }
        return -1;
    }

    /**
     * Gets the average block size from the block stores
     *
     * @return the average block size, or -1 if not found
     */
    public int getAverageBlockSize() {
        File[] files = folder.listFiles();
        int total = 0;
        int count = 0;
        if (files != null) {
            for (File file : files) {
                FileBlockStore store = new FileBlockStore(file);
                int[] header = store.header();

                total += header[3];
                count++;
            }
        }
        return count == 0 ? -1 : (int) (total / (double) count);
    }

    /**
     * Gets an array sorted by no particular order of all the unique block sizes
     * represented by the block stores.
     *
     * @return the unsorted array. Never null, but may be of length 0 to represent "no stores"
     */
    public int[] getBlockSizes() {
        List<Integer> sizes = new ArrayList<>();
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                FileBlockStore store = new FileBlockStore(file);
                int[] header = store.header();
                if (!sizes.contains(header[3])) sizes.add(header[3]);
            }
        }
        int[] data = new int[sizes.size()];
        for (int i = 0; i < sizes.size(); i++)
            data[i] = sizes.get(i);
        return data;
    }

    /**
     * Gets a list of block stores that are of a particular block size. This method only loads header information
     * for the specified file stores and will not attempt to load the entire file.
     *
     * @param blockSize the block size to look for. Value must be greater than 0. See {@link FileBlockStore} for more information
     *
     * @return the list of block stores. Never null but may be empty
     *
     * @see FileBlockStore
     */
    public List<BlockStore> getStoresOfSize(int blockSize) {
        if (blockSize <= 0) throw new IllegalArgumentException("block size must be a positive, non-zero, number");

        List<BlockStore> stores = new ArrayList<>();
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                FileBlockStore store = new FileBlockStore(file);
                int[] header = store.header();

                if (header[3] == blockSize) stores.add(store);
            }
        }
        return stores;
    }
}
