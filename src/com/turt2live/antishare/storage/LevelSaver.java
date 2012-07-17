package com.turt2live.antishare.storage;

import java.io.File;
import java.io.IOException;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;

public class LevelSaver {

	public static class Level {

		public int level;
		public float percent;

		public Level(int level, float percent){
			this.level = level;
			this.percent = percent;
		}

		public void setTo(Player player){
			player.setLevel(level);
			player.setExp(percent);
		}

	}

	private static EnhancedConfiguration getFile(){
		AntiShare plugin = AntiShare.getInstance();
		File file = new File(plugin.getDataFolder(), "data" + File.separator + "levels.yml");
		if(!file.exists()){
			try{
				file.createNewFile();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		EnhancedConfiguration efile = new EnhancedConfiguration(file, plugin);
		efile.load();
		return efile;
	}

	public static Level getLevel(String player, GameMode gamemode){
		EnhancedConfiguration file = getFile();
		float percent = (float) file.getDouble(player + "." + gamemode.name() + ".percent", 0f);
		int level = file.getInt(player + "." + gamemode.name() + ".level", 0);
		return new Level(level, percent);
	}

	public static void saveLevel(String player, GameMode gamemode, Level level){
		EnhancedConfiguration file = getFile();
		file.set(player + "." + gamemode.name() + ".level", level.level);
		file.set(player + "." + gamemode.name() + ".percent", level.percent);
		file.save();
	}

}
