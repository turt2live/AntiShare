package com.turt2live.antishare.lang;

/**
 * Supported Locales
 */
public enum Locale{

	/**
	 * ENGLISH
	 */
	EN_US("locale_en_US.yml");

	private String fileName;

	private Locale(String fileName){
		this.fileName = fileName;
	}

	/**
	 * Gets the locale's file name
	 * 
	 * @return the file name
	 */
	public String getFileName(){
		return fileName;
	}

}
