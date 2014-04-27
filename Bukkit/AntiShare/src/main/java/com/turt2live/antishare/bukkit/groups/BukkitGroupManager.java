package com.turt2live.antishare.bukkit.groups;

import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.configuration.YamlConfiguration;
import com.turt2live.antishare.configuration.Configuration;
import com.turt2live.antishare.configuration.groups.Group;
import com.turt2live.antishare.configuration.groups.MainGroup;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;

/**
 * Bukkit group manager
 */
public class BukkitGroupManager extends com.turt2live.antishare.configuration.groups.GroupManager {

    private AntiShare plugin = AntiShare.getInstance();

    @Override
    public void loadAll() {
        super.groups.clear();
        super.mainGroup = null;
        File[] files = plugin.getDataFolder().listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().startsWith("group_") && file.getName().toLowerCase().endsWith(".yml")) {
                    Configuration groupConfig = new YamlConfiguration(file);
                    if (file.getName().equalsIgnoreCase("group_main.yml")) {
                        MainGroup main = new BukkitMainGroup(groupConfig);
                        super.mainGroup = main;
                    } else {
                        Group group = new BukkitGroup(groupConfig);
                        super.groups.put(group.getName(), group);
                    }
                }
            }
        }
        if (super.mainGroup == null) {
            File file = new File(plugin.getDataFolder(), "group_main.yml");
            org.bukkit.configuration.file.YamlConfiguration yaml = new org.bukkit.configuration.file.YamlConfiguration();
            try {
                yaml.load(plugin.getResource("group.yml"));
                yaml.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
            Configuration groupConfig = new YamlConfiguration(file);
            MainGroup main = new BukkitMainGroup(groupConfig);
            super.mainGroup = main;
        }
    }
}
