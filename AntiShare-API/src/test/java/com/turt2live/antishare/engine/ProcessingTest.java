/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

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
    private static ABlock test1;
    private static ABlock test2;
    private static ABlock test3;
    private static ABlock block;
    private static APlayer player;

    @BeforeClass
    public static void before() {
        blockTypeList = mock(BlockTypeList.class);
        when(blockTypeList.isTracked(any(ABlock.class))).then(new ReturnIsTrackedWorkaround());
        when(blockTypeList.getState(test1)).thenReturn(TrackedState.INCLUDED);
        when(blockTypeList.getState(test2)).thenReturn(TrackedState.NEGATED);
        when(blockTypeList.getState(test3)).thenReturn(TrackedState.NOT_PRESENT);

        test1 = mock(ABlock.class);
        test2 = mock(ABlock.class);
        test3 = mock(ABlock.class);

        rejectionList = mock(RejectionList.class);
        // TODO: Initialize rejection list (if needed)

        block = mock(ABlock.class);
        when(block.getLocation()).thenReturn(new ASLocation(0, 0, 1));
        when(block.getWorld()).thenReturn(mock(AWorld.class));

        player = mock(APlayer.class);
        when(player.hasPermission(any(String.class))).thenReturn(false);
    }

}
