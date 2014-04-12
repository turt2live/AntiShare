package com.turt2live.antishare.utils;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ASLocationTest {

    @Test
    public void aTestCreate() {
        for (int x = -2; x < 2; x++) {
            for (int y = -2; y < 2; y++) {
                for (int z = -2; z < 2; z++) {
                    ASLocation location = new ASLocation(x, y, z);
                    assertEquals(x, location.X);
                    assertEquals(y, location.Y);
                    assertEquals(z, location.Z);
                }
            }
        }
    }

    @Test
    public void aTestEquality() {
        ASLocation location1 = new ASLocation(1, 2, 3);
        ASLocation location2 = new ASLocation(1, 2, 3);
        ASLocation location3 = new ASLocation(2, 3, 4);

        assertTrue(location1.equals(location2));
        assertTrue(location2.equals(location1));
        assertFalse(location1.equals(location3));
        assertFalse(location2.equals(location3));
        assertFalse(location1.equals(null));
    }

}
