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
package com.turt2live.antishare.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.PermissionNodes;
import com.turt2live.materials.MaterialAPI;
import com.turt2live.simplenotice.SNAPI;

/**
 * Messages class
 * 
 * @author turt2live
 */
public class Messages {

	private class AlertDetails {
		public long admin_last_sent = 0L;
		public long player_last_sent = 0L;
		public long console_last_sent = 0L;
	}

	private EnhancedConfiguration yaml;
	private AntiShare plugin = AntiShare.p;
	private Map<String, AlertDetails> alerts = new HashMap<String, AlertDetails>();
	private final int messageDelay = 1000; // Milliseconds
	private SNAPI sn = new SNAPI(plugin);

	public Messages() {
		yaml = new EnhancedConfiguration(new File(plugin.getDataFolder(), "locale.yml"), plugin);
		yaml.loadDefaults(plugin.getResource("locale.yml"));
		if (yaml.needsUpdate()) {
			yaml.saveDefaults();
		}
		yaml.load();
	}

	public void magicValue() {
		if (plugin.settings().ignoreMagicValues) {
			return;
		}

		String mapKey = "antishare.magic.value";
		// Check last alert, if any
		AlertDetails details = alerts.get(mapKey);
		if (details == null) {
			details = new AlertDetails();
			details.admin_last_sent = System.currentTimeMillis();
			details.player_last_sent = System.currentTimeMillis();
			details.console_last_sent = System.currentTimeMillis();
		} else {
			long now = System.currentTimeMillis();
			if ((now - details.console_last_sent) < messageDelay) {
				return;
			}
		}

		// Send the message
		plugin.getLogger().warning("Magic values (Item IDs, etc) may be removed in a later version!");
		plugin.getLogger().warning("Please update your config to avoid using the item IDs!");

		// Update last sent
		alerts.put(mapKey, details);
	}

	/**
	 * Gets a message
	 * 
	 * @param path the path
	 * @param arguments arguments to insert
	 * @return the message, or null if not found
	 */
	public String getMessage(String path, String... arguments) {
		String message = yaml.getString(path, "no message");
		int expectedArguments = 0;
		int counter = 0;
		boolean moreArguments = true;
		while(moreArguments) {
			if (!message.contains("{" + counter + "}")) {
				moreArguments = false;
			} else {
				expectedArguments++;
			}
			counter++;
		}
		if ((expectedArguments > 0 && arguments == null) || (arguments != null && expectedArguments > arguments.length)) {
			Exception exception = new IllegalArgumentException("Too few arguments for " + path + " (Expected " + expectedArguments + " got " + (arguments == null ? "null" : arguments.length) + ")");
			exception.printStackTrace();
		} else if (arguments != null && (arguments.length > expectedArguments)) {
			Exception exception = new IllegalArgumentException("Too many arguments for " + path + " (Expected " + expectedArguments + " got " + (arguments == null ? "null" : arguments.length) + ")");
			exception.printStackTrace();
		}
		if (arguments != null) {
			for(int i = 0; i < arguments.length; i++) {
				String replaceString = "{" + i + "}";
				if (arguments[i] == null) {
					arguments[i] = "null";
				}
				message = message.replace(replaceString, arguments[i]);
			}
		}
		for(ChatColor color : ChatColor.values()) {
			message = message.replace("{" + color.name() + "}", color + "");
		}
		return message;
	}

	/**
	 * Reloads the messages
	 */
	public void reload() {
		yaml.load();
	}

	/**
	 * Sends a notification of an action
	 * 
	 * @param player the player executing the action
	 * @param action the action
	 * @param illegal is it an illegal action
	 * @param material the material involved (Eg: thrown)
	 * @param extraVariables any extra variables (Eg: GameMode)
	 */
	public void notifyParties(Player player, Action action, boolean illegal, Material material, String... extraVariables) {
		notifyParties(player, action, illegal, MaterialAPI.capitalize(material.name()), extraVariables);
	}

	/**
	 * Sends a notification of an action
	 * 
	 * @param player the player executing the action
	 * @param action the action
	 * @param illegal is it an illegal action
	 * @param entity the Entity involved (Eg: attacked)
	 * @param extraVariables any extra variables (Eg: GameMode)
	 */
	@SuppressWarnings ("deprecation")
	// TODO: Magic value
	public void notifyParties(Player player, Action action, boolean illegal, EntityType entity, String... extraVariables) {
		notifyParties(player, action, illegal, MaterialAPI.capitalize(entity.getName()), extraVariables);
	}

