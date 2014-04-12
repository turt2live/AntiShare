package com.turt2live.antishare.inventory.defaults;

import com.turt2live.antishare.utils.ASGameMode;
import com.turt2live.antishare.inventory.ASInventory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultASInventoryTest {

    @BeforeClass
    public static void preTest() {
    }

    @AfterClass
    public static void postTest() {
    }

    @Test(expected = IllegalArgumentException.class)
    public void aTestCreation1() {
        // A default ASInventory should be a pass-through, therefore it should pass as expected
        new DefaultASInventory(null, "test", ASGameMode.ADVENTURE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bTestCreation2() {
        // A default ASInventory should be a pass-through, therefore it should pass as expected
        new DefaultASInventory(UUID.randomUUID(), null, ASGameMode.ADVENTURE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cTestCreation3() {
        // A default ASInventory should be a pass-through, therefore it should pass as expected
        new DefaultASInventory(UUID.randomUUID(), "test", null);
    }

    @Test
    public void dTestClone() {
        DefaultASInventory inventory = new DefaultASInventory(UUID.randomUUID(), "testworld", ASGameMode.ADVENTURE);

        for (int i = 0; i < 10; i++) {
            inventory.set(i, new DefaultASItem(i * 9));
        }

        ASInventory cloned = inventory.clone();
        assertTrue(cloned != null && cloned instanceof DefaultASInventory);

        assertEquals(inventory, cloned);
    }

}
