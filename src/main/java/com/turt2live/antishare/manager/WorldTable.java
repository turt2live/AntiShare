package com.turt2live.antishare.manager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldTable{

	static Map<Integer, String> worlds = new HashMap<Integer, String>();
	static Map<String, Integer> worldsNames = new HashMap<String, Integer>();
	private static int nextID = Integer.MIN_VALUE;

	public static void load() throws IOException{
		if(worlds.size() <= 0){
			try{
				DataInputStream oos = new DataInputStream(new FileInputStream("worlds.dat"));
				while(oos.available() > 0){
					int id = oos.read();
					int len = oos.read();
					byte[] i = new byte[len];
					oos.read(i);
					String name = new String(i, "UTF-8");
					worlds.put(id, name);
					worldsNames.put(name, id);
					if(id >= nextID){
						nextID = id + 1;
					}
				}
				oos.close();
			}catch(FileNotFoundException e){}
		}
		for(World world : Bukkit.getWorlds()){
			if(!worldsNames.containsKey(world.getName())){
				save(world, nextID);
				String name = world.getName();
				worlds.put(nextID, name);
				worldsNames.put(name, nextID);
				nextID++;
			}
		}
	}

	public static void save(World world, int id) throws IOException{
		DataOutputStream oos = new DataOutputStream(new FileOutputStream("worlds.dat", true));
		oos.write(id);
		byte[] i = world.getName().getBytes("UTF-8");
		oos.write(i.length);
		oos.write(i);
		oos.close();
	}

}
