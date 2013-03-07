/*******************************************************************************
 * Copyright (c) 2013 Travis Ralston.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.config;

import java.io.File;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.util.ASUtils;

/**
 * Represents a per-world configuration
 * 
 * @author turt2live
 */
public class PerWorldConfig extends ASConfig {

	public final String worldName;
	private static AntiShare plugin = AntiShare.p;

	/**
	 * Location where region configurations are stored
	 */
	public static final File WORLD_CONFIGURATIONS = new File(plugin.getDataFolder(), "world_configurations");

	/**
	 * Generates a per world configuration
	 * 
	 * @param world the world name
	 * @return the per world configuration
	 */
	public static PerWorldConfig getConfig(String world){
		File path = WORLD_CONFIGURATIONS;
		EnhancedConfiguration worldConfig = new EnhancedConfiguration(new File(path, ASUtils.fileSafeName(world) + ".yml"), plugin);
		worldConfig.loadDefaults(plugin.getResource("world.yml"));
		if(worldConfig.needsUpdate()){
			worldConfig.saveDefaults();
		}
		worldConfig.load();
		if(worldConfig.getBoolean("use-global")){
			worldConfig = plugin.getConfig();
		}
		return new PerWorldConfig(world, worldConfig);
	}

	PerWorldConfig(String world, EnhancedConfiguration config){
		super(config);
		worldName = world;
	}

}
