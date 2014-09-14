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

package com.turt2live.antishare.configuration.groups;

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.configuration.BreakSettings;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.engine.list.TrackedTypeList;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class ConsolidatedGroupTest {

    private static List<Group> GROUPS;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setup() {
        GROUPS = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Group group = mock(Group.class);
            when(group.getBlockTrackedList(any(ASGameMode.class))).thenReturn(mock(TrackedTypeList.class));
            when(group.getEntityTrackedList(any(ASGameMode.class))).thenReturn(mock(TrackedTypeList.class));
            when(group.getRejectionList(any(RejectionList.ListType.class))).thenReturn(mock(RejectionList.class));

            GROUPS.add(group);
        }
    }

    @Test
    public void testConsolidation() {
        ConsolidatedGroup group = new ConsolidatedGroup(GROUPS);

        for (ASGameMode gamemode : ASGameMode.values()) {
            assertNotNull(group.getBlockTrackedList(gamemode));
            assertNotNull(group.getEntityTrackedList(gamemode));
        }

        for (RejectionList.ListType type : RejectionList.ListType.values()) {
            assertNotNull(group.getRejectionList(type));
        }

        BreakSettings mock1 = new BreakSettings(true, ASGameMode.ADVENTURE);
        BreakSettings mock2 = new BreakSettings(false, ASGameMode.SPECTATOR);

        when(GROUPS.get(0).getBreakSettings(any(ASGameMode.class), any(ASGameMode.class))).thenReturn(mock1);
        when(GROUPS.get(1).getBreakSettings(any(ASGameMode.class), any(ASGameMode.class))).thenReturn(mock2);

        assertEquals(mock1, group.getBreakSettings(ASGameMode.CREATIVE, ASGameMode.ADVENTURE));

        ASGameMode gm1 = ASGameMode.ADVENTURE;
        ASGameMode gm2 = ASGameMode.CREATIVE;

        when(GROUPS.get(0).getActingMode(any(ASGameMode.class))).thenReturn(gm1);
        when(GROUPS.get(1).getActingMode(any(ASGameMode.class))).thenReturn(gm2);

        assertEquals(gm1, group.getActingMode(ASGameMode.SURVIVAL));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull1() {
        new ConsolidatedGroup((List<Group>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull2() {
        new ConsolidatedGroup((Group) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmpty1() {
        new ConsolidatedGroup(new ArrayList<Group>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmpty2() {
        new ConsolidatedGroup(new Group[] {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmpty3() {
        new ConsolidatedGroup(new Group[] {null, null});
    }

}
