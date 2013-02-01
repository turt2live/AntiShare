package com.turt2live.antishare.lang;

public enum Locale{

	EN_US("locale_en_US.yml");

	private String file;

	private Locale(String file){
		this.file = file;
	}

	public String getFileName(){
		return file;
	}

}
