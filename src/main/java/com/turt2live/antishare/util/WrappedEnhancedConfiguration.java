package com.turt2live.antishare.util;

import java.io.File;

import org.bukkit.plugin.Plugin;

import com.feildmaster.lib.configuration.EnhancedConfiguration;

public class WrappedEnhancedConfiguration extends EnhancedConfiguration {

	public WrappedEnhancedConfiguration(File file, Plugin plugin){
		super(file, plugin);
	}

	public void clearFile(){
		clearCache();
		this.map.clear();
	}

}
