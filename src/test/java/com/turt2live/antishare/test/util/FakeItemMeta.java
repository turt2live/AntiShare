package com.turt2live.antishare.test.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

public class FakeItemMeta implements Cloneable, ItemMeta {

	private String name = null;
	private List<String> lore = new ArrayList<String>();

	@Override
	public boolean hasDisplayName(){
		return name != null && !name.trim().isEmpty();
	}

	@Override
	public String getDisplayName(){
		return name;
	}

	@Override
	public void setDisplayName(String name){
		this.name = name;
	}

	@Override
	public boolean hasLore(){
		return lore != null && lore.size() > 0;
	}

	@Override
	public List<String> getLore(){
		return lore;
	}

	@Override
	public void setLore(List<String> lore){
		this.lore = lore;
	}

	@Override
	public Map<String, Object> serialize(){
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public boolean hasEnchants(){
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public boolean hasEnchant(Enchantment ench){
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public int getEnchantLevel(Enchantment ench){
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public Map<Enchantment, Integer> getEnchants(){
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public boolean addEnchant(Enchantment ench, int level, boolean ignoreLevelRestriction){
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public boolean removeEnchant(Enchantment ench){
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public FakeItemMeta clone(){
		return this;
	}

}
