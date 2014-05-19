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

package com.turt2live.antishare.io.flatfile;

import com.turt2live.antishare.object.attribute.ObjectType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileBlockStoreTest {

    private static File folder;
    private static File testFile1;
    private static ObjectType[][][] expected;

    @Test
    public void aTestWrite() {
        FileBlockStore store = new FileBlockStore(testFile1, 10, 11, 12, 60);
        Random random = new Random();
        ObjectType[] types = ObjectType.values();
        expected = new ObjectType[60][60][60];
        for (int x = 0; x < 60; x++)
            for (int y = 0; y < 60; y++)
                for (int z = 0; z < 60; z++) {
                    ObjectType type = types[random.nextInt(types.length)];
                    store.setType(600 + x, 660 + y, 720 + z, type);
                    expected[x][y][z] = type;
                }
        store.save();
    }

    @Test
    public void bTestRead() {
        FileBlockStore store = new FileBlockStore(testFile1);
        store.load();
        for (int x = 0; x < 60; x++)
            for (int y = 0; y < 60; y++)
                for (int z = 0; z < 60; z++) {
                    assertEquals(expected[x][y][z], store.getType(600 + x, 660 + y, 720 + z));
                }
    }

    @Test
    public void cTestHeader() {
        FileBlockStore store = new FileBlockStore(testFile1);
        assertEquals(10, store.header()[0]);
        assertEquals(11, store.header()[1]);
        assertEquals(12, store.header()[2]);
        assertEquals(60, store.header()[3]);

        store = new FileBlockStore(testFile1, 10, 20, 30, 40);
        assertEquals(10, store.header()[0]);
        assertEquals(20, store.header()[1]);
        assertEquals(30, store.header()[2]);
        assertEquals(40, store.header()[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void dTestWriteOverflow() {
        // Intentionally try to write past the block size
        FileBlockStore store = new FileBlockStore(testFile1, 10, 11, 12, 60);
        store.setType(6000, 6600, 7200, ObjectType.ADVENTURE);
        store.save();
    }

    @Test(expected = IllegalArgumentException.class)
    public void eTestReadOverflow() {
        // Intentionally try to read past the block size
        FileBlockStore store = new FileBlockStore(testFile1, 10, 11, 12, 60);
        store.getType(6000, 6600, 7200);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fTestWriteUnderflow() {
        // Intentionally try to write below the block size
        FileBlockStore store = new FileBlockStore(testFile1, 10, 11, 12, 60);
        store.setType(1, 1, 1, ObjectType.CREATIVE);
        store.save();
    }

    @Test(expected = IllegalArgumentException.class)
    public void gTestReadOverflow() {
        // Intentionally try to read under the block size
        FileBlockStore store = new FileBlockStore(testFile1, 10, 11, 12, 60);
        ObjectType[] types = ObjectType.values();
        store.getType(-1, -1, -1);
    }

    @Test
    public void hTestWriteNothing() {
        // Attempt to write nothing, the file should not be created
        File file = new File(folder, "write_nothing_test.dat");
        FileBlockStore store = new FileBlockStore(file, 9, 8, 7, 6);
        store.save();

        assertFalse(file.exists());
    }

    @BeforeClass
    public static void preTest() {
        folder = new File("test_data");
        if (folder.exists()) {
            delete(folder);
        }
        folder.mkdirs();
        testFile1 = new File(folder, "test1.dat");
    }

    @AfterClass
    public static void postTest() {
        if (folder != null && folder.exists()) {
            delete(folder);
        }
    }

    private static void delete(File folder) {
        File[] files = folder.listFiles();
        for (File f : files) {
            if (f.isDirectory()) delete(f);
            else f.delete();
        }
        folder.delete();
    }
}
