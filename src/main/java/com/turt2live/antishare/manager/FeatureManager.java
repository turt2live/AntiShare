package com.turt2live.antishare.manager;

import java.io.File;

import com.feildmaster.lib.configuration.EnhancedConfiguration;

public class FeatureManager extends AntiShareManager {

	/**
	 * Feature type
	 */
	public static enum Feature{
		INVENTORIES, REGIONS, BLOCKS, MONEY, SELF, ALWAYS_ON;
	}

	private EnhancedConfiguration yamlFile;

	@Override
	public boolean load(){
		yamlFile = new EnhancedConfiguration(new File(plugin.getDataFolder(), "features.yml"), plugin);
		yamlFile.loadDefaults(plugin.getResource("resources/features.yml"));
		if(!yamlFile.fileExists() || !yamlFile.checkDefaults()){
			yamlFile.saveDefaults();
		}
		yamlFile.load();
		return true;
	}

	@Override
	public boolean save(){
		return true;
	}

	/**
	 * Determines if a feature is enabled
	 * 
	 * @param feature the feature to test
	 * @return true if the feature is enabled, false otherwise
	 */
	public boolean isEnabled(Feature feature){
		if(feature == Feature.SELF || feature == Feature.ALWAYS_ON){
			return true;
		}
		return yamlFile.getBoolean(feature.name().toLowerCase().trim());
	}

}
