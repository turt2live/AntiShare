package com.turt2live.antishare.io;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;

import java.io.File;
import java.io.IOException;

abstract class GenericDataFile {

    protected static EnhancedConfiguration getFile(String name) {
        AntiShare plugin = AntiShare.p;
        File file = new File(plugin.getDataFolder(), "data" + File.separator + name + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        EnhancedConfiguration yamlFile = new EnhancedConfiguration(file, plugin);
        yamlFile.load();
        return yamlFile;
    }

}
