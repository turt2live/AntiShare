package com.turt2live.antishare.tekkitcompat;

import org.bukkit.Material;
import org.bukkit.entity.Entity;

public class ItemFrameLayer {

	public static boolean isItemFrame(Entity entity){
		return entity.getClass().getName().equals("org.bukkit.entity.ItemFrame");
	}

	public static Material getItemFrame(){
		return Material.getMaterial(389);
	}

}
