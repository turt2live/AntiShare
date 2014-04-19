package com.turt2live.antishare.io.generics;

import com.turt2live.antishare.inventory.ASInventory;
import com.turt2live.antishare.inventory.ASItem;
import com.turt2live.antishare.utils.ASGameMode;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class GenericInventoryStoreTest {

    private static class TestStore extends GenericInventoryStore<ASItem> {

        public TestStore(UUID uuid, String world) {
            super(uuid, world);
        }

        @Override
        protected String getDefaultSerializerClass() {
            return SERIALZER_CLASS;
        }

        @Override
        protected void fillEmpty() {
            for (ASGameMode gamemode : ASGameMode.values()) {
                if (getInventory(gamemode) == null) {
                    setInventory(gamemode, mock(ASInventory.class));
                }
            }
        }

        @Override
        protected void loadAll() {
        }

        @Override
        public void save() {
        }
    }

    private static final String SERIALZER_CLASS = "com.turt2live.serializerdummy";
    private static final UUID UID = UUID.randomUUID();
    private static final String WORLD = "world";
    private static TestStore store;

    @Test
    public void testMisc() {
        assertEquals(UID, store.getUUID());
        assertEquals(WORLD, store.getWorld());
        assertEquals(SERIALZER_CLASS, store.getDefaultSerializerClass());
    }

    @BeforeClass
    public static void before() {
        store = new TestStore(UID, WORLD);
    }

    // TODO: Continue test

}
