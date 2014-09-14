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

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.configuration.groups.GroupManager;
import com.turt2live.antishare.io.BlockManager;
import com.turt2live.antishare.io.memory.MemoryBlockManager;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class WorldEngineTest {

    private static WorldEngine testEngine;

    @BeforeClass
    public static void before() {
        testEngine = new WorldEngine("test");

        // Force initialization
        Engine.getInstance().setGroupManager(mock(GroupManager.class));
    }

    @Test
    public void testInstance1() {
        WorldEngine engine = new WorldEngine("test");
        assertEquals("test", engine.getWorldName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInstance2() {
        new WorldEngine(null);
    }

    @Test
    public void testBlockManager() {
        assertTrue(testEngine.getBlockManager() instanceof MemoryBlockManager);

        BlockManager manager = mock(BlockManager.class);
        testEngine.setBlockManager(manager);
        assertEquals(manager, testEngine.getBlockManager());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockManagerNull() {
        testEngine.setBlockManager(null);
    }

    @Test
    public void testBlockList() {
        for (ASGameMode gameMode : ASGameMode.values()) {
            assertNotNull(testEngine.getTrackedBlocks(gameMode));
        }
        // No further tests needed, this is handled by the block type list (or the consolidation)
    }

}
