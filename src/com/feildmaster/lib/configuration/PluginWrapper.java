/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.feildmaster.lib.configuration;

public class PluginWrapper extends org.bukkit.plugin.java.JavaPlugin {
    private EnhancedConfiguration config;

    public EnhancedConfiguration getConfig() {
        if(config == null) {
            config = new EnhancedConfiguration(this);
        }
        return config;
    }

    public void reloadConfig() {
        getConfig().load();
    }

    public void saveConfig() {
        getConfig().save();
    }

    public void saveDefaultConfig() {
        getConfig().saveDefaults();
    }
}
