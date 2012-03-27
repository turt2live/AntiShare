package com.turt2live.antishare.storage;

import java.util.HashMap;

public class Meta {

	private HashMap<String, Object> values = new HashMap<String, Object>();

	public Meta(){}

	public void add(String key, Object value){
		values.put(key, value);
	}

	public Object get(String key){
		if(values.containsKey(key)){
			return values.get(key);
		}
		return null;
	}

	public void unset(String key){
		values.remove(key);
	}
}