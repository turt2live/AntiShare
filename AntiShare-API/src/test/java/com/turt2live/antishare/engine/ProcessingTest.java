package com.turt2live.antishare.engine;

import com.turt2live.antishare.configuration.MemoryConfiguration;
import com.turt2live.antishare.configuration.groups.GroupManager;
import com.turt2live.antishare.engine.list.BlockTypeList;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.object.ABlock;
import com.turt2live.antishare.object.APlayer;
import com.turt2live.antishare.object.ASLocation;
import com.turt2live.antishare.object.AWorld;
import com.turt2live.antishare.object.attribute.ASGameMode;
import com.turt2live.antishare.object.attribute.TrackedState;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
            return blockTypeList;
        }

        @Override
        public RejectionList getRejectionList(RejectionList.ListType list) {
            return rejectionList;
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
            return ((BlockTypeList) invocation.getMock()).getState((ABlock) invocation.getArguments()[0]) == TrackedState.INCLUDED;
        }
    }

    private static BlockTypeList blockTypeList;
    private static RejectionList rejectionList;
    private static ASLocation test1 = new ASLocation(0, 0, 1);
    private static ASLocation test2 = new ASLocation(0, 1, 0);
    private static ASLocation test3 = new ASLocation(1, 0, 0);
    private static ABlock block;
    private static APlayer player;

    @BeforeClass
    public static void before() {
        blockTypeList = mock(BlockTypeList.class);
        when(blockTypeList.isTracked(any(ABlock.class))).then(new ReturnIsTrackedWorkaround());
//        when(blockTypeList.getState(test1)).thenReturn(TrackedState.INCLUDED);
//        when(blockTypeList.getState(test2)).thenReturn(TrackedState.NEGATED);
//        when(blockTypeList.getState(test3)).thenReturn(TrackedState.NOT_PRESENT);

        rejectionList = mock(RejectionList.class);
        // TODO: Initialize rejection list

        block = mock(ABlock.class);
        when(block.getLocation()).thenReturn(test1);
        when(block.getWorld()).thenReturn(mock(AWorld.class));

        player = mock(APlayer.class);
        when(player.hasPermission(any(String.class))).thenReturn(false);
    }

    @Test
    public void testBlockPlace() {
        // TODO: Test complex logic
//        Engine.getInstance().forceNotInitialized();
//
//        Engine.getInstance().setGroupManager(new TestGroupManager());
//        WorldEngine engineTest = Engine.getInstance().getEngine("test-engine");
//        engineTest.setBlockManager(new MemoryBlockManager());
//
//        engineTest.processBlockPlace(player, block, ASGameMode.ADVENTURE);
//        assertEquals(BlockType.ADVENTURE, engineTest.getBlockManager().getBlockType(block.getLocation()));
//
//        when(block.getLocation()).thenReturn(test2);
//        engineTest.processBlockPlace(player, block, ASGameMode.ADVENTURE);
//        assertEquals(BlockType.UNKNOWN, engineTest.getBlockManager().getBlockType(block.getLocation()));
//
//        engineTest.setBlockManager(new MemoryBlockManager()); // Reset
//
//        when(block.getLocation()).thenReturn(test1);
//        when(player.hasPermission(any(String.class))).thenReturn(true);
//        engineTest.processBlockPlace(player, block, ASGameMode.ADVENTURE);
//        assertEquals(BlockType.UNKNOWN, engineTest.getBlockManager().getBlockType(block.getLocation()));
//
//        when(block.getLocation()).thenReturn(test2);
//        engineTest.processBlockPlace(player, block, ASGameMode.ADVENTURE);
//        assertEquals(BlockType.UNKNOWN, engineTest.getBlockManager().getBlockType(block.getLocation()));
//
//        Engine.getInstance().forceNotInitialized();
    }

}
