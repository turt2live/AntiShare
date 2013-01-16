package com.turt2live.antishare.manager;

import java.io.File;

import com.turt2live.antishare.feildmaster.lib.configuration.EnhancedConfiguration;

public class FeatureManager extends ConfigBackedManager {

	public static enum Feature{
		INVENTORIES, REGIONS, BLOCKS, SELF;
	}

	private EnhancedConfiguration config;

	@Override
	public boolean loadManager(){
		return config != null;
	}

	@Override
	public void loadConfiguration(){
		config = new EnhancedConfiguration(new File(plugin.getDataFolder(), "features.yml"), plugin);
		config.load();
		config.loadDefaults(plugin.getResource("resources/features.yml"));
	}

	@Override
	public boolean save(){
		return true;
	}

	public boolean isEnabled(Feature feature){
		if(feature == Feature.SELF){
			return true;
		}
		return config.getBoolean(feature.name().toLowerCase().trim());
	}

}
