package com.turt2live.antishare.engine;

import com.turt2live.antishare.object.ABlock;
import com.turt2live.antishare.object.APlayer;
import com.turt2live.antishare.object.attribute.ASGameMode;
import com.turt2live.antishare.configuration.groups.GroupManager;
import com.turt2live.antishare.io.BlockManager;
import com.turt2live.antishare.io.memory.MemoryBlockManager;
import junit.framework.AssertionFailedError;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    public void testProcessNull() {
        APlayer player;
        ABlock block;
        ASGameMode gameMode;
        for (boolean flag1 = true; flag1; flag1 = !flag1) {
            for (boolean flag2 = true; flag2; flag2 = !flag2) {
                for (boolean flag3 = true; flag3; flag3 = !flag3) {
                    player = flag1 ? mock(APlayer.class) : null;
                    block = flag2 ? mock(ABlock.class) : null;
                    gameMode = flag3 ? ASGameMode.ADVENTURE : null;

                    if (block != null) {
                        when(block.getChestType()).thenReturn(ABlock.ChestType.NONE);
                    }

                    boolean expected = player == null || block == null || gameMode == null;
                    try {
                        testEngine.processBlockPlace(player, block, gameMode);
                        if (expected) throw new AssertionFailedError("Expected an exception");
                    } catch (IllegalArgumentException e) {
                        if (!expected) throw new AssertionFailedError("Expected no exception");
                    }
                }
            }
        }
    }

}
