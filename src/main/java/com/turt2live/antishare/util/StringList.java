package com.turt2live.antishare.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.turt2live.antishare.TabHandler;

/**
 * Class to store a list of Strings for use in the {@link TabHandler}
 */
public class StringList {

	private String[] array;

	/**
	 * Creates a new String List
	 * 
	 * @param strings the list of strings to store
	 */
	public StringList(String... strings){
		array = strings;
	}

	/**
	 * Gets the list of strings as an array
	 * 
	 * @return the array of strings
	 */
	public final String[] get(){
		return array;
	}

	/**
	 * Gets an immutable list of the strings
	 * 
	 * @return an immutable list of the strings
	 */
	public final List<String> getList(){
		List<String> list = new ArrayList<String>();
		for(String a : array){
			list.add(a);
		}
		Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
		return Collections.unmodifiableList(list);
	}

	/**
	 * Determines if this string list is an error list
	 * 
	 * @return true if this is an error list, false otherwise
	 */
	public final boolean isError(){
		return this instanceof ErrorStringList;
	}

	/**
	 * Prints a message to the user, if {@link #isError()} is true this will send the error, otherwise it does nothing.
	 * 
	 * @param sender the CommandSender to send to
	 */
	public void print(CommandSender sender){
		// Do nothing
	}

}
