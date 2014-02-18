package com.turt2live.antishare.io.flatfile;

import com.turt2live.antishare.BlockType;
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
    private static BlockType[][][] expected;

    @BeforeClass
    public static void preTest() {
        folder = new File("test_data");
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

    @Test
    public void aTestWrite() {
        FileBlockStore store = new FileBlockStore(testFile1, 10, 11, 12, 60);
        Random random = new Random();
        BlockType[] types = BlockType.values();
        expected = new BlockType[60][60][60];
        for (int x = 0; x < 60; x++)
            for (int y = 0; y < 60; y++)
                for (int z = 0; z < 60; z++) {
                    BlockType type = types[random.nextInt(types.length)];
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
                    BlockType actual = store.getType(600 + x, 660 + y, 720 + z);
                    assertEquals(expected[x][y][z], actual);
                }
    }

    @Test
    public void cTestHeader() {
        FileBlockStore store = new FileBlockStore(testFile1);
        store.loadHeader();
        assertEquals(10, store.getHeader()[0]);
        assertEquals(11, store.getHeader()[1]);
        assertEquals(12, store.getHeader()[2]);
        assertEquals(60, store.getHeader()[3]);

        store = new FileBlockStore(testFile1, 10, 20, 30, 40);
        store.loadHeader();
        assertEquals(10, store.getHeader()[0]);
        assertEquals(11, store.getHeader()[1]);
        assertEquals(12, store.getHeader()[2]);
        assertEquals(60, store.getHeader()[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void dTestWriteOverflow() {
        // Intentionally try to write past the block size
        FileBlockStore store = new FileBlockStore(testFile1, 10, 11, 12, 60);
        store.setType(6000, 6600, 7200, BlockType.ADVENTURE);
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
        store.setType(1, 1, 1, BlockType.CREATIVE);
        store.save();
    }

    @Test(expected = IllegalArgumentException.class)
    public void gTestReadOverflow() {
        // Intentionally try to read under the block size
        FileBlockStore store = new FileBlockStore(testFile1, 10, 11, 12, 60);
        BlockType[] types = BlockType.values();
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
}
