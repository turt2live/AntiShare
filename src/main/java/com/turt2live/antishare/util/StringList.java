package com.turt2live.antishare.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

public class StringList {

	private String[] array;

	public StringList(String... strings){
		array = strings;
	}

	public String[] get(){
		return array;
	}

	public List<String> getList(){
		List<String> list = new ArrayList<String>();
		for(String a : array){
			list.add(a);
		}
		Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
		return list;
	}

	public boolean isError(){
		return false;
	}

	public void print(CommandSender sender){
		// Do nothing
	}

}