package com.turt2live.antishare.storage;

import java.util.Vector;

import org.bukkit.Material;

import com.turt2live.antishare.AntiShare;

public class EventList {

	private boolean whitelist = false;
	private boolean useString = false;
	private Vector<Integer> blocked = new Vector<Integer>();
	private Vector<String> blocked_strings = new Vector<String>();
	private String[] raw;

	public EventList(AntiShare plugin, String... configurationValue){
		this.raw = configurationValue;
		boolean skip = false;
		if(raw.length == 1){
			if(raw[0].equalsIgnoreCase("*")){
				for(Material m : Material.values()){
					blocked.add(m.getId());
				}
				skip = true;
			}else if(raw[0].equalsIgnoreCase("none")){
				skip = true;
			}
		}
		if(!skip){
			int index = 0;
			for(String blocked : raw){
				blocked = blocked.trim();
				if(blocked.equalsIgnoreCase("whitelist") && index == 0){
					whitelist = true;
					index++;
					continue;
				}
				try{
					this.blocked.add(Integer.valueOf(blocked));
				}catch(Exception e){
					plugin.log.warning("Configuration Problem: '" + blocked + "' is not a number");
				}
				index++;
			}
		}
	}

	public EventList(boolean stringsOnly, AntiShare plugin, String... configurationValue){
		this.raw = configurationValue;
		int index = 0;
		for(String blocked : raw){
			blocked = blocked.trim();
			if(blocked.equalsIgnoreCase("whitelist") && index == 0){
				whitelist = true;
				index++;
				continue;
			}
			if(!blocked.startsWith("/")){
				blocked = blocked + "/";
			}
			this.blocked_strings.add(blocked);
			index++;
		}
	}

	public boolean isBlocked(Material item){
		if(whitelist){
			return !blocked.contains(item.getId());
		}
		return blocked.contains(item.getId());
	}

	public boolean isBlocked(String command){
		if(!useString){
			return false;
		}
		if(!command.startsWith("/")){
			command = "/" + command;
		}
		if(whitelist){
			return !blocked_strings.contains(command);
		}
		return blocked_strings.contains(command);
	}

	public String[] getRaw(){
		return raw;
	}

	public boolean isWhitelist(){
		return whitelist;
	}
}
