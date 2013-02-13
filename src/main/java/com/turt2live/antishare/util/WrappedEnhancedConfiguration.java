package com.turt2live.antishare.util;

import java.io.File;

import org.bukkit.plugin.Plugin;

import com.feildmaster.lib.configuration.EnhancedConfiguration;

/**
 * Temporary fix for missing methods
 */
public class WrappedEnhancedConfiguration extends EnhancedConfiguration {

	public WrappedEnhancedConfiguration(File file, Plugin plugin){
		super(file, plugin);
	}

	/**
	 * Clears the configuration. This does not save the file.
	 */
	public void clearFile(){
		clearCache();
		map.clear();
	}

}
