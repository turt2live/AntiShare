package com.turt2live.antishare.io.generics;

import com.turt2live.antishare.utils.ASLocation;
import com.turt2live.antishare.utils.BlockType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class GenericBlockStoreTest {

    private static GenericBlockStore store;

    @Test
    public void testSetGet() {
        reset(store);
        store.setType(0, 0, 0, BlockType.ADVENTURE);
        verify(store).setType(any(ASLocation.class), eq(BlockType.ADVENTURE));
        BlockType type = store.getType(0, 0, 0);
        verify(store).getType(any(ASLocation.class));
        assertEquals(BlockType.ADVENTURE, type);

        reset(store);
        store.setType(new ASLocation(0, 0, 2), BlockType.CREATIVE);
        assertEquals(BlockType.CREATIVE, store.getType(new ASLocation(0, 0, 2)));

        assertEquals(BlockType.UNKNOWN, store.getType(new ASLocation(0, 2, 3)));
    }

    @Test
    public void testOverwrite() {
        store.setType(new ASLocation(0, 9, 9), BlockType.CREATIVE);
        assertEquals(BlockType.CREATIVE, store.getType(new ASLocation(0, 9, 9)));
        store.setType(new ASLocation(0, 9, 9), BlockType.SURVIVAL);
        assertEquals(BlockType.SURVIVAL, store.getType(new ASLocation(0, 9, 9)));
        store.setType(new ASLocation(0, 9, 9), null);
        assertEquals(BlockType.UNKNOWN, store.getType(new ASLocation(0, 9, 9)));
        store.setType(new ASLocation(0, 9, 9), BlockType.SPECTATOR);
        assertEquals(BlockType.SPECTATOR, store.getType(new ASLocation(0, 9, 9)));
        store.setType(new ASLocation(0, 9, 9), BlockType.UNKNOWN);
        assertEquals(BlockType.UNKNOWN, store.getType(new ASLocation(0, 9, 9)));
    }

    @Test
    public void testClearGetALl() {
        store.setType(new ASLocation(0, 2, 2), BlockType.SPECTATOR);
        assertEquals(BlockType.SPECTATOR, store.getType(new ASLocation(0, 2, 2)));
        store.clear();
        assertEquals(BlockType.UNKNOWN, store.getType(new ASLocation(0, 2, 2)));

        ConcurrentMap<ASLocation, BlockType> map = store.getLiveMap();
        Map<ASLocation, BlockType> map2 = store.getAll();
        assertNotNull(map);
        assertEquals(0, map.size());
        assertNotNull(map2);
        assertEquals(0, map2.size());

        store.setType(new ASLocation(0, 0, 0), BlockType.SPECTATOR);
        map = store.getLiveMap();
        map2 = store.getAll();
        assertNotNull(map);
        assertEquals(1, map.size());
        assertNotNull(map2);
        assertEquals(1, map2.size());

        store.setType(new ASLocation(0, 0, 0), BlockType.UNKNOWN);
        map = store.getLiveMap();
        map2 = store.getAll();
        assertNotNull(map);
        assertEquals(0, map.size());
        assertNotNull(map2);
        assertEquals(0, map2.size());
    }

    @Test
    public void testLastAccess() {
        long last = store.getLastAccess();

        // Work load
        long curr = System.currentTimeMillis();
        while (curr == System.currentTimeMillis()) ;

        store.setType(0, 0, 0, BlockType.UNKNOWN);
        assertTrue(last < store.getLastAccess());
        last = store.getLastAccess();

        // Work load
        curr = System.currentTimeMillis();
        while (curr == System.currentTimeMillis()) ;

        store.getType(0, 0, 0);
        assertTrue(last < store.getLastAccess());
        last = store.getLastAccess();

        // Work load
        curr = System.currentTimeMillis();
        while (curr == System.currentTimeMillis()) ;

        store.clear();
        assertTrue(last < store.getLastAccess());
        last = store.getLastAccess();

        // Work load
        curr = System.currentTimeMillis();
        while (curr == System.currentTimeMillis()) ;

        store.getAll();
        assertEquals(last, store.getLastAccess());

        // Work load
        curr = System.currentTimeMillis();
        while (curr == System.currentTimeMillis()) ;

        store.getLastAccess();
        assertEquals(last, store.getLastAccess());

        // Work load
        curr = System.currentTimeMillis();
        while (curr == System.currentTimeMillis()) ;

        store.getLiveMap();
        assertEquals(last, store.getLastAccess());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullGet() {
        store.getType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSet() {
        store.setType(null, BlockType.ADVENTURE); // null block type tested elsewhere
    }

    @BeforeClass
    public static void before() {
        store = mock(GenericBlockStore.class, Mockito.CALLS_REAL_METHODS);
        store.initTest();
    }

}
