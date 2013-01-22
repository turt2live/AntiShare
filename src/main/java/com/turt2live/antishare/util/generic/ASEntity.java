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
package com.turt2live.antishare.util.generic;

/**
 * AntiShare Entity - Used for determining "proper" names of entities
 * 
 * @author turt2live
 */
public class ASEntity {

	private String givenName;
	private String properName;

	/**
	 * Creates a new entity name
	 * @param given given name
	 * @param proper proper name
	 */
	public ASEntity(String given, String proper){
		this.givenName = given;
		this.properName = proper;
	}

	/**
	 * Gets the proper name for this entity
	 * @return the proper name
	 */
	public String getProperName(){
		return properName;
	}

	/**
	 * Gets the given name for this entity
	 * @return the given name
	 */
	public String getGivenName(){
		return givenName;
	}

}
