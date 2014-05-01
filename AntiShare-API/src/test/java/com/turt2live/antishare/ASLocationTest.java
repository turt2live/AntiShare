package com.turt2live.antishare;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class ASLocationTest {

    @Test
    public void testCreate() {
        AWorld world = mock(AWorld.class);
        for (int x = -2; x < 2; x++) {
            for (int y = -2; y < 2; y++) {
                for (int z = -2; z < 2; z++) {
                    ASLocation location = new ASLocation(x, y, z);
                    assertEquals(x, location.X);
                    assertEquals(y, location.Y);
                    assertEquals(z, location.Z);
                    assertNull(location.world);

                    location = new ASLocation(world, x, y, z);
                    assertEquals(x, location.X);
                    assertEquals(y, location.Y);
                    assertEquals(z, location.Z);
                    assertNotNull(location.world);
                }
            }
        }
    }

    @Test
    public void testWorldCreate() {
        AWorld world = mock(AWorld.class);
        ASLocation location = new ASLocation(world, 0, 0, 0);
        assertEquals(world, location.world);
    }

    @Test
    public void testNullWorldCreate() {
        ASLocation location = new ASLocation(null, 0, 0, 0);
        assertNull(location.world);
    }

    @Test
    public void testEquality() {
        ASLocation location1 = new ASLocation(1, 2, 3);
        ASLocation location2 = new ASLocation(1, 2, 3);
        ASLocation location3 = new ASLocation(2, 3, 4);

        assertTrue(location1.equals(location2));
        assertTrue(location2.equals(location1));
        assertFalse(location1.equals(location3));
        assertFalse(location2.equals(location3));
        assertFalse(location1.equals(null));

        // World equality testing
        AWorld world1 = mock(AWorld.class);
        AWorld world2 = mock(AWorld.class);
        AWorld world3 = mock(AWorld.class);

        location1 = new ASLocation(world1, 1, 2, 3);
        location2 = new ASLocation(world1, 1, 2, 3);
        location3 = new ASLocation(world1, 2, 3, 4);

        assertTrue(location1.equals(location2));
        assertTrue(location2.equals(location1));
        assertFalse(location1.equals(location3));
        assertFalse(location2.equals(location3));
        assertFalse(location1.equals(null));

        location1 = new ASLocation(world1, 1, 2, 3);
        location2 = new ASLocation(world2, 1, 2, 3);
        location3 = new ASLocation(world3, 2, 3, 4);

        assertFalse(location1.equals(location2));
        assertFalse(location2.equals(location1));
        assertFalse(location1.equals(location3));
        assertFalse(location2.equals(location3));
        assertFalse(location1.equals(null));

        location1 = new ASLocation(world1, 1, 2, 3);
        location2 = new ASLocation(world2, 1, 2, 3);
        location3 = new ASLocation(world1, 2, 3, 4);

        assertFalse(location1.equals(location2));
        assertFalse(location2.equals(location1));
        assertFalse(location1.equals(location3));
        assertFalse(location2.equals(location3));
        assertFalse(location1.equals(null));
    }

}
