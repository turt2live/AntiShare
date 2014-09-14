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

import com.turt2live.antishare.object.ASLocation;
import com.turt2live.antishare.object.attribute.ObjectType;
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
        store.setType(0, 0, 0, ObjectType.ADVENTURE);
        verify(store).setType(any(ASLocation.class), eq(ObjectType.ADVENTURE));
        ObjectType type = store.getType(0, 0, 0);
        verify(store).getType(any(ASLocation.class));
        assertEquals(ObjectType.ADVENTURE, type);

        reset(store);
        store.setType(new ASLocation(0, 0, 2), ObjectType.CREATIVE);
        assertEquals(ObjectType.CREATIVE, store.getType(new ASLocation(0, 0, 2)));

        assertEquals(ObjectType.UNKNOWN, store.getType(new ASLocation(0, 2, 3)));
    }

    @Test
    public void testOverwrite() {
        store.setType(new ASLocation(0, 9, 9), ObjectType.CREATIVE);
        assertEquals(ObjectType.CREATIVE, store.getType(new ASLocation(0, 9, 9)));
        store.setType(new ASLocation(0, 9, 9), ObjectType.SURVIVAL);
        assertEquals(ObjectType.SURVIVAL, store.getType(new ASLocation(0, 9, 9)));
        store.setType(new ASLocation(0, 9, 9), null);
        assertEquals(ObjectType.UNKNOWN, store.getType(new ASLocation(0, 9, 9)));
        store.setType(new ASLocation(0, 9, 9), ObjectType.SPECTATOR);
        assertEquals(ObjectType.SPECTATOR, store.getType(new ASLocation(0, 9, 9)));
        store.setType(new ASLocation(0, 9, 9), ObjectType.UNKNOWN);
        assertEquals(ObjectType.UNKNOWN, store.getType(new ASLocation(0, 9, 9)));
    }

    @Test
    public void testClearGetALl() {
        store.setType(new ASLocation(0, 2, 2), ObjectType.SPECTATOR);
        assertEquals(ObjectType.SPECTATOR, store.getType(new ASLocation(0, 2, 2)));
        store.clear();
        assertEquals(ObjectType.UNKNOWN, store.getType(new ASLocation(0, 2, 2)));

        ConcurrentMap<ASLocation, ObjectType> map = store.getLiveMap();
        Map<ASLocation, ObjectType> map2 = store.getAll();
        assertNotNull(map);
        assertEquals(0, map.size());
        assertNotNull(map2);
        assertEquals(0, map2.size());

        store.setType(new ASLocation(0, 0, 0), ObjectType.SPECTATOR);
        map = store.getLiveMap();
        map2 = store.getAll();
        assertNotNull(map);
        assertEquals(1, map.size());
        assertNotNull(map2);
        assertEquals(1, map2.size());

        store.setType(new ASLocation(0, 0, 0), ObjectType.UNKNOWN);
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

        store.setType(0, 0, 0, ObjectType.UNKNOWN);
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
        store.setType(null, ObjectType.ADVENTURE); // null block type tested elsewhere
    }

    @BeforeClass
    public static void before() {
        store = mock(GenericBlockStore.class, Mockito.CALLS_REAL_METHODS);
        store.initTest();
    }

}
