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
package com.turt2live.antishare.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.entity.Entity;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.api.ASEntity;

/**
 * Entity List
 * 
 * @author turt2live
 */
public class EntityList {

	private boolean whitelist = false;
	private List<String> blocked = new ArrayList<String>();

	/**
	 * Creates a new Event List
	 * 
	 * @param file the configuration file name
	 * @param node the configuration node
	 * @param configurationValue the values
	 */
	public EntityList(String file, String node, String... configurationValue){
		// Setup
		AntiShare plugin = AntiShare.getInstance();

		// Sanity
		if(configurationValue.length <= 0){
			return;
		}

		// Loop
		for(int index = 0; index < configurationValue.length; index++){
			String blocked = configurationValue[index].trim();

			// Sanity
			if(blocked.length() <= 0){
				continue;
			}

			// Check negation
			boolean negate = false;
			if(blocked.startsWith("-")){
				negate = true;
				blocked = blocked.replaceFirst("-", "");
			}

			// Check for "all"/"none"
			if(blocked.equalsIgnoreCase("*") || blocked.equalsIgnoreCase("all")){
				// Add entities
				for(ASEntity e : ASUtils.allEntities()){
					if(!negate){
						this.blocked.add(e.getProperName());
					}else{
						this.blocked.remove(e.getProperName());
					}
				}
				continue;
			}else if(blocked.equalsIgnoreCase("none")){
				this.blocked.clear();
				continue; // For sanity sake
			}

			// Whitelist?
			if(blocked.equalsIgnoreCase("whitelist") && index == 0){
				whitelist = true;
				if(negate){
					whitelist = false;
				}
				continue;
			}

			// Add entity to list
			try{
				if(ASUtils.getEntityName(blocked) == null){
					throw new Exception("");
				}
				if(!negate){
					this.blocked.add(ASUtils.getEntityName(blocked));
				}else{
					this.blocked.remove(ASUtils.getEntityName(blocked));
				}
			}catch(Exception e){
				plugin.log("Configuration Problem: '" + (negate ? "-" : "") + blocked + "' is not valid! (See '" + node + "' in your " + file + ")", Level.WARNING);
			}
		}
	}

	/**
	 * Checks if a block is blocked
	 * 
	 * @param entity the block
	 * @return true if blocked
	 */
	public boolean isBlocked(Entity entity){
		if(whitelist){
			boolean contained = !blocked.contains(ASUtils.getEntityName(entity));
			return contained;
		}
		boolean contained = blocked.contains(ASUtils.getEntityName(entity));
		return contained;
	}

}
