package com.turt2live.antishare.engine;

import com.turt2live.antishare.*;
import com.turt2live.antishare.configuration.MemoryConfiguration;
import com.turt2live.antishare.configuration.groups.GroupManager;
import com.turt2live.antishare.io.memory.MemoryBlockManager;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// Unit tests for processing logic
// All classes used here are tested elsewhere for null values, etc. This is used to ensure that
// the actual logic used is operating correctly.
@RunWith(JUnit4.class)
public class ProcessingTest {

    private class MainGroup extends com.turt2live.antishare.configuration.groups.MainGroup {
        public MainGroup() {
            super(new MemoryConfiguration());
        }

        @Override
        public BlockTypeList getTrackedList(ASGameMode gameMode) {
            return list;
        }
    }

    private class TestGroupManager extends GroupManager {
        @Override
        public void loadAll() {
            mainGroup = new MainGroup();
        }
    }

    private static class ReturnIsTrackedWorkaround implements Answer<Boolean> {
        @Override
        public Boolean answer(InvocationOnMock invocation) throws Throwable {
            // Workaround for ensuring isTracked() works
            return ((BlockTypeList) invocation.getMock()).getState((ASLocation) invocation.getArguments()[0]) == BlockTypeList.TrackedState.INCLUDED;
        }
    }

    private static BlockTypeList list;
    private static ASLocation test1 = new ASLocation(0, 0, 1);
    private static ASLocation test2 = new ASLocation(0, 1, 0);
    private static ASLocation test3 = new ASLocation(1, 0, 0);
    private static ABlock block;
    private static APlayer player;

    @BeforeClass
    public static void before() {
        list = mock(BlockTypeList.class);
        when(list.isTracked(any(ASLocation.class))).then(new ReturnIsTrackedWorkaround());
        when(list.getState(test1)).thenReturn(BlockTypeList.TrackedState.INCLUDED);
        when(list.getState(test2)).thenReturn(BlockTypeList.TrackedState.NEGATED);
        when(list.getState(test3)).thenReturn(BlockTypeList.TrackedState.NOT_PRESENT);

        block = mock(ABlock.class);
        when(block.getLocation()).thenReturn(test1);
        when(block.getWorld()).thenReturn(mock(AWorld.class));

        player = mock(APlayer.class);
        when(player.hasPermission(any(String.class))).thenReturn(false);
    }

    @Test
    public void testBlockPlace() {
        Engine.getInstance().forceNotInitialized();

        Engine.getInstance().setGroupManager(new TestGroupManager());
        WorldEngine engineTest = Engine.getInstance().getEngine("test-engine");
        engineTest.setBlockManager(new MemoryBlockManager());

        engineTest.processBlockPlace(player, block, ASGameMode.ADVENTURE);
        assertEquals(BlockType.ADVENTURE, engineTest.getBlockManager().getBlockType(block.getLocation()));

        when(block.getLocation()).thenReturn(test2);
        engineTest.processBlockPlace(player, block, ASGameMode.ADVENTURE);
        assertEquals(BlockType.UNKNOWN, engineTest.getBlockManager().getBlockType(block.getLocation()));

        engineTest.setBlockManager(new MemoryBlockManager()); // Reset

        when(block.getLocation()).thenReturn(test1);
        when(player.hasPermission(any(String.class))).thenReturn(true);
        engineTest.processBlockPlace(player, block, ASGameMode.ADVENTURE);
        assertEquals(BlockType.UNKNOWN, engineTest.getBlockManager().getBlockType(block.getLocation()));

        when(block.getLocation()).thenReturn(test2);
        engineTest.processBlockPlace(player, block, ASGameMode.ADVENTURE);
        assertEquals(BlockType.UNKNOWN, engineTest.getBlockManager().getBlockType(block.getLocation()));

        Engine.getInstance().forceNotInitialized();
    }

}
