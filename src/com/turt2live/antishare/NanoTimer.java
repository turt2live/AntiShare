package com.turt2live.antishare;

import java.util.concurrent.ConcurrentHashMap;

public class NanoTimer {

	private static ConcurrentHashMap<Integer, Long> starts = new ConcurrentHashMap<Integer, Long>();

	public static int start(){
		Long now = System.nanoTime();
		int key = starts.size() + 1;
		starts.put(key, now);
		return key;
	}

	public static void stop(int id, String name){
		Long now = System.nanoTime();
		Long then = starts.get(id);
		if(then != null){
			long diff = now - then;
			System.out.println(id + ": " + diff + " (" + name + ")");
		}else{
			System.out.println("Invalid timer, " + id);
		}
	}

}
