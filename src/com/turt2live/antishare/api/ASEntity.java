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
