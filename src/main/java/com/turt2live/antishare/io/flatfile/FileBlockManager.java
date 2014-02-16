package com.turt2live.antishare.io.flatfile;

import com.turt2live.antishare.ASLocation;
import com.turt2live.antishare.io.BlockStore;
import com.turt2live.antishare.io.generics.GenericBlockManager;

import java.io.File;
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
    }

    @Override
    protected BlockStore createStore(int sx, int sy, int sz) {
        // File name is not used, the store uses a header for this information
        File storeFile = new File(folder, sx + "." + sy + "." + sz + ".dat");
        return new FileBlockStore(storeFile, sx, sy, sz, blocksPerStore);
    }

    @Override
    public void loadAll() {
        File[] files = folder.listFiles();
        ConcurrentMap<ASLocation, BlockStore> stores = getLiveStores();
        if (files != null) {
            for (File file : files) {
                FileBlockStore store = new FileBlockStore(file);
                store.load();

                if (store.getHeader()[3] != blocksPerStore) continue; // Ignore anything that does not match our size

                int[] header = store.getHeader();
                ASLocation storeLocation = new ASLocation(header[0], header[1], header[2]);
                stores.put(storeLocation, store);
            }
        }
    }
}
