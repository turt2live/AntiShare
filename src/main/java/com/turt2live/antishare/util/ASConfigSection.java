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

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;

import com.turt2live.antishare.config.ConfigConvert;

/**
 * Customised version of a configuration section. This allows for operations in {@link ConfigConvert}
 * 
 * @author turt2live
 */
public class ASConfigSection extends MemorySection {

	public ASConfigSection(ConfigurationSection s, String p) {
		super(s, p);
	}

}
