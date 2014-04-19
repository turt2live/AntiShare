package com.turt2live.antishare.io.memory;

import com.turt2live.antishare.io.BlockStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class MemoryBlockManagerTest {

    @Test
    public void testLoad() {
        MemoryBlockManager manager = new MemoryBlockManager();

        List<BlockStore> list = manager.loadAll();

        assertNotNull(list);
        assertEquals(0, list.size());
    }

}
