/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.engine;

import com.turt2live.antishare.configuration.Configuration;
import com.turt2live.antishare.configuration.MemoryConfiguration;
import com.turt2live.antishare.configuration.groups.GroupManager;
import com.turt2live.antishare.io.InventoryManager;
import com.turt2live.antishare.object.AWorld;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class EngineTest {

    @BeforeClass
    public static void before() {
        // Force initialization
        Engine.getInstance().setGroupManager(mock(GroupManager.class));
        Engine.getInstance().setWorldProvider(mock(WorldProvider.class));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testInitialization() {
        Engine.getInstance().forceNotInitialized();
        assertFalse(Engine.getInstance().isReady());
        Engine.getInstance().setGroupManager(mock(GroupManager.class));
        assertFalse(Engine.getInstance().isReady());
        Engine.getInstance().setWorldProvider(mock(WorldProvider.class));
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
    public void testWorldProvider() {
        assertNotNull(Engine.getInstance().getWorldProvider());
        WorldProvider mocked1 = mock(WorldProvider.class);
        WorldProvider mocked2 = mock(WorldProvider.class);

        Engine.getInstance().setWorldProvider(mocked1);
        assertNotNull(Engine.getInstance().getWorldProvider());
        assertEquals(mocked1, Engine.getInstance().getWorldProvider());
        assertNotEquals(mocked2, Engine.getInstance().getWorldProvider());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWorldProvider() {
        Engine.getInstance().setWorldProvider(null);
    }

    @Test
    public void testInventoryManager() {
        assertNotNull(Engine.getInstance().getInventoryManager());
        InventoryManager mocked1 = mock(InventoryManager.class);
        InventoryManager mocked2 = mock(InventoryManager.class);

        Engine.getInstance().setInventoryManager(mocked1);
        assertNotNull(Engine.getInstance().getInventoryManager());
        assertEquals(mocked1, Engine.getInstance().getInventoryManager());
        assertNotEquals(mocked2, Engine.getInstance().getInventoryManager());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInventoryManager() {
        Engine.getInstance().setInventoryManager(null);
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
    public void testGetWorld() {
        WorldProvider provider = mock(WorldProvider.class);
        when(provider.getWorld(anyString())).thenReturn(null);
        when(provider.getWorld("world1")).thenReturn(mock(AWorld.class));

        Engine.getInstance().setWorldProvider(provider);
        assertNotNull(Engine.getInstance().getWorld("world1"));
        verify(provider).getWorld("world1");

        assertNull(Engine.getInstance().getWorld("other"));
        verify(provider).getWorld("other");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullGetWorld() {
        Engine.getInstance().getWorld(null);
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

    @Test
    public void testConfiguration() {
        assertNotNull(Engine.getInstance().getConfiguration());

        Configuration config = mock(Configuration.class);
        Engine.getInstance().setConfiguration(config);

        assertEquals(config, Engine.getInstance().getConfiguration());
        verify(config).load();

        Engine.getInstance().setConfiguration(new MemoryConfiguration());
        assertTrue(Engine.getInstance().getConfiguration() instanceof MemoryConfiguration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullConfiguration() {
        Engine.getInstance().setConfiguration(null);
    }

}
