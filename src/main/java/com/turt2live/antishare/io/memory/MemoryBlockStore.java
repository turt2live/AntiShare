package com.turt2live.antishare.io.memory;

import com.turt2live.antishare.io.generics.GenericBlockStore;

/**
 * Represents a block store that is not persistent in any way. Any changes to this
 * block store are done in memory and are not saved to any form of persistent storage.
 * <br/>
 * There are no errors for saving/loading the block store. This would silently fail.
 *
 * @author turt2live
 */
// TODO: Add unit test
public class MemoryBlockStore extends GenericBlockStore {

    @Override
    public void save() {
    }

    @Override
    public void load() {
    }

}
