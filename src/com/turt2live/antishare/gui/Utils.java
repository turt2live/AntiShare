package com.turt2live.antishare.gui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class Utils {

	/**
	 * Determines if 2 vectors have the same keys as well as the same number of each duplicate key.
	 * <P>
	 * <b>Example:</b><br>
	 * A vector with the elements <i>Test, Test, Test2</i> compared to <i>Test, Test2, NotTest</i> would return false however if <i>Test, Test, Test2</i> was compared to <i>Test, Test, Test2</i> it would return true.<br>
	 * <b>This method works best on Strings although other objects (in fact, all) are supported.</b> If you decide to use other objects, be warned that it only uses .equals() on the object, so they would have to be the same instance for this to return true. <br>
	 * <i>Generally you will want 'vec1' to be your 'key' and 'vec2' to be the checked against 'vec1'</i>
	 * </P>
	 * 
	 * @param vec1 the first vector
	 * @param vec2 the second vector
	 * @return true if they have the same keys, and same number of keys; false otherwise.
	 */
	public static boolean containsCount(Vector<?> vec1, Vector<?> vec2){
		HashMap<Object, Integer> vec1count = new HashMap<Object, Integer>();
		HashMap<Object, Integer> vec2count = new HashMap<Object, Integer>();
		for(Object o : vec1){
			if(vec1count.containsKey(o)){
				vec1count.put(o, vec1count.get(o).intValue() + 1);
			}else{
				vec1count.put(o, 1);
			}
		}
		for(Object o : vec2){
			if(vec2count.containsKey(o)){
				vec2count.put(o, vec2count.get(o).intValue() + 1);
			}else{
				vec2count.put(o, 1);
			}
		}
		Iterator<Object> vec1objects = vec1count.keySet().iterator();
		while (vec1objects.hasNext()){
			Object o = vec1objects.next();
			if(vec2count.containsKey(o)){
				if(vec2count.get(o) != vec1count.get(o)){
					return false;
				}
			}else{
				return false;
			}
		}
		return true;
	}

	/**
	 * Counts the occurrence of a sequence in a String
	 * 
	 * @param sequence the sequence to search for
	 * @param line the line to search in
	 * @return the number of occurrences
	 */
	public static int count(String sequence, String line){
		int count = 0;
		int index = 0;
		while (index < line.length()){
			if(line.substring(index).contains(sequence)){
				count++;
				index += sequence.length();
			}else{
				break; // The rest of the string does not have it, why bother checking?
			}
		}
		return count;
	}

}
