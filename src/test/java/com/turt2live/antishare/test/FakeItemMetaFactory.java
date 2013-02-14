package com.turt2live.antishare.test;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

// TODO: Test
public class FakeItemMetaFactory implements ItemFactory {

	@Override
	public ItemMeta getItemMeta(Material material){
		return new FakeItemMeta();
	}

	@Override
	public boolean isApplicable(ItemMeta meta, Material material) throws IllegalArgumentException{
		return true;
	}

	@Override
	public boolean isApplicable(ItemMeta meta, ItemStack stack) throws IllegalArgumentException{
		return true;
	}

	@Override
	public boolean equals(ItemMeta meta1, ItemMeta meta2) throws IllegalArgumentException{
		return meta1.equals(meta2);
	}

	@Override
	public ItemMeta asMetaFor(ItemMeta meta, ItemStack stack) throws IllegalArgumentException{
		return meta;
	}

	@Override
	public ItemMeta asMetaFor(ItemMeta meta, Material material) throws IllegalArgumentException{
		return meta;
	}

	@Override
	public Color getDefaultLeatherColor(){
		return Color.AQUA;
	}

}
