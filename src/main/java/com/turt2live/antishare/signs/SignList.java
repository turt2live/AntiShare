/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.signs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.metrics.TrackerList.TrackerType;

/**
 * Manages signs
 * 
 * @author turt2live
 */
public class SignList extends ArrayList<Sign> {

	private static final long serialVersionUID = -7383444270595912585L;
	private AntiShare plugin;

	/**
	 * Creates a new sign manager
	 */
	public SignList(){
		plugin = AntiShare.getInstance();

		// Prepare config
		EnhancedConfiguration signs = new EnhancedConfiguration(new File(plugin.getDataFolder(), "signs.yml"), plugin);
		signs.loadDefaults(plugin.getResource("resources/signs.yml"));
		if(!signs.fileExists() || !signs.checkDefaults()){
			signs.saveDefaults();
		}
		signs.load();

		// Load
		reload();
		if(size() > 0){
			plugin.getTrackers().getTracker(TrackerType.FEATURE_SIGNS).increment(1);
		}
	}

	/**
	 * Reloads the sign manager
	 */
	public void reload(){
		clear();
		EnhancedConfiguration signs = new EnhancedConfiguration(new File(plugin.getDataFolder(), "signs.yml"), plugin);
		signs.load();
		for(String sign : signs.getKeys(false)){
			String name = sign;
			String[] lines = new String[4];
			lines[0] = signs.getString(sign + ".line1");
			lines[1] = signs.getString(sign + ".line2");
			lines[2] = signs.getString(sign + ".line3");
			lines[3] = signs.getString(sign + ".line4");
			boolean enabled = signs.getBoolean(sign + ".enabled");
			boolean caseSensitive = signs.getBoolean(sign + ".case-sensitive");
			boolean invalid = false;
			String invalidProp = "";
			for(int i = 0; i < lines.length; i++){
				if(lines[i] == null){
					invalid = true;
					invalidProp = "line" + (i + 1);
					break;
				}
			}
			if(invalid){
				plugin.log("Invalid sign: " + name + " (property '" + invalidProp + "' is null)", Level.WARNING);
			}
			Sign assign = new Sign(name, lines, enabled, caseSensitive);
			add(assign);
		}
		if(size() > 0){
			plugin.log("Signs Loaded: " + size(), Level.INFO);
		}
	}

	/**
	 * Gets a sign by name
	 * 
	 * @param name the name
	 * @return the sign, or null if not found
	 */
	public Sign getSign(String name){
		for(int i = 0; i < size(); i++){
			if(get(i).getName().equalsIgnoreCase(name)){
				return get(i);
			}
		}
		return null;
	}

	/**
	 * Gets all the enabled signs
	 * 
	 * @return the enabled signs
	 */
	public List<Sign> getEnabledSigns(){
		List<Sign> enabled = new ArrayList<Sign>();
		for(int i = 0; i < size(); i++){
			Sign sign = get(i);
			if(sign.isEnabled()){
				enabled.add(sign);
			}
		}
		return enabled;
	}

	/**
	 * Gets all the disabled signs
	 * 
	 * @return the disabled signs
	 */
	public List<Sign> getDisabledSigns(){
		List<Sign> disabled = new ArrayList<Sign>();
		for(int i = 0; i < size(); i++){
			Sign sign = get(i);
			if(!sign.isEnabled()){
				disabled.add(sign);
			}
		}
		return disabled;
	}

	/**
	 * Gets all the signs
	 * 
	 * @return the signs
	 */
	public List<Sign> getAllSigns(){
		List<Sign> signs = new ArrayList<Sign>();
		for(int i = 0; i < size(); i++){
			signs.add(get(i));
		}
		return signs;
	}

}
