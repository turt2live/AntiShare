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
package com.turt2live.antishare.api;

/**
 * AntiShare Entity - Used for determining "proper" names of entities
 * 
 * @author turt2live
 */
public class ASEntity {

	private String givenName;
	private String properName;

	public ASEntity(String given, String proper){
		this.givenName = given;
		this.properName = proper;
	}

	public String getProperName(){
		return properName;
	}

	public String getGivenName(){
		return givenName;
	}

}
