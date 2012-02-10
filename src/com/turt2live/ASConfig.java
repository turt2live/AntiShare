package com.turt2live;

public class ASConfig {

	private AntiShare plugin;

	public ASConfig(AntiShare plugin){
		this.plugin = plugin;
	}

	public void create(){
		plugin.getConfig().loadDefaults(plugin.getResource("resources/config.yml"));
		if(!plugin.getConfig().fileExists() || !plugin.getConfig().checkDefaults()){
			plugin.getConfig().saveDefaults();
		}
		load();
	}

	public Object get(String path){
		return plugin.getConfig().get(path);
	}

	public boolean getBoolean(String path){
		return plugin.getConfig().getBoolean(path);
	}

	public String getString(String path){
		return plugin.getConfig().getString(path);
	}

	public void load(){
		plugin.reloadConfig();
	}

	public void reload(){
		load();
	}

	public void save(){
		plugin.saveConfig();
	}

	public void set(String path, Object value){
		plugin.getConfig().set(path, value);
	}
}
