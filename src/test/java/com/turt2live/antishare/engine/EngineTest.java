package com.turt2live.antishare.engine;

import com.turt2live.antishare.economy.ASEconomy;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EngineTest {

    @Test
    public void aTestInstance() {
        assertNotNull(Engine.getInstance());
    }

    @Test
    public void bTestEconomy() {
        assertNull(Engine.getInstance().getEconomy());

        Engine.getInstance().setEconomy(mock(ASEconomy.class));
        assertNotNull(Engine.getInstance().getEconomy());

        Engine.getInstance().setEconomy(null);
        assertNull(Engine.getInstance().getEconomy());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cTestLogger() {
        Logger logger = Logger.getLogger("test-case");
        assertNotNull(Engine.getInstance().getLogger());

        Engine.getInstance().setLogger(logger);
        assertEquals(logger, Engine.getInstance().getLogger());

        Engine.getInstance().setLogger(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void dTestAddListener() {
        EngineListener listener = mock(EngineListener.class);
        Engine.getInstance().addListener(listener);
        Engine.getInstance().addListener(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void eTestRemoveListener() {
        EngineListener listener = mock(EngineListener.class);
        Engine.getInstance().removeListener(listener);
        Engine.getInstance().removeListener(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fTestWorldEngine() {
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

        Engine.getInstance().getEngine(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void gTestWorldEngineNull1() {
        Engine.getInstance().createWorldEngine(null);
    }

    @Test
    public void hTestWorldEngineNull2() {
        Engine.getInstance().unloadWorldEngine(null);
    }

    @Test
    public void fTestListenerEvents() {
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

    @Test(expected = IllegalArgumentException.class)
    public void gTestCacheMaxiumum() {
        assertEquals(Engine.DEFAULT_CACHE_MAXIMUM, Engine.getInstance().getCacheMaximum());
        Engine.getInstance().setCacheMaximum(Engine.DEFAULT_CACHE_MAXIMUM + 1);
        assertEquals(Engine.DEFAULT_CACHE_MAXIMUM + 1, Engine.getInstance().getCacheMaximum());
        Engine.getInstance().setCacheIncrement(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hTestCacheMaximumRange1() {
        Engine.getInstance().setCacheIncrement(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void iTestCacheIncrement() {
        assertEquals(Engine.DEFAULT_CACHE_INCREMENT, Engine.getInstance().getCacheIncrement());
        Engine.getInstance().setCacheIncrement(Engine.DEFAULT_CACHE_INCREMENT + 1);
        assertEquals(Engine.DEFAULT_CACHE_INCREMENT + 1, Engine.getInstance().getCacheIncrement());
        Engine.getInstance().setCacheIncrement(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void jTestCacheIncrementRange1() {
        Engine.getInstance().setCacheIncrement(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void kTestSaveInterval() {
        assertEquals(Engine.DEFAULT_SAVE_INTERVAL, Engine.getInstance().getSaveInterval());
        Engine.getInstance().setSaveInterval(Engine.DEFAULT_SAVE_INTERVAL + 1);
        assertEquals(Engine.DEFAULT_SAVE_INTERVAL + 1, Engine.getInstance().getSaveInterval());
        Engine.getInstance().setSaveInterval(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void lTestSaveIntervalRange1() {
        Engine.getInstance().setSaveInterval(0);
    }

}
