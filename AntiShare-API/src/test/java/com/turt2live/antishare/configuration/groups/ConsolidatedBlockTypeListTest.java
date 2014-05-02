package com.turt2live.antishare.configuration.groups;

import com.turt2live.antishare.ASLocation;
import com.turt2live.antishare.TrackedState;
import com.turt2live.antishare.engine.BlockTypeList;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class ConsolidatedBlockTypeListTest {

    private static class ReturnIsTrackedWorkaround implements Answer<Boolean> {
        @Override
        public Boolean answer(InvocationOnMock invocation) throws Throwable {
            // Workaround for ensuring isTracked() works
            return ((BlockTypeList) invocation.getMock()).getState((ASLocation) invocation.getArguments()[0]) == TrackedState.INCLUDED;
        }
    }

    private static BlockTypeList list1, list2, list3, list4;
    private static ConsolidatedBlockTypeList consolidated;
    private static final ASLocation testLocation = new ASLocation(null, 0, 0, 0);

    @BeforeClass
    public static void beforeTest() {
        list1 = mock(BlockTypeList.class);
        list2 = mock(BlockTypeList.class);
        list3 = mock(BlockTypeList.class);
        list4 = mock(BlockTypeList.class);

        consolidated = new ConsolidatedBlockTypeList(list1, list2, list3, list4);

        when(list1.isTracked(any(ASLocation.class))).then(new ReturnIsTrackedWorkaround());
        when(list2.isTracked(any(ASLocation.class))).then(new ReturnIsTrackedWorkaround());
        when(list3.isTracked(any(ASLocation.class))).then(new ReturnIsTrackedWorkaround());
        when(list4.isTracked(any(ASLocation.class))).then(new ReturnIsTrackedWorkaround());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEmpty1() {
        new ConsolidatedBlockTypeList(new ArrayList<BlockTypeList>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEmpty2() {
        new ConsolidatedBlockTypeList(new BlockTypeList[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNull1() {
        new ConsolidatedBlockTypeList((List<BlockTypeList>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNull2() {
        new ConsolidatedBlockTypeList((BlockTypeList[]) null);
    }

    @Test
    public void testBias() throws Exception {
        // Ensure equal balance of tracked & negated results in "not tracked"
        when(list1.getState(any(ASLocation.class))).thenReturn(TrackedState.INCLUDED);
        when(list2.getState(any(ASLocation.class))).thenReturn(TrackedState.INCLUDED);
        when(list3.getState(any(ASLocation.class))).thenReturn(TrackedState.NEGATED);
        when(list4.getState(any(ASLocation.class))).thenReturn(TrackedState.NEGATED);

        assertEquals(TrackedState.NOT_PRESENT, consolidated.getState(testLocation));
        assertEquals(false, consolidated.isTracked(testLocation));

        // When nothing is included, result should be "not tracked"
        when(list1.getState(any(ASLocation.class))).thenReturn(TrackedState.NOT_PRESENT);
        when(list2.getState(any(ASLocation.class))).thenReturn(TrackedState.NOT_PRESENT);
        when(list3.getState(any(ASLocation.class))).thenReturn(TrackedState.NOT_PRESENT);
        when(list4.getState(any(ASLocation.class))).thenReturn(TrackedState.NOT_PRESENT);

        assertEquals(TrackedState.NOT_PRESENT, consolidated.getState(testLocation));
        assertEquals(false, consolidated.isTracked(testLocation));

        // When at least one is included, result should be "included"
        when(list1.getState(any(ASLocation.class))).thenReturn(TrackedState.INCLUDED);
        when(list2.getState(any(ASLocation.class))).thenReturn(TrackedState.NOT_PRESENT);
        when(list3.getState(any(ASLocation.class))).thenReturn(TrackedState.NOT_PRESENT);
        when(list4.getState(any(ASLocation.class))).thenReturn(TrackedState.NOT_PRESENT);

        assertEquals(TrackedState.INCLUDED, consolidated.getState(testLocation));
        assertEquals(true, consolidated.isTracked(testLocation));

        // Ensure equal balance of tracked & negated results in "not tracked"
        when(list1.getState(any(ASLocation.class))).thenReturn(TrackedState.INCLUDED);
        when(list2.getState(any(ASLocation.class))).thenReturn(TrackedState.NEGATED);
        when(list3.getState(any(ASLocation.class))).thenReturn(TrackedState.NOT_PRESENT);
        when(list4.getState(any(ASLocation.class))).thenReturn(TrackedState.NOT_PRESENT);

        assertEquals(TrackedState.NOT_PRESENT, consolidated.getState(testLocation));
        assertEquals(false, consolidated.isTracked(testLocation));

        // When at least one is negated, result should be "negated"
        when(list1.getState(any(ASLocation.class))).thenReturn(TrackedState.NEGATED);
        when(list2.getState(any(ASLocation.class))).thenReturn(TrackedState.NOT_PRESENT);
        when(list3.getState(any(ASLocation.class))).thenReturn(TrackedState.NOT_PRESENT);
        when(list4.getState(any(ASLocation.class))).thenReturn(TrackedState.NOT_PRESENT);

        assertEquals(TrackedState.NEGATED, consolidated.getState(testLocation));
        assertEquals(false, consolidated.isTracked(testLocation));

        // When a majority are negated, the result should be "negated"
        when(list1.getState(any(ASLocation.class))).thenReturn(TrackedState.NEGATED);
        when(list2.getState(any(ASLocation.class))).thenReturn(TrackedState.NEGATED);
        when(list3.getState(any(ASLocation.class))).thenReturn(TrackedState.NEGATED);
        when(list4.getState(any(ASLocation.class))).thenReturn(TrackedState.INCLUDED);

        assertEquals(TrackedState.NEGATED, consolidated.getState(testLocation));
        assertEquals(false, consolidated.isTracked(testLocation));

        // When a majority are negated, the result should be "negated"
        when(list1.getState(any(ASLocation.class))).thenReturn(TrackedState.NEGATED);
        when(list2.getState(any(ASLocation.class))).thenReturn(TrackedState.NEGATED);
        when(list3.getState(any(ASLocation.class))).thenReturn(TrackedState.NEGATED);
        when(list4.getState(any(ASLocation.class))).thenReturn(TrackedState.NOT_PRESENT);

        assertEquals(TrackedState.NEGATED, consolidated.getState(testLocation));
        assertEquals(false, consolidated.isTracked(testLocation));

        // When a majority are included, the result should be "included"
        when(list1.getState(any(ASLocation.class))).thenReturn(TrackedState.INCLUDED);
        when(list2.getState(any(ASLocation.class))).thenReturn(TrackedState.INCLUDED);
        when(list3.getState(any(ASLocation.class))).thenReturn(TrackedState.INCLUDED);
        when(list4.getState(any(ASLocation.class))).thenReturn(TrackedState.NEGATED);

        assertEquals(TrackedState.INCLUDED, consolidated.getState(testLocation));
        assertEquals(true, consolidated.isTracked(testLocation));

        // When a majority are included, the result should be "included"
        when(list1.getState(any(ASLocation.class))).thenReturn(TrackedState.INCLUDED);
        when(list2.getState(any(ASLocation.class))).thenReturn(TrackedState.INCLUDED);
        when(list3.getState(any(ASLocation.class))).thenReturn(TrackedState.INCLUDED);
        when(list4.getState(any(ASLocation.class))).thenReturn(TrackedState.NOT_PRESENT);

        assertEquals(TrackedState.INCLUDED, consolidated.getState(testLocation));
        assertEquals(true, consolidated.isTracked(testLocation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull1() {
        consolidated.isTracked(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull2() {
        consolidated.getState(null);
    }
}
