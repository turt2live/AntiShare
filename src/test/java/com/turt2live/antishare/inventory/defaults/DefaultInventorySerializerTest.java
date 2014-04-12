package com.turt2live.antishare.inventory.defaults;

import com.turt2live.antishare.inventory.ASItem;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;


@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultInventorySerializerTest {

    private static DefaultInventorySerializer SERIALIZER;

    @Test
    public void aTestSerialize() {
        DefaultASItem item1 = new DefaultASItem(12);

        assertNotNull(SERIALIZER.toJson(item1));
    }

    @Test
    public void bTestDeserialize() {
        DefaultASItem item1 = new DefaultASItem(12);

        assertNotNull(SERIALIZER.fromJson(SERIALIZER.toJson(item1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void cTestInvalidSerialize1() {
        SERIALIZER.toJson(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void dTestInvalidSerialize2() {
        SERIALIZER.toJson(mock(ASItem.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void eTestInvalidDeserialize1() {
        SERIALIZER.fromJson(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fTestInvalidDeserialize2() {
        SERIALIZER.fromJson(mock(ASItem.class).toString());
    }

    @BeforeClass
    public static void preTest() {
        SERIALIZER = new DefaultInventorySerializer();
    }

}
