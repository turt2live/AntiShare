package me.turt2live;

import java.io.File;

public class ASConfig {

	private AntiShare plugin;

	public ASConfig(AntiShare plugin){
		this.plugin = plugin;
	}

	public void create(){
		File d = plugin.getDataFolder();
		d.mkdirs();
		if(!plugin.getConfig().fileExists() || !plugin.getConfig().checkDefaults())
			plugin.saveDefaultConfig();
		load();
	}

	public void save(){
		plugin.saveConfig();
	}

	public void set(String path, Object value){
		plugin.getConfig().set(path, value);
	}

	public Object get(String path){
		return plugin.getConfig().get(path);
	}

	public void reload(){
		load();
	}

	public void load(){
		plugin.reloadConfig();
	}
}
