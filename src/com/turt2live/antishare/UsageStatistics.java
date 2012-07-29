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
		AntiShare plugin = AntiShare.getInstance();
		if(!plugin.getConfig().getBoolean("settings.send-usage-statistics")){
			return;
		}

		// mc.turt2live.com/plugins
		String pluginVersion = "UNKNOWN";
		String bukkitVersion = "UNKNOWN";
		try{
			pluginVersion = URLEncoder.encode(plugin.getDescription().getVersion(), "UTF-8");
			bukkitVersion = URLEncoder.encode(Bukkit.getVersion(), "UTF-8");
		}catch(UnsupportedEncodingException e1){
			AntiShare.getInstance().getMessenger().log("AntiShare encountered and error. Please report this to turt2live.", Level.SEVERE, LogType.ERROR);
			AntiShare.getInstance().getMessenger().log("Please see " + ErrorLog.print(e1) + " for the full error.", Level.SEVERE, LogType.ERROR);
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

		// mcstats.org
		plugin.getTrackers().addTo(plugin.getMetrics());
		plugin.getMetrics().start(); // Handles it's own opt-out
	}

}
