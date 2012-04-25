package com.turt2live.antishare;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import com.turt2live.antishare.AntiShare.LogType;

/**
 * Usage Statistics
 * 
 * @author turt2live
 */
public class UsageStatistics {

	/**
	 * Sends usage statistics
	 */
	public static void send(){
		AntiShare plugin = AntiShare.instance;
		if(!plugin.getConfig().getBoolean("settings.send-usage-statistics")){
			return;
		}
		String pluginVersion = "UNKNOWN";
		String bukkitVersion = "UNKNOWN";
		try{
			pluginVersion = URLEncoder.encode(plugin.getDescription().getVersion(), "UTF-8");
			bukkitVersion = URLEncoder.encode(Bukkit.getVersion(), "UTF-8");
		}catch(UnsupportedEncodingException e1){
			e1.printStackTrace();
		}
		try{
			URL statsURL = new URL("http://mc.turt2live.com/plugins/plugin_stats.php?plugin=AntiShare&version=" + pluginVersion + "&cbVersion=" + bukkitVersion);
			BufferedReader in = new BufferedReader(new InputStreamReader(statsURL.openConnection().getInputStream()));
			String line;
			boolean sent = false;
			while ((line = in.readLine()) != null){
				if(line.equalsIgnoreCase("sent")){
					sent = true;
					break;
				}
			}
			if(!sent){
				plugin.getMessenger().log("Could not send usage statistics.", Level.WARNING, LogType.INFO);
			}
			in.close();
		}catch(Exception e){
			plugin.getMessenger().log("Could not send usage statistics.", Level.WARNING, LogType.INFO);
		}
	}

}
