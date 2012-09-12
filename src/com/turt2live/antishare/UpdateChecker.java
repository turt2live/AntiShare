/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare;

import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Update Checker
 * 
 * @author turt2live
 */
public class UpdateChecker {

	/*
	 * Class function replicated from Vault (thanks Sleaker!)
	 * 
	 * URL: https://github.com/MilkBowl/Vault/blob/master/src/net/milkbowl/vault/Vault.java#L118
	 */

	/**
	 * Creates and starts the update checker
	 */
	public static void start(){
		final AntiShare plugin = AntiShare.getInstance();
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			@Override
			public void run(){
				try{
					if(isOutdated()){
						String newVersion = getNewVersionString();
						String currentVersion = getVersion();
						plugin.getLogger().warning("AntiShare " + newVersion + " is out! You are running AntiShare " + currentVersion);
						plugin.getLogger().warning("Update AntiShare at: http://dev.bukkit.org/server-mods/antishare");
					}
				}catch(Exception e){} // Don't handle
			}
		}, 0, 36000); // 30 minutes
	}

	/**
	 * Determines if AntiShare is outdated
	 * 
	 * @return true if outdated
	 */
	public static boolean isOutdated(){
		double current = Double.valueOf(getVersion().split("-")[0].replaceFirst("\\.", ""));
		;
		double release = Double.valueOf(getNewVersion());
		return release > current;
	}

	/**
	 * Gets the active version of AntiShare
	 * 
	 * @return the active version of AntiShare
	 */
	public static String getVersion(){
		return AntiShare.getInstance().getDescription().getVersion();
	}

	/**
	 * Gets the public release version of AntiShare
	 * 
	 * @return the public release version of AntiShare
	 */
	public static String getNewVersion(){
		return getNewVersionString().replaceFirst("\\.", "");
	}

	/**
	 * Gets the public release version of AntiShare
	 * 
	 * @return the public release version or AntiShare
	 */
	// Borrowed from Vault, thanks Sleaker!
	// https://github.com/MilkBowl/Vault/blob/master/src/net/milkbowl/vault/Vault.java#L520
	public static String getNewVersionString(){
		String pluginUrlString = "http://dev.bukkit.org/server-mods/antishare/files.rss";
		try{
			URL url = new URL(pluginUrlString);
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openConnection().getInputStream());
			doc.getDocumentElement().normalize();
			NodeList nodes = doc.getElementsByTagName("item");
			Node firstNode = nodes.item(0);
			if(firstNode.getNodeType() == 1){
				Element firstElement = (Element) firstNode;
				NodeList firstElementTagName = firstElement.getElementsByTagName("title");
				Element firstNameElement = (Element) firstElementTagName.item(0);
				NodeList firstNodes = firstNameElement.getChildNodes();
				return firstNodes.item(0).getNodeValue().replace("v", "").trim();
			}
		}catch(Exception localException){} // Do not handle
		return getVersion();
	}
}
