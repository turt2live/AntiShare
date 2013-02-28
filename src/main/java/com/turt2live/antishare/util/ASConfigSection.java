package com.turt2live.antishare.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;

import com.turt2live.antishare.config.ConfigConvert;

/**
 * Customised version of a configuration section. This allows for operations in {@link ConfigConvert}
 * 
 * @author turt2live
 */
public class ASConfigSection extends MemorySection {

	public ASConfigSection(ConfigurationSection s, String p){
		super(s, p);
	}

}
