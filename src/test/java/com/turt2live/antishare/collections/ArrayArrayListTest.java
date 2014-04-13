package com.turt2live.antishare.collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ArrayArrayListTest {

    @Test
    public void testCreate1() {
        ArrayArrayList<Integer> numbers = new ArrayArrayList<Integer>();
        assertEquals(0, numbers.size());
    }

    @Test
    public void testCreate2() {
        ArrayArrayList<Integer> numbers = new ArrayArrayList<Integer>(new Integer[]{9, 10, 11});
        assertEquals(3, numbers.size());
        assertEquals(new Integer(9), numbers.get(0));
        assertEquals(new Integer(10), numbers.get(1));
        assertEquals(new Integer(11), numbers.get(2));
    }

    @Test
    public void testCreate3() {
        ArrayArrayList<Integer> numbers = new ArrayArrayList<Integer>(9, 10, 11);
        assertEquals(3, numbers.size());
        assertEquals(new Integer(9), numbers.get(0));
        assertEquals(new Integer(10), numbers.get(1));
        assertEquals(new Integer(11), numbers.get(2));
    }

    @Test
    public void testCreate4() {
        ArrayArrayList<Integer> numbers = new ArrayArrayList<Integer>(Arrays.asList(9, 10, 11));
        assertEquals(3, numbers.size());
        assertEquals(new Integer(9), numbers.get(0));
        assertEquals(new Integer(10), numbers.get(1));
        assertEquals(new Integer(11), numbers.get(2));
    }

    @Test
    public void testCreate5() {
        new ArrayArrayList<Integer>((List<Integer>) null);
        new ArrayArrayList<Integer>((Integer[]) null);
    }

}
