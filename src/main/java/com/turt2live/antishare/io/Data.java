package com.turt2live.antishare.io;

import java.io.File;

public class Data {

	private final String key;
	private final Object value;
	private final File file;

	public Data(String key, Object value, File file){
		this.key = key;
		this.value = value;
		this.file = file;
	}

	public String getKey(){
		return key;
	}

	public Object getValue(){
		return value;
	}

	public File getFile(){
		return file;
	}

}
