package com.turt2live.antishare.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.turt2live.antishare.AntiShare;

/**
 * Material list for items
 * 
 * @author turt2live
 */
public class ASMaterialList {

	public static class ASMaterial {
		int id;
		short data; // -1 = any
	}

	private Map<Integer, List<ASMaterial>> listing = new HashMap<Integer, List<ASMaterial>>();

	/**
	 * Creates a new material list
	 * 
	 * @param strings the list of strings
	 */
	// TODO: Implement into ASConfig
	public ASMaterialList(List<String> strings){
		if(strings == null){
			throw new IllegalArgumentException("Null arguments are not allowed");
		}
		AntiShare p = AntiShare.p;
		for(String s : strings){
			ASMaterial asm = ItemMap.get(s);
			if(asm == null){
				p.getLogger().warning(p.getMessages().getMessage("unknown-material", s));
				continue;
			}
			List<ASMaterial> materials = new ArrayList<ASMaterial>();
			materials.add(asm);
			if(listing.containsKey(asm.id)){
				materials.addAll(listing.get(asm.id));
			}
			listing.put(asm.id, materials);
		}
	}

	public boolean has(Material material){
		if(material == null){
			return false;
		}
		return listing.containsKey(material.getId());
	}

	public boolean has(Block block){
		if(block == null){
			return false;
		}
		Material material = block.getType();
		short data = block.getData();
		List<ASMaterial> asMaterials = listing.get(material);
		if(asMaterials == null){
			return false;
		}
		for(ASMaterial m : asMaterials){
			if(m.id == material.getId() && m.data == data){
				return true;
			}
		}
		return false;
	}

}