	/**
	 * Sends a notification of an action
	 * 
	 * @param player the player executing the action
	 * @param action the action
	 * @param illegal is it an illegal action
	 * @param string the applicable string (Eg: Material, Entity)
	 * @param extraVariables any extra variables (Eg: GameMode)
	 */
	public void notifyParties(Player player, Action action, boolean illegal, String string, String... extraVariables) {
		if (player == null || action == null || string == null) {
			throw new IllegalArgumentException("Null arguments are not allowed");
		}

		boolean ignoredAction = action == Action.ENTER_REGION || action == Action.EXIT_REGION
				|| action == Action.GAMEMODE_CHANGE || action == Action.WORLD_CHANGE;

		// Metrics
		if (!ignoredAction) {
			if (illegal) {
				AntiShare.ILLEGAL_ACTIONS.increment(action);
			} else {
				AntiShare.LEGAL_ACTIONS.increment(action);
			}
		}

		// We don't care
		if (!illegal) {
			return;
		}

		if (extraVariables == null) {
			extraVariables = new String[0];
		}
		String[] playerStrings = new String[1 + extraVariables.length];
		String[] notifyStrings = new String[2 + extraVariables.length];
		playerStrings[0] = string;
		notifyStrings[0] = player.getName();
		notifyStrings[1] = string;
		for(int i = 0; i < extraVariables.length; i++) {
			playerStrings[i + 1] = extraVariables[i];
			notifyStrings[i + 2] = extraVariables[i];
		}

		// Do fine/reward
		plugin.getMoneyManager().fire(action, illegal, player);

		// Prepare logic and messages
		String playerMessage = ignoredAction ? "no message" : getMessage("player-messages." + action.name(), playerStrings);
		String adminMessage = getMessage("notify-messages." + action.name(), notifyStrings);
		boolean toPlayer = !player.hasPermission(PermissionNodes.SILENT_NOTIFICATIONS);
		boolean toAdmin = plugin.settings().notificationSettings.admins && plugin.settings().notificationSettings.enabled;
		boolean toConsole = plugin.settings().notificationSettings.console && plugin.settings().notificationSettings.enabled;
		String mapKey = action.name() + adminMessage + playerMessage + player.hashCode();

		// Check last alert, if any
		AlertDetails details = alerts.get(mapKey);
		if (details == null) {
			details = new AlertDetails();
			details.admin_last_sent = System.currentTimeMillis();
			details.player_last_sent = System.currentTimeMillis();
			details.console_last_sent = System.currentTimeMillis();
		} else {
			long now = System.currentTimeMillis();
			if ((now - details.admin_last_sent) < messageDelay) {
				toAdmin = false;
			}
			if ((now - details.player_last_sent) < messageDelay) {
				toPlayer = false;
			}
			if ((now - details.console_last_sent) < messageDelay) {
				toConsole = false;
			}
		}

		// Alert
		if (toPlayer) {
			sendTo(player, playerMessage, true);
			details.player_last_sent = System.currentTimeMillis();
		}
		if (toAdmin) {
			details.admin_last_sent = System.currentTimeMillis();
			for(Player potentialAdmin : plugin.getServer().getOnlinePlayers()) {
				if (potentialAdmin.hasPermission(PermissionNodes.GET_NOTIFICATIONS)) {
					sendTo(potentialAdmin, adminMessage, true);
				}
			}
		}
		if (toConsole) {
			details.console_last_sent = System.currentTimeMillis();
			sendTo(Bukkit.getConsoleSender(), adminMessage, false);
		}

		// Update last sent
		alerts.put(mapKey, details);
	}

	/**
	 * Sends a message to a target. A prefix is automatically supplied
	 * 
	 * @param target the target
	 * @param message the message ("no message" to not send). This should already be converted for ChatColors
	 * @param useSimpleNotice true to use SimpleNotice. Toggles are handled internally
	 */
	public void sendTo(CommandSender target, String message, boolean useSimpleNotice) {
		if (target == null || message == null) {
			throw new IllegalArgumentException("Cannot use null arguments");
		}
		if (ChatColor.stripColor(message).trim().equalsIgnoreCase("no message")) {
			return;
		}
		message = message.replace("EntityHorse", "Horse"); // Cause Bukkit is weird
		String prefix = plugin.getPrefix() == null || plugin.getPrefix().equalsIgnoreCase("no message") ? "[AntiShare]" : plugin.getPrefix();
		message = ChatColor.GRAY + prefix + " " + ChatColor.RESET + message;
		/* SimpleNotice support provided by feildmaster.
		 * Support adapted by krinsdeath and further
		 * modified by turt2live for AntiShare.
		 */
		if (target instanceof Player) {
			Player player = (Player) target;
			boolean sentBySN = false;
			if (useSimpleNotice && plugin.isSimpleNoticeEnabled(player.getName())) {
				sentBySN = sn.send(player, message);
			}
			if (!sentBySN) {
				player.sendMessage(message);
			}
		} else {
			target.sendMessage(message);
		}
	}

}
