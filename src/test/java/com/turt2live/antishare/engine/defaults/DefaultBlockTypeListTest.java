package com.turt2live.antishare.engine.defaults;

import com.turt2live.antishare.utils.ASLocation;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import static junit.framework.TestCase.assertFalse;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultBlockTypeListTest {

    @Test
    public void aTestIsTracked() {
        DefaultBlockTypeList list = new DefaultBlockTypeList();

        assertFalse(list.isTracked(null));
        assertFalse(list.isTracked(new ASLocation(0, 90, 0)));
    }
}
