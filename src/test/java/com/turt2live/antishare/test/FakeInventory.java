package com.turt2live.antishare.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class FakeInventory implements PlayerInventory {

	private Map<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();

	@Override
	public int getSize(){
		return 36;
	}

	@Override
	public ItemStack getItem(int index){
		return items.get(index);
	}

	@Override
	public void setItem(int index, ItemStack item){
		items.put(index, item);
	}

	@Override
	public HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException{
		int count = 0;
		HashMap<Integer, ItemStack> missing = new HashMap<Integer, ItemStack>();
		for(ItemStack item : items){
			int slot = firstEmpty();
			if(slot == -1){
				missing.put(count, item);
			}else{
				this.items.put(slot, item);
			}
			count++;
		}
		return missing;
	}

	@Override
	public ItemStack[] getContents(){
		List<ItemStack> items = new ArrayList<ItemStack>();
		for(Map.Entry<Integer, ItemStack> entry : this.items.entrySet()){
			items.add(entry.getValue());
		}
		return items.toArray(new ItemStack[items.size()]);
	}

	@Override
	public void setContents(ItemStack[] items) throws IllegalArgumentException{
		this.items.clear();
		addItem(items);
	}

	@Override
	public boolean contains(Material material) throws IllegalArgumentException{
		for(Map.Entry<Integer, ItemStack> entry : items.entrySet()){
			if(entry.getValue() != null && entry.getValue().getType() == material){
				return true;
			}
		}
		return false;
	}

	@Override
	public int firstEmpty(){
		for(int i = 0; i < getSize(); i++){
			if(!items.containsKey(i)){
				return i;
			}
		}
		return -1;
	}

	@Override
	public void clear(int index){
		items.remove(index);
	}

	@Override
	public void clear(){
		items.clear();
	}

	@Override
	public InventoryType getType(){
		return InventoryType.PLAYER;
	}

	@Override
	public int getMaxStackSize(){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public void setMaxStackSize(int size){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public String getName(){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException{
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public boolean contains(int materialId){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public boolean contains(ItemStack item){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public boolean contains(int materialId, int amount){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public boolean contains(Material material, int amount) throws IllegalArgumentException{
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public boolean contains(ItemStack item, int amount){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public boolean containsAtLeast(ItemStack item, int amount){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(int materialId){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException{
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(ItemStack item){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public int first(int materialId){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public int first(Material material) throws IllegalArgumentException{
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public int first(ItemStack item){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public void remove(int materialId){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public void remove(Material material) throws IllegalArgumentException{
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public void remove(ItemStack item){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public List<HumanEntity> getViewers(){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public String getTitle(){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public ListIterator<ItemStack> iterator(){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public ListIterator<ItemStack> iterator(int index){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public ItemStack[] getArmorContents(){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public ItemStack getHelmet(){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public ItemStack getChestplate(){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public ItemStack getLeggings(){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public ItemStack getBoots(){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public void setArmorContents(ItemStack[] items){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public void setHelmet(ItemStack helmet){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public void setChestplate(ItemStack chestplate){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public void setLeggings(ItemStack leggings){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public void setBoots(ItemStack boots){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public ItemStack getItemInHand(){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public void setItemInHand(ItemStack stack){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public int getHeldItemSlot(){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public int clear(int id, int data){
		throw new UnsupportedOperationException("Not handled");
	}

	@Override
	public HumanEntity getHolder(){
		throw new UnsupportedOperationException("Not handled");
	}

}
