package com.turt2live.antishare;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.Bukkit;

public class UsageStatistics {

	public static void send(AntiShare plugin){
		if(!plugin.getConfig().getBoolean("settings.send-use-info")){
			return;
		}
		String pluginVersion = plugin.getDescription().getVersion();
		String bukkitVersion = Bukkit.getBukkitVersion();
		try{
			URL statsURL = new URL("http://mc.turt2live.com/plugins/plugin_stats.php?plugin=AntiShare&version=" + pluginVersion + "&cbVersion=" + bukkitVersion);
			BufferedReader in = new BufferedReader(new InputStreamReader(statsURL.openConnection().getInputStream()));
			String line = in.readLine();
			if(!line.equalsIgnoreCase("sent")){
				AntiShare.log.warning("[AntiShare] Could not send usage statistics.");
			}
			in.close();
		}catch(Exception e){
			AntiShare.log.warning("[AntiShare] Could not send usage statistics.");
		}
	}

}
