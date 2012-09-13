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
package com.turt2live.antishare.api;

public class Variable {

	public final String key;
	public final String value;

	/**
	 * Creates a variable
	 * 
	 * @param key the key
	 * @param value the value
	 */
	public Variable(String key, String value){
		this.key = key;
		this.value = value;
	}

}
