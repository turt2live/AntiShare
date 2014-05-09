package com.turt2live.antishare.io.flatfile;

import com.turt2live.antishare.object.attribute.BlockType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class FileBlockManagerTest {

    private static File folder;
    private static int expectedMin = Integer.MAX_VALUE,
            expectedMax = Integer.MIN_VALUE,
            expectedAvg,
            expectedMostCommon;
    private static Map<Integer, Integer> counts = new HashMap<Integer, Integer>();

    @Test
    public void testRanges() {
        FileBlockManager manager = new FileBlockManager(80, folder);
        assertEquals(expectedMin, manager.getSmallestBlockSize());
        assertEquals(expectedMax, manager.getLargestBlockSize());
        assertEquals(expectedAvg, manager.getAverageBlockSize());
        assertEquals(expectedMostCommon, manager.getMostCommonBlockSize());
        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            assertEquals((int) entry.getValue(), manager.getStoresOfSize(entry.getKey()).size());
        }
    }

    @BeforeClass
    public static void preTest() {
        folder = new File("test_data_2");
        if (folder.exists()) {
            delete(folder);
        }
        folder.mkdirs();

        // Write a bunch of files
        Random random = new Random();
        int total = 0;
        for (int i = 0; i < 100; i++) {
            int size = random.nextInt(100) + 1;
            FileBlockStore store = new FileBlockStore(new File(folder, i + ".dat"), 0, 0, 0, size);
            store.setType(0, 0, 0, BlockType.ADVENTURE); // Required : FileBlockStore doesn't save nothing
            store.save();

            if (size < expectedMin) expectedMin = size;
            if (size > expectedMax) expectedMax = size;
            total += size;
            if (counts.containsKey(size)) counts.put(size, counts.get(size) + 1);
            else counts.put(size, 1);
        }
        expectedAvg = (int) (total / 100.0);
        int val = 0;
        total = 0;
        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > total) {
                total = entry.getValue();
                val = entry.getKey();
            }
        }
        expectedMostCommon = val;
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
