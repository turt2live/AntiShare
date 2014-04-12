package com.turt2live.antishare.collections;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ArrayArrayListTest {

    @Test
    public void aTestCreate1() {
        ArrayArrayList<Integer> numbers = new ArrayArrayList<Integer>();
        assertEquals(0, numbers.size());
    }

    @Test
    public void bTestCreate2() {
        ArrayArrayList<Integer> numbers = new ArrayArrayList<Integer>(new Integer[]{9, 10, 11});
        assertEquals(3, numbers.size());
        assertEquals(new Integer(9), numbers.get(0));
        assertEquals(new Integer(10), numbers.get(1));
        assertEquals(new Integer(11), numbers.get(2));
    }

    @Test
    public void cTestCreate3() {
        ArrayArrayList<Integer> numbers = new ArrayArrayList<Integer>(9, 10, 11);
        assertEquals(3, numbers.size());
        assertEquals(new Integer(9), numbers.get(0));
        assertEquals(new Integer(10), numbers.get(1));
        assertEquals(new Integer(11), numbers.get(2));
    }

    @Test
    public void dTestCreate4() {
        ArrayArrayList<Integer> numbers = new ArrayArrayList<Integer>(Arrays.asList(9, 10, 11));
        assertEquals(3, numbers.size());
        assertEquals(new Integer(9), numbers.get(0));
        assertEquals(new Integer(10), numbers.get(1));
        assertEquals(new Integer(11), numbers.get(2));
    }

    @Test
    public void dTestCreate5() {
        new ArrayArrayList<Integer>((List<Integer>) null);
        new ArrayArrayList<Integer>((Integer[]) null);
    }

}
