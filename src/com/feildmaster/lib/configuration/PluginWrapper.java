package com.feildmaster.lib.configuration;

public class PluginWrapper extends org.bukkit.plugin.java.JavaPlugin {
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
