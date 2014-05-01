package com.turt2live.antishare.engine;

import com.turt2live.antishare.configuration.groups.GroupManager;
import com.turt2live.antishare.economy.ASEconomy;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class EngineTest {

    @BeforeClass
    public static void before() {
        // Force initialization
        Engine.getInstance().setGroupManager(mock(GroupManager.class));
    }

    @Test
    public void testInitialization() {
        Engine.getInstance().forceNotInitialized();
        assertFalse(Engine.getInstance().isReady());
        Engine.getInstance().setGroupManager(mock(GroupManager.class));
        assertTrue(Engine.getInstance().isReady());
    }

    @Test
    public void testInstance() {
        assertNotNull(Engine.getInstance());
    }

    @Test
    public void testGroupManager() {
        assertNotNull(Engine.getInstance().getGroupManager());
        GroupManager mocked1 = mock(GroupManager.class);
        GroupManager mocked2 = mock(GroupManager.class);

        Engine.getInstance().setGroupManager(mocked1);
        assertNotNull(Engine.getInstance().getGroupManager());
        assertEquals(mocked1, Engine.getInstance().getGroupManager());
        assertNotEquals(mocked2, Engine.getInstance().getGroupManager());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullGroupManager() {
        Engine.getInstance().setGroupManager(null);
    }

    @Test
    public void testEconomy() {
        assertNull(Engine.getInstance().getEconomy());

        Engine.getInstance().setEconomy(mock(ASEconomy.class));
        assertNotNull(Engine.getInstance().getEconomy());

        Engine.getInstance().setEconomy(null);
        assertNull(Engine.getInstance().getEconomy());
    }

    @Test
    public void testLogger() {
        Logger logger = Logger.getLogger("test-case");
        assertNotNull(Engine.getInstance().getLogger());

        Engine.getInstance().setLogger(logger);
        assertEquals(logger, Engine.getInstance().getLogger());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLogger() {
        Engine.getInstance().setLogger(null);
    }

    @Test
    public void testWorldEngine() {
        WorldEngine created = Engine.getInstance().createWorldEngine("test");
        WorldEngine fetched = Engine.getInstance().getEngine("test");
        WorldEngine fetched2 = Engine.getInstance().getEngine("test2");

        assertNotNull(created);
        assertNotNull(fetched);
        assertNotNull(fetched2);
        assertEquals(created, fetched);

        Engine.getInstance().unloadWorldEngine("test");
        WorldEngine unloaded = Engine.getInstance().getEngine("test");

        assertNotNull(unloaded);

        // Should be ok to unload an engine twice
        Engine.getInstance().unloadWorldEngine("test");
        Engine.getInstance().unloadWorldEngine("test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWorldEngineNull() {
        Engine.getInstance().getEngine(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWorldEngineNull1() {
        Engine.getInstance().createWorldEngine(null);
    }

    @Test
    public void testWorldEngineNull2() {
        Engine.getInstance().unloadWorldEngine(null);
    }

    @Test
    public void testCacheMaxiumum() {
        assertEquals(Engine.DEFAULT_CACHE_MAXIMUM, Engine.getInstance().getCacheMaximum());
        Engine.getInstance().setCacheMaximum(Engine.DEFAULT_CACHE_MAXIMUM + 1);
        assertEquals(Engine.DEFAULT_CACHE_MAXIMUM + 1, Engine.getInstance().getCacheMaximum());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCacheMaximumRange1() {
        Engine.getInstance().setCacheMaximum(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCacheMaximumRange2() {
        Engine.getInstance().setCacheMaximum(-1);
    }

    @Test
    public void testCacheIncrement() {
        assertEquals(Engine.DEFAULT_CACHE_INCREMENT, Engine.getInstance().getCacheIncrement());
        Engine.getInstance().setCacheIncrement(Engine.DEFAULT_CACHE_INCREMENT + 1);
        assertEquals(Engine.DEFAULT_CACHE_INCREMENT + 1, Engine.getInstance().getCacheIncrement());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCacheIncrementRange1() {
        Engine.getInstance().setCacheIncrement(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCacheIncrementRange2() {
        Engine.getInstance().setCacheIncrement(-1);
    }

    @Test
    public void testSaveInterval() {
        assertEquals(Engine.DEFAULT_SAVE_INTERVAL, Engine.getInstance().getSaveInterval());
        Engine.getInstance().setSaveInterval(Engine.DEFAULT_SAVE_INTERVAL + 1);
        assertEquals(Engine.DEFAULT_SAVE_INTERVAL + 1, Engine.getInstance().getSaveInterval());

        // <=0 means no save
        Engine.getInstance().setSaveInterval(0);
        Engine.getInstance().setSaveInterval(-1);
    }

}
