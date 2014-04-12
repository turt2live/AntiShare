package com.turt2live.antishare.collections;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SimpleListTest {

    @Test
    public void aTestAdd() {
        SimpleList<Integer> numbers = new SimpleList<Integer>();
        numbers.addItem(19);

        assertTrue(numbers.hasItem(19));
        assertFalse(numbers.hasItem(20));
    }

    @Test
    public void bTestRemove() {
        SimpleList<Integer> numbers = new SimpleList<Integer>();
        numbers.addItem(19);
        numbers.addItem(20);
        numbers.addItem(21);

        numbers.removeItem(10);
        numbers.removeItem(20);

        assertTrue(numbers.hasItem(19));
        assertTrue(numbers.hasItem(21));
        assertFalse(numbers.hasItem(10));
        assertFalse(numbers.hasItem(20));
    }

    @Test
    public void cTestConvert() {
        SimpleList<Integer> numbers = new SimpleList<Integer>();
        numbers.addItem(19);
        numbers.addItem(20);
        numbers.addItem(21);

        List<Integer> list = numbers.getListing();
        assertTrue(list.contains(19));
        assertTrue(list.contains(20));
        assertTrue(list.contains(21));
        assertEquals(3, list.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void dTestModify() {
        SimpleList<Integer> numbers = new SimpleList<Integer>();
        numbers.addItem(19);
        numbers.addItem(20);
        numbers.addItem(21);

        List<Integer> list = numbers.getListing();
        list.add(12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void eTestNull1() {
        SimpleList<Integer> numbers = new SimpleList<Integer>();
        numbers.addItem(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fTestNull2() {
        SimpleList<Integer> numbers = new SimpleList<Integer>();
        numbers.removeItem(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void gTestNull3() {
        SimpleList<Integer> numbers = new SimpleList<Integer>();
        numbers.hasItem(null);
    }

}
