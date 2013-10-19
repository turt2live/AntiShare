package com.turt2live.antishare.manager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.World;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.regions.worldsplit.WorldSplit;
import com.turt2live.antishare.regions.worldsplit.WorldSplit.Axis;
import com.turt2live.antishare.util.ASUtils;

/**
 * Represents the world split manager
 * 
 * @author turt2live
 */
public class SplitManager {

	private Map<String, WorldSplit> splits = new HashMap<String, WorldSplit>();
	private long milliseconds = 3000;

	/**
	 * Gets the world split for a world
	 * 
	 * @param world the world
	 * @return the split, if any. Will be null if not found
	 */
	public WorldSplit getSplit(World world) {
		return getSplit(world.getName());
	}

	/**
	 * Gets the world split for a world name
	 * 
	 * @param worldName the world name
	 * @return the split, if any. Will be null if not found
	 */
	public WorldSplit getSplit(String worldName) {
		return splits.get(worldName.toLowerCase());
	}

	/**
	 * Gets how often a warning should be issued, in milliseconds
	 * 
	 * @return milliseconds between warnings
	 */
	public long getWarnEvery() {
		return milliseconds;
	}

	public void load() {
		splits.clear();
		AntiShare plugin = AntiShare.p;
		EnhancedConfiguration config = new EnhancedConfiguration(new File(plugin.getDataFolder(), "worldsplit.yml"), plugin);
		config.loadDefaults(plugin.getResource("worldsplit.yml"));
		if (config.needsUpdate()) {
			config.saveDefaults();
		}
		Set<String> keys = config.getKeys(false);
		for (String key : keys) {
			if (key.equalsIgnoreCase("warn-every-in-seconds")) {
				milliseconds = config.getLong(key, 3) * 1000;
				continue;
			}
			String worldName = key;
			String axis = config.getString(key + ".axis");
			int value = config.getInt(key + ".value", 0);
			String positive = config.getString(key + ".positive");
			String negative = config.getString(key + ".negative");
			boolean warnUse = config.getBoolean(key + ".warn.use");
			int warnDistance = config.getInt(key + ".warn.distance", 5);

			if (warnUse && warnDistance <= 0) {
				plugin.getLogger().warning("Invalid warn distance for '" + key + "'. Should be >0. Using 5");
				warnDistance = 5;
			}

			if (axis == null || positive == null || negative == null) {
				plugin.getLogger().warning("Invalid world split for '" + key + "'. Please see the configuration header in worldsplits.yml");
				continue;
			}

			Axis axisE = Axis.fromString(axis);
			GameMode p = ASUtils.getGameMode(positive);
			GameMode n = ASUtils.getGameMode(negative);

			WorldSplit split = new WorldSplit(axisE, worldName, value, p, n, warnUse, warnDistance);
			if (split.isValid()) {
				this.splits.put(worldName.toLowerCase(), split);
			} else {
				plugin.getLogger().warning("Invalid world split for '" + key + "'. Please see the configuration header in worldsplits.yml");
			}
		}

		if (splits.keySet().size() > 0) {
			plugin.getLogger().info(plugin.getMessages().getMessage("splits-loaded", String.valueOf(this.splits.keySet().size())));
		}
	}

	public void reload() {
		load();
	}

}
