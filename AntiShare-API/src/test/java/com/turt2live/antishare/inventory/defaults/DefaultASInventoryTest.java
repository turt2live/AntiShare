package com.turt2live.antishare.inventory.defaults;

import com.turt2live.antishare.inventory.ASInventory;
import com.turt2live.antishare.utils.ASGameMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(JUnit4.class)
public class DefaultASInventoryTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCreation1() {
        // A default ASInventory should be a pass-through, therefore it should pass as expected
        new DefaultASInventory(null, "test", ASGameMode.ADVENTURE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreation2() {
        // A default ASInventory should be a pass-through, therefore it should pass as expected
        new DefaultASInventory(UUID.randomUUID(), null, ASGameMode.ADVENTURE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreation3() {
        // A default ASInventory should be a pass-through, therefore it should pass as expected
        new DefaultASInventory(UUID.randomUUID(), "test", null);
    }

    @Test
    public void testClone() {
        DefaultASInventory inventory = new DefaultASInventory(UUID.randomUUID(), "testworld", ASGameMode.ADVENTURE);

        for (int i = 0; i < 10; i++) {
            inventory.set(i, new DefaultASItem(i * 9));
        }

        ASInventory cloned = inventory.clone();
        assertTrue(cloned != null && cloned instanceof DefaultASInventory);

        assertEquals(inventory, cloned);
    }
}
