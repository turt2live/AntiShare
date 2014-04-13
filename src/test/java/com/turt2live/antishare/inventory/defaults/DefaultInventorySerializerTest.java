package com.turt2live.antishare.inventory.defaults;

import com.turt2live.antishare.inventory.ASItem;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;


@RunWith(JUnit4.class)
public class DefaultInventorySerializerTest {

    private static DefaultInventorySerializer SERIALIZER;

    @Test
    public void testSerialize() {
        DefaultASItem item1 = new DefaultASItem(12);

        assertNotNull(SERIALIZER.toJson(item1));
    }

    @Test
    public void testDeserialize() {
        DefaultASItem item1 = new DefaultASItem(12);

        assertNotNull(SERIALIZER.fromJson(SERIALIZER.toJson(item1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSerialize1() {
        SERIALIZER.toJson(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSerialize2() {
        SERIALIZER.toJson(mock(ASItem.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDeserialize1() {
        SERIALIZER.fromJson(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDeserialize2() {
        SERIALIZER.fromJson(mock(ASItem.class).toString());
    }

    @BeforeClass
    public static void preTest() {
        SERIALIZER = new DefaultInventorySerializer();
    }

}
