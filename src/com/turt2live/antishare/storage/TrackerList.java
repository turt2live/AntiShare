package com.turt2live.antishare.storage;

import java.util.Vector;

import org.bukkit.Material;

import com.turt2live.antishare.AntiShare;

public class TrackerList {

	private Vector<Integer> tracked_blocks = new Vector<Integer>();
	private String[] raw;

	public TrackerList(AntiShare plugin, String... configurationValue){
		this.raw = configurationValue;
		for(String value : raw){
			if(value.startsWith("-")){
				value = value.replace("-", "");
				try{
					if(value.equalsIgnoreCase("*")){
						for(Material material : Material.values()){
							tracked_blocks.remove(material.getId());
						}
					}else if(value.equals("none")){
						// Negated none, don't need to do anything
					}else{
						Integer block = Integer.parseInt(value);
						if(tracked_blocks.contains(block)){
							tracked_blocks.remove(block);
						}
					}
				}catch(Exception e){
					plugin.log.warning("Configuration Problem: '" + value + "' is not a number");
				}
			}else{
				try{
					if(value.equalsIgnoreCase("*")){
						for(Material material : Material.values()){
							tracked_blocks.add(material.getId());
						}
					}else if(value.equals("none")){
						// None, don't need to do anything
					}else{
						Integer block = Integer.parseInt(value);
						if(!tracked_blocks.contains(block)){
							tracked_blocks.add(block);
						}
					}
				}catch(Exception e){
					plugin.log.warning("Configuration Problem: '" + value + "' is not a number");
				}
			}
		}
	}

	public boolean track(Material block){
		return tracked_blocks.contains(block.getId());
	}
}
