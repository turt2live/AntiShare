package com.turt2live.antishare.collections.inventory;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.collections.ASInventoryCollection;
import com.turt2live.antishare.inventory.ASInventory;
import com.turt2live.antishare.inventory.ASItem;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class ASInventoryCollectionTest {

    private static ASInventory<ASItem> inv1, inv2, inv3;
    private static ASGameMode unused;

    @Test
    public void testNothing() {
        UUID uuid = UUID.randomUUID();
        ASInventoryCollection<ASItem> collection = new ASInventoryCollection<ASItem>(uuid, new HashMap<ASGameMode, ASInventory<ASItem>>());

        assertEquals(uuid, collection.getPlayer());

        for (ASGameMode gamemode : ASGameMode.values()) {
            assertNull(collection.getInventory(gamemode));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull1() {
        new ASInventoryCollection<ASItem>(null, new HashMap<ASGameMode, ASInventory<ASItem>>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull2() {
        new ASInventoryCollection<ASItem>(UUID.randomUUID(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull13() {
        new ASInventoryCollection<ASItem>(null, null);
    }

    @Test
    public void testFill() {
        Map<ASGameMode, ASInventory<ASItem>> items = new HashMap<ASGameMode, ASInventory<ASItem>>();
        items.put(inv1.getGameMode(), inv1);
        items.put(inv2.getGameMode(), inv2);
        items.put(inv3.getGameMode(), inv3);
        ASInventoryCollection<ASItem> collection = new ASInventoryCollection<ASItem>(UUID.randomUUID(), items);

        assertEquals(inv1, collection.getInventory(inv1.getGameMode()));
        assertEquals(inv2, collection.getInventory(inv2.getGameMode()));
        assertEquals(inv3, collection.getInventory(inv3.getGameMode()));
        assertNull(collection.getInventory(unused));
    }

    @Test
    public void testReassociate() {
        Map<ASGameMode, ASInventory<ASItem>> items = new HashMap<ASGameMode, ASInventory<ASItem>>();
        items.put(inv2.getGameMode(), inv1);
        items.put(inv3.getGameMode(), inv2);
        items.put(unused, inv3);
        ASInventoryCollection<ASItem> collection = new ASInventoryCollection<ASItem>(UUID.randomUUID(), items);

        assertEquals(inv1, collection.getInventory(inv2.getGameMode()));
        assertEquals(inv2, collection.getInventory(inv3.getGameMode()));
        assertEquals(inv3, collection.getInventory(unused));
        assertNull(collection.getInventory(inv1.getGameMode()));

        collection.reassociate();

        assertEquals(inv1, collection.getInventory(inv1.getGameMode()));
        assertEquals(inv2, collection.getInventory(inv2.getGameMode()));
        assertEquals(inv3, collection.getInventory(inv3.getGameMode()));
        assertNull(collection.getInventory(unused));
    }

    @BeforeClass
    public static void preTest() {
        inv1 = mock(ASInventory.class);
        inv2 = mock(ASInventory.class);
        inv3 = mock(ASInventory.class);

        when(inv1.getGameMode()).thenReturn(ASGameMode.ADVENTURE);
        when(inv2.getGameMode()).thenReturn(ASGameMode.CREATIVE);
        when(inv3.getGameMode()).thenReturn(ASGameMode.SURVIVAL);
        unused = ASGameMode.SPECTATOR;
    }

}
