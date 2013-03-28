package com.turt2live.antishare.manager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;

// TODO: REMOVE
public class BlockIO {

	public static void tortureTest(int items) throws IOException{
		int iterations = 10;

		Location l = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		GameMode gm = GameMode.SURVIVAL;
		ByteBuffer buffer = ByteBuffer.allocateDirect(17);

		// WRITE
		for(int i = 0; i < iterations; i++){
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
		}

		// READ
		for(int i = 0; i < iterations; i++){
			FileInputStream oos = new FileInputStream("temp2.dat");
			FileChannel fc = oos.getChannel();
			while (fc.read(buffer) > 0){
				buffer.position(0);
				BlockSaver2.getNext(buffer);
				buffer.clear();
			}
			oos.close();
		}
	}

}
