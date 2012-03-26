package com.turt2live.antishare.api;

import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.entity.Player;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.turt2live.antishare.Notification;
import com.turt2live.antishare.enums.NotificationType;

public class UtilsAPI extends APIBase {

	/**
	 * Sends a notification through the AntiShare notification system
	 * 
	 * @param type the notification type
	 * @param player the player who is involved
	 * @param variable the variable. This is the red or green portion at the end of the notification
	 */
	public void sendNotification(NotificationType type, Player player, String variable){
		Notification.sendNotification(type, player, variable);
	}

	/**
	 * Determines if this version of AntiShare is outdated
	 * 
	 * @return true if outdated
	 */
	public boolean isOutdated(){
		double publicRelease = getNewVersionAsNumber();
		return publicRelease > getVersionAsNumber();
	}

	/**
	 * Gets the version of the latest release of AntiShare
	 * 
	 * @return the latest release version
	 */
	// Borrowed from Vault, thanks Sleaker!
	// https://github.com/MilkBowl/Vault/blob/master/src/net/milkbowl/vault/Vault.java#L520
	public String getNewVersion(){
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
				return firstNodes.item(0).getNodeValue().replace("v", "").replaceFirst("\\.", "").trim();
			}
		}catch(Exception localException){} // Do not handle
		return getVersion();
	}

	/**
	 * Gets the version of the latest release of AntiShare in the form of a double
	 * 
	 * @return the latest release version as a double
	 */
	public double getNewVersionAsNumber(){
		return Double.valueOf(getNewVersion());
	}

}
