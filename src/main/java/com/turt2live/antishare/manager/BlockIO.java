package com.turt2live.antishare.manager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;

import com.turt2live.antishare.AntiShare;

// TODO: Document
public class BlockIO{

	public static void tortureTest(int items) throws IOException{
		int iterations = 10;

		long binarySize = 0;
		long yamlSize = 0;
		long nioSize = 0;
		long averageBinary = 0;
		long averageYaml = 0;
		long minBinary = Long.MAX_VALUE;
		long maxBinary = Long.MIN_VALUE;
		long minYaml = Long.MAX_VALUE;
		long maxYaml = Long.MIN_VALUE;
		long minNio = Long.MAX_VALUE;
		long maxNio = Long.MIN_VALUE;
		long averageNio = 0;

		Location l = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		GameMode gm = GameMode.SURVIVAL;
		File file = AntiShare.p.getDataFolder().getParentFile().getParentFile();
		ByteBuffer buffer = ByteBuffer.allocateDirect(17);

		System.out.println("======================================");
		System.out.println("SETUP: ");
		System.out.println("Iterations: " + iterations);
		System.out.println("Items:      " + items);
		System.out.println("======================================");

		System.out.println("Running WRITE test...");

		// Test binary
		for(int i = 0; i < iterations; i++){
			System.out.println("Running binary write test " + (i + 1));
			long start = System.currentTimeMillis();

			// Do test
			DataOutputStream oos = new DataOutputStream(new FileOutputStream("temp.dat", false));
			for(int x = 0; x < 16; x++){
				for(int y = 0; y < 256; y++){
					for(int z = 0; z < 16; z++){
						if(x * y * z > items){
							z = 20;
							x = 20;
							y = 300;
							break;
						}
						l.setX(x);
						l.setY(y);
						l.setZ(z);
						BlockSaver.save(oos, l, gm);
					}
				}
			}
			oos.close();

			// Record times
			long totalTime = System.currentTimeMillis() - start;
			if(totalTime < minBinary){
				minBinary = totalTime;
			}else if(totalTime > maxBinary){
				maxBinary = totalTime;
			}
			averageBinary += totalTime;
			System.out.println("Time: " + totalTime);
		}
		averageBinary /= iterations;

		// Test nio
		for(int i = 0; i < iterations; i++){
			System.out.println("Running nio write test " + (i + 1));
			long start = System.currentTimeMillis();

			// Do test
			FileOutputStream oos = new FileOutputStream("temp2.dat", false);
			FileChannel fc = oos.getChannel();
			for(int x = 0; x < 16; x++){
				for(int y = 0; y < 256; y++){
					for(int z = 0; z < 16; z++){
						if(x * y * z > items){
							z = 20;
							x = 20;
							y = 300;
							break;
						}
						l.setX(x);
						l.setY(y);
						l.setZ(z);
						BlockSaver2.save(buffer, l, gm);
						buffer.flip();
						fc.write(buffer);
						buffer.clear();
					}
				}
			}
			oos.close();

			// Record times
			long totalTime = System.currentTimeMillis() - start;
			if(totalTime < minNio){
				minNio = totalTime;
			}else if(totalTime > maxNio){
				maxNio = totalTime;
			}
			averageNio += totalTime;
			System.out.println("Time: " + totalTime);
		}
		averageNio /= iterations;

		// Test yaml
		ChunkWrapper wrapper = null;
		for(int i = 0; i < iterations; i++){
			System.out.println("Running yaml write test " + (i + 1));
			wrapper = new ChunkWrapper(AntiShare.p.getBlockManager(), l.getChunk());
			long start = System.currentTimeMillis();

			// Do test
			for(int x = 0; x < 16; x++){
				for(int y = 0; y < 256; y++){
					for(int z = 0; z < 16; z++){
						if(x * y * z > items){
							z = 20;
							x = 20;
							y = 300;
							break;
						}
						l.setX(x);
						l.setY(y);
						l.setZ(z);
						wrapper.addBlock(gm, l.getBlock());
					}
				}
			}
			wrapper.save(false, false, file, file);

			// Record times
			long totalTime = System.currentTimeMillis() - start;
			if(totalTime < minYaml){
				minYaml = totalTime;
			}else if(totalTime > maxYaml){
				maxYaml = totalTime;
			}
			averageYaml += totalTime;
			System.out.println("Time: " + totalTime);
		}
		averageYaml /= iterations;

		binarySize = new File("temp.dat").length();
		yamlSize = new File("0.0.world.yml").length();
		nioSize = new File("temp2.dat").length();

		// Display results
		System.out.println("(WRITE) BINARY RESULTS: ");
		System.out.println("    AVG: " + averageBinary + "\n    MIN: " + minBinary + "\n    MAX: " + maxBinary);
		System.out.println("(WRITE) YAML RESULTS: ");
		System.out.println("    AVG: " + averageYaml + "\n    MIN: " + minYaml + "\n    MAX: " + maxYaml);
		System.out.println("(WRITE) NIO RESULTS: ");
		System.out.println("    AVG: " + averageNio + "\n    MIN: " + minNio + "\n    MAX: " + maxNio);

		// Reset
		averageBinary = 0;
		averageYaml = 0;
		minBinary = Long.MAX_VALUE;
		maxBinary = Long.MIN_VALUE;
		minYaml = Long.MAX_VALUE;
		maxYaml = Long.MIN_VALUE;
		minNio = Long.MAX_VALUE;
		maxNio = Long.MIN_VALUE;
		averageNio = 0;

		System.out.println("Running READ test...");

		// Test binary
		for(int i = 0; i < iterations; i++){
			System.out.println("Running binary read test " + (i + 1));
			long start = System.currentTimeMillis();

			// Do test
			DataInputStream oos = new DataInputStream(new FileInputStream("temp.dat"));
			while(oos.available() > 0){
				BlockSaver.getNext(oos);
			}
			oos.close();

			// Record times
			long totalTime = System.currentTimeMillis() - start;
			if(totalTime < minBinary){
				minBinary = totalTime;
			}else if(totalTime > maxBinary){
				maxBinary = totalTime;
			}
			averageBinary += totalTime;
			System.out.println("Time: " + totalTime);
		}
		averageBinary /= iterations;

		// Test nio
		for(int i = 0; i < iterations; i++){
			System.out.println("Running nio read test " + (i + 1));
			long start = System.currentTimeMillis();

			// Do test
			FileInputStream oos = new FileInputStream("temp2.dat");
			FileChannel fc = oos.getChannel();
			while(fc.read(buffer) > 0){
				buffer.position(0);
				BlockSaver2.getNext(buffer);
				buffer.clear();
			}
			oos.close();

			// Record times
			long totalTime = System.currentTimeMillis() - start;
			if(totalTime < minNio){
				minNio = totalTime;
			}else if(totalTime > maxNio){
				maxNio = totalTime;
			}
			averageNio += totalTime;
			System.out.println("Time: " + totalTime);
		}
		averageNio /= iterations;

		// Test yaml
		for(int i = 0; i < iterations; i++){
			System.out.println("Running yaml read test " + (i + 1));
			wrapper = new ChunkWrapper(AntiShare.p.getBlockManager(), l.getChunk());
			long start = System.currentTimeMillis();

			// Do test
			wrapper.tempload(true, file);

			// Record times
			long totalTime = System.currentTimeMillis() - start;
			if(totalTime < minYaml){
				minYaml = totalTime;
			}else if(totalTime > maxYaml){
				maxYaml = totalTime;
			}
			averageYaml += totalTime;
			System.out.println("Time: " + totalTime);
		}
		averageYaml /= iterations;

		// Display results
		System.out.println("(READ) BINARY RESULTS: ");
		System.out.println("    AVG: " + averageBinary + "\n    MIN: " + minBinary + "\n    MAX: " + maxBinary);
		System.out.println("(READ) YAML RESULTS: ");
		System.out.println("    AVG: " + averageYaml + "\n    MIN: " + minYaml + "\n    MAX: " + maxYaml);
		System.out.println("(READ) NIO RESULTS: ");
		System.out.println("    AVG: " + averageNio + "\n    MIN: " + minNio + "\n    MAX: " + maxNio);

		System.out.println("======================================");
		System.out.println("FILE SIZES: ");
		System.out.println("Binary:    " + binarySize);
		System.out.println("NIO:       " + nioSize);
		System.out.println("YAML:      " + yamlSize);
		System.out.println("======================================");

		System.out.println("=== TEST COMPLETE ===");
	}

}
