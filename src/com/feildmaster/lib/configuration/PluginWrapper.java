package com.feildmaster.lib.configuration;

public abstract class PluginWrapper extends org.bukkit.plugin.java.JavaPlugin {
	private EnhancedConfiguration config;

	// This is to reorder Enable to be on top
	@Override
	public abstract void onEnable();

	@Override
	public abstract void onDisable();

	@Override
	public EnhancedConfiguration getConfig(){
		if(config == null){
			reloadConfig();
		}
		return config;
	}

	@Override
	public void reloadConfig(){
		if(config == null){
			config = new EnhancedConfiguration(this);
		}
		config.load();
	}

	@Override
	public void saveConfig(){
		config.save();
	}

	@Override
	public void saveDefaultConfig(){
		config.saveDefaults();
	}
}
