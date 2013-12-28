/*******************************************************************************
 * Copyright (c) 2013 Travis Ralston.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.config;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.regions.Region;
import com.turt2live.antishare.util.ASUtils;

import java.io.File;

/**
 * Region configuration
 *
 * @author turt2live
 */
public class RegionConfiguration extends ASConfig {

    private static AntiShare plugin = AntiShare.p;
    private Region region;

    /**
     * Generates a region configuration
     *
     * @param region the region
     * @return the region configuration
     */
    public static RegionConfiguration getConfig(Region region) {
        File path = Region.REGION_CONFIGURATIONS;
        EnhancedConfiguration regionConfig = new EnhancedConfiguration(new File(path, ASUtils.fileSafeName(region.getName()) + ".yml"), plugin);
        regionConfig.loadDefaults(plugin.getResource("world.yml"));
        if (regionConfig.needsUpdate()) {
            regionConfig.saveDefaults();
        }
        EnhancedConfiguration worldConfig = plugin.getWorldConfigs().getConfig(region.getWorldName()).rawConfiguration;
        regionConfig.load();
        if (regionConfig.getBoolean("use-global")) {
            regionConfig = plugin.getConfig();
        } else if (regionConfig.getBoolean("use-world")) {
            regionConfig = worldConfig;
        }
        return new RegionConfiguration(region, regionConfig, worldConfig);
    }

    RegionConfiguration(Region region, EnhancedConfiguration config, EnhancedConfiguration world) {
        super(config, world);
        this.region = region;
    }

    /**
     * Gets the region associated with this configuration
     *
     * @return the region for this configuration
     */
    public Region getRegion() {
        return region;
    }

}
