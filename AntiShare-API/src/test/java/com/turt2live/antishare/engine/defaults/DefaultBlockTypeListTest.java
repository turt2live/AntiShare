package com.turt2live.antishare.engine.defaults;

import com.turt2live.antishare.ASLocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.TestCase.assertFalse;

@RunWith(JUnit4.class)
public class DefaultBlockTypeListTest {

    @Test
    public void testIsTracked() {
        DefaultBlockTypeList list = new DefaultBlockTypeList();

        assertFalse(list.isTracked(null));
        assertFalse(list.isTracked(new ASLocation(0, 90, 0)));
    }
}
