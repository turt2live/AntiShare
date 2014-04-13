package com.turt2live.antishare.engine;

import com.turt2live.antishare.economy.ASEconomy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class EngineTest {

    @Test
    public void testInstance() {
        assertNotNull(Engine.getInstance());
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

        Engine.getInstance().setLogger(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullLogger() {
        Engine.getInstance().setLogger(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddListener() {
        EngineListener listener = mock(EngineListener.class);
        Engine.getInstance().addListener(listener);
        Engine.getInstance().addListener(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullListener() {
        Engine.getInstance().addListener(null);
    }

    @Test
    public void testRemoveListener() {
        EngineListener listener = mock(EngineListener.class);
        Engine.getInstance().removeListener(listener);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullListener() {
        Engine.getInstance().removeListener(null);
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
        assertEquals(created, unloaded);
        assertEquals(fetched, unloaded);

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
    public void testListenerEvents() {
        EngineListener fakeListener = mock(EngineListener.class);
        Engine.getInstance().addListener(fakeListener);
        verifyZeroInteractions(fakeListener);

        verify(fakeListener, atMost(0)).onEngineShutdown();
        verify(fakeListener, atMost(0)).onWorldEngineCreate(any(WorldEngine.class));

        Engine.getInstance().createWorldEngine("test");
        verify(fakeListener, atMost(0)).onEngineShutdown();
        verify(fakeListener, atMost(1)).onWorldEngineCreate(any(WorldEngine.class));
        verify(fakeListener, atLeast(1)).onWorldEngineCreate(any(WorldEngine.class));

        // same world should yield only one call, as it should be cached
        Engine.getInstance().createWorldEngine("test");
        verify(fakeListener, atMost(0)).onEngineShutdown();
        verify(fakeListener, atMost(1)).onWorldEngineCreate(any(WorldEngine.class));
        verify(fakeListener, atLeast(1)).onWorldEngineCreate(any(WorldEngine.class));

        Engine.getInstance().unloadWorldEngine("test"); // Unload ensures we create a new one
        Engine.getInstance().createWorldEngine("test");
        verify(fakeListener, atMost(0)).onEngineShutdown();
        verify(fakeListener, atMost(2)).onWorldEngineCreate(any(WorldEngine.class));
        verify(fakeListener, atLeast(2)).onWorldEngineCreate(any(WorldEngine.class));

        Engine.getInstance().prepareShutdown();
        verify(fakeListener, atMost(1)).onEngineShutdown();
        verify(fakeListener, atMost(2)).onWorldEngineCreate(any(WorldEngine.class));
        verify(fakeListener, atLeast(1)).onEngineShutdown();
        verify(fakeListener, atLeast(2)).onWorldEngineCreate(any(WorldEngine.class));

        // Calling again should ensure that a new engine can be prepared at any time
        Engine.getInstance().prepareShutdown();
        verify(fakeListener, atMost(2)).onEngineShutdown();
        verify(fakeListener, atMost(2)).onWorldEngineCreate(any(WorldEngine.class));
        verify(fakeListener, atLeast(2)).onEngineShutdown();
        verify(fakeListener, atLeast(2)).onWorldEngineCreate(any(WorldEngine.class));

        // Now to see what happens if it's removed
        Engine.getInstance().removeListener(fakeListener);
        verify(fakeListener, atMost(2)).onEngineShutdown();
        verify(fakeListener, atMost(2)).onWorldEngineCreate(any(WorldEngine.class));
        verify(fakeListener, atLeast(2)).onEngineShutdown();
        verify(fakeListener, atLeast(2)).onWorldEngineCreate(any(WorldEngine.class));

        Engine.getInstance().prepareShutdown();
        verify(fakeListener, atMost(2)).onEngineShutdown();
        verify(fakeListener, atMost(2)).onWorldEngineCreate(any(WorldEngine.class));
        verify(fakeListener, atLeast(2)).onEngineShutdown();
        verify(fakeListener, atLeast(2)).onWorldEngineCreate(any(WorldEngine.class));

        Engine.getInstance().unloadWorldEngine("test"); // Unload ensures we create a new one
        Engine.getInstance().createWorldEngine("test");
        verify(fakeListener, atMost(2)).onEngineShutdown();
        verify(fakeListener, atMost(2)).onWorldEngineCreate(any(WorldEngine.class));
        verify(fakeListener, atLeast(2)).onEngineShutdown();
        verify(fakeListener, atLeast(2)).onWorldEngineCreate(any(WorldEngine.class));
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

    @Test(expected = IllegalArgumentException.class)
    public void testSaveInterval() {
        assertEquals(Engine.DEFAULT_SAVE_INTERVAL, Engine.getInstance().getSaveInterval());
        Engine.getInstance().setSaveInterval(Engine.DEFAULT_SAVE_INTERVAL + 1);
        assertEquals(Engine.DEFAULT_SAVE_INTERVAL + 1, Engine.getInstance().getSaveInterval());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveIntervalRange1() {
        Engine.getInstance().setSaveInterval(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveIntervalRange2() {
        Engine.getInstance().setSaveInterval(-1);
    }

}
