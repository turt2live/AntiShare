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
package com.turt2live.antishare.convert;

import com.turt2live.antishare.AntiShare;

/**
 * For internal conversion of 3.2.0b formats
 * 
 * @author turt2live
 */
public class Convert320bInternal {

	/**
	 * An enum to represent what conversion type we are doing
	 * 
	 * @author turt2live
	 */
	public static enum ConvertType{
		SQL,
		YAML,
		INVALID;

		/**
		 * Gets the type of conversion based on input
		 * 
		 * @param input the input
		 * @return the type, or INVALID if invalid
		 */
		public static ConvertType getType(String input){
			input = input.trim();

			// Check SQL
			if(input.equalsIgnoreCase("db") || input.equalsIgnoreCase("database") || input.equalsIgnoreCase("sql")){
				return ConvertType.SQL;
			}

			// Check file
			if(input.equalsIgnoreCase("file") || input.equalsIgnoreCase("flatfile") || input.equalsIgnoreCase("yaml")){
				return ConvertType.YAML;
			}

			// Default
			return ConvertType.INVALID;
		}
	}

	/**
	 * Converts the formats
	 * 
	 * @param from the "from" format
	 * @param to the "to" format
	 * @return true if everything went well
	 */
	public static boolean convert(ConvertType from, ConvertType to){
		AntiShare plugin = AntiShare.getInstance();

		// Sanity
		if((to == ConvertType.INVALID || from == ConvertType.INVALID) || (to == from)){
			return false;
		}

		// Induces a save
		plugin.reload();

		return true;
	}
}
