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

package com.turt2live.antishare.io.generics;

import com.turt2live.antishare.collections.ArrayArrayList;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.io.BlockStore;
import com.turt2live.antishare.object.ASLocation;
import com.turt2live.antishare.object.attribute.BlockType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class GenericBlockManagerTest {

    private static class TestManager extends GenericBlockManager {

        private BlockStore store;

        public TestManager(BlockStore store, int blockSize) {
            super(blockSize);
            this.store = store;
        }

        @Override
        protected BlockStore createStore(int sx, int sy, int sz) {
            CREATE_CALLS++;
            return store;
        }

        @Override
        public List<BlockStore> loadAll() {
            return new ArrayArrayList<BlockStore>(store);
        }
    }

    private static final int BLOCK_SIZE = 1023;
    private static int CREATE_CALLS = 0;
    private static GenericBlockManager manager;
    private static BlockStore store;

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBlockStore1() {
        new TestManager(store, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBlockStore2() {
        new TestManager(store, 0);
    }

    @Test
    public void testValidBlockStore() {
        new TestManager(store, 1);
    }

    @Test
    public void testGetStore() {
        BlockStore aStore = manager.getStore(0, 0, 0);
        assertNotNull(aStore);
        assertEquals(store, aStore);
        assertEquals(1, CREATE_CALLS);

        BlockStore bStore = manager.getStore(0, 0, 0);
        assertNotNull(bStore);
        assertEquals(aStore, bStore);
        assertEquals(1, CREATE_CALLS);

        manager = new TestManager(store, BLOCK_SIZE); // Reset
        CREATE_CALLS = 0; // Reset
        aStore = manager.getStore(new ASLocation(0, 0, 0));
        assertNotNull(aStore);
        assertEquals(store, aStore);
        assertEquals(1, CREATE_CALLS);

        bStore = manager.getStore(new ASLocation(0, 0, 0));
        assertNotNull(bStore);
        assertEquals(aStore, bStore);
        assertEquals(1, CREATE_CALLS);
    }

    @Test
    public void testSetBlock() {
        // int, int, int should forward to ASLocation methods
        manager.setBlockType(0, 0, 0, BlockType.ADVENTURE);
        verify(store, times(1)).setType(any(ASLocation.class), eq(BlockType.ADVENTURE));
        manager.setBlockType(0, 0, 0, null);
        verify(store, times(1)).setType(any(ASLocation.class), eq(BlockType.UNKNOWN));
        manager.setBlockType(0, 0, 0, BlockType.UNKNOWN);
        verify(store, times(2)).setType(any(ASLocation.class), eq(BlockType.UNKNOWN));

        manager.setBlockType(new ASLocation(0, 0, 0), BlockType.ADVENTURE);
        verify(store, times(2)).setType(any(ASLocation.class), eq(BlockType.ADVENTURE));
        manager.setBlockType(new ASLocation(0, 0, 0), null);
        verify(store, times(3)).setType(any(ASLocation.class), eq(BlockType.UNKNOWN));
        manager.setBlockType(new ASLocation(0, 0, 0), BlockType.UNKNOWN);
        verify(store, times(4)).setType(any(ASLocation.class), eq(BlockType.UNKNOWN));
    }

    @Test
    public void testGetBlock() {
        manager.getBlockType(0, 0, 0);
        verify(store, times(1)).getType(any(ASLocation.class));
        manager.getBlockType(new ASLocation(0, 0, 0));
        verify(store, times(2)).getType(any(ASLocation.class));
    }

    @Test
    public void testSaveAll() {
        manager.saveAll();
        verify(store).save();
    }

    @Test
    public void testCleanup() {
        reset(store); // For fresh use
        when(store.getLastAccess()).thenReturn(System.currentTimeMillis()); // "Fresh"
        manager.cleanup();
        verify(store, times(0)).save();
        verify(store, times(1)).getLastAccess();

        when(store.getLastAccess()).thenReturn(System.currentTimeMillis() - (Engine.getInstance().getCacheMaximum() * 2)); // "Fresh"
        manager.cleanup();
        verify(store, times(1)).save();
        verify(store, times(2)).getLastAccess();
    }

    @Test
    public void testMisc() {
        assertEquals(BLOCK_SIZE, manager.getBlocksPerStore());

        ConcurrentMap<ASLocation, BlockStore> stores = manager.getLiveStores();
        assertNotNull(stores);
        assertEquals(1, stores.size());
        assertEquals(store, stores.values().iterator().next()); // Checks first value
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullGetStore() {
        manager.getStore(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullGetBlock() {
        manager.getBlockType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSetType() {
        manager.setBlockType(null, BlockType.ADVENTURE); // Null block type tested elsewhere
    }

    @BeforeClass
    public static void before() {
        store = mock(BlockStore.class);
        manager = new TestManager(store, BLOCK_SIZE);
    }

}
