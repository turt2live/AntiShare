package com.turt2live.antishare.io.memory;

import com.turt2live.antishare.io.BlockStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class MemoryInventoryManagerTest {

    @Test
    public void testCreate1() {
        new MemoryInventoryManager("test");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreate2() {
        new MemoryInventoryManager(null);
    }

    @Test
    public void testLoad() {
        MemoryInventoryManager manager = new MemoryInventoryManager("test");

        List<BlockStore> list = manager.loadAll();

        assertNotNull(list);
        assertEquals(0, list.size());
    }

}
