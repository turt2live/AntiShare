package com.turt2live.antishare.io.memory;

import com.turt2live.antishare.io.BlockStore;
import com.turt2live.antishare.io.generics.GenericBlockManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a block manager that is not persistent in any way. Any changes to this
 * block manager are done in memory and are not saved to any form of persistent storage.
 * <br/>
 * There are no errors for saving/loading the block manager. This would silently fail.
 *
 * @author turt2live
 */
// TODO: Add unit test
public class MemoryBlockManager extends GenericBlockManager {

    /**
     * Creates a new block manager
     */
    public MemoryBlockManager() {
        super(1024);
    }

    @Override
    protected BlockStore createStore(int sx, int sy, int sz) {
        return new MemoryBlockStore();
    }

    @Override
    public List<BlockStore> loadAll() {
        return new ArrayList<BlockStore>();
    }
}
