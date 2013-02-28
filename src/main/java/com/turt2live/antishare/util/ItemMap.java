package com.turt2live.antishare.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.util.ASMaterialList.ASMaterial;
import com.turt2live.materials.MaterialAPI;

/**
 * Item map from a CSV file
 * 
 * @author turt2live
 */
public class ItemMap {

	private static Map<String, ASMaterial> listing = new HashMap<String, ASMaterial>();

	public static ASMaterial get(String string){
		if(listing.size() <= 0){
			try{
				load();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		if(string == null){
			return null;
		}
		string = string.trim().toLowerCase();
		String[] parts = string.split(":");
		String customData = null;
		String name = parts[0];
		if(parts.length >= 2){
			customData = parts[1];
		}
		ASMaterial asm = listing.get(string);
		if(asm == null){
			Material real = Material.matchMaterial(name);
			if(real != null){
				asm = new ASMaterial();
				asm.id = real.getId();
				short d = -1;
				if(customData != null && MaterialAPI.hasData(real)){
					try{
						d = Short.parseShort(customData);
					}catch(NumberFormatException e){}
				}
				asm.data = d;
			}
		}
		return asm;
	}

	private static void load() throws IOException{
		AntiShare p = AntiShare.p;
		// TODO: Auto update
		File items = new File(p.getDataFolder(), "items.csv");
		if(!items.exists()){
			createFile(items, p);
		}
		BufferedReader in = new BufferedReader(new FileReader(items));
		String line;
		while ((line = in.readLine()) != null){
			if(line.startsWith("#")){
				continue;
			}
			String[] parts = line.split(",");
			if(parts.length < 3 || parts.length > 3){
				continue;
			}
			// 0 = item name
			// 1 = id
			// 2 = meta, * = any
			String name = parts[0].trim().toLowerCase();
			int id = 0;
			short data = 0;
			try{
				id = Integer.parseInt(parts[1].trim());
				String d = parts[2].trim();
				if(d.equalsIgnoreCase("*") || !MaterialAPI.hasData(id)){
					data = -1;
				}else{
					data = Short.parseShort(d);
				}
			}catch(NumberFormatException e){
				continue;
			}
			ASMaterial asMaterial = new ASMaterial();
			asMaterial.id = id;
			asMaterial.data = data;
			listing.put(name, asMaterial);
		}
		in.close();
	}

	private static void createFile(File items, AntiShare p) throws IOException{
		InputStream input = p.getResource("items.csv");
		FileOutputStream out = new FileOutputStream(items);
		byte[] buf = new byte[1024];
		int len;
		while ((len = input.read(buf)) > 0){
			out.write(buf, 0, len);
		}
		out.close();
		input.close();
	}

}
