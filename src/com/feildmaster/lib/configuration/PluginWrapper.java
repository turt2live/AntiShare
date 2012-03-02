package com.feildmaster.lib.configuration;

import org.bukkit.plugin.java.JavaPlugin;

public class PluginWrapper extends JavaPlugin {
	private EnhancedConfiguration config;

	@Override
	public EnhancedConfiguration getConfig(){
		if(config == null){
			config = new EnhancedConfiguration(this);
		}
		return config;
	}

	@Override
	public void reloadConfig(){
		getConfig().load();
	}

	@Override
	public void saveConfig(){
		getConfig().save();
	}

	@Override
	public void saveDefaultConfig(){
		getConfig().saveDefaults();
	}
}
