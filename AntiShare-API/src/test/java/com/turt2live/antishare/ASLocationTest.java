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

package com.turt2live.antishare;

import com.turt2live.antishare.object.ASLocation;
import com.turt2live.antishare.object.AWorld;
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
