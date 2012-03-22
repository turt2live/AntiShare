package com.turt2live.antishare.debug;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.turt2live.antishare.regions.ASRegion;

public class BugCheck {

	public static void verifyEqualRegion(ASRegion object1, ASRegion object2, String message, Class<?> offendingClass){
		if(message == null || offendingClass == null){
			Bug bug = new Bug(new BugException("Null: REEGION EQUAL TO"), "Values: '" + object1 + "' '" + object2 + "' '" + message + "' '" + offendingClass + "'", BugCheck.class, null);
			Debugger.sendBug(bug);
			return;
		}
		if(object1 == null && object2 == null){
			return; //Equal
		}else if(object1 == null || object2 == null){ //Either
			Bug bug = new Bug(new BugException("[TYPE 1] Failed to make region " + (object1 == null ? "null" : object1.getUniqueID()) + " equal to " + (object2 == null ? "null" : object2.getUniqueID())), message, offendingClass, null);
			bug.getMessage();
			Debugger.sendBug(bug);
		}
		try{
			if(object1.hashCode() != object2.hashCode()){
				Bug bug = new Bug(new BugException("[TYPE 2] Failed to make region " + object1.getUniqueID() + " equal to " + object2.getUniqueID()), message, offendingClass, null);
				Debugger.sendBug(bug);
			}
		}catch(Exception e){
			throwInternalBug(e, "Failed: " + message);
		}
	}

	public static void verifyEqual(Object object1, Object object2, String message, Class<?> offendingClass){
		if(object1 == null || object2 == null || message == null || offendingClass == null){
			Bug bug = new Bug(new BugException("Null: EQUAL TO"), "Values: '" + object1 + "' '" + object2 + "' '" + message + "' '" + offendingClass + "'", BugCheck.class, null);
			Debugger.sendBug(bug);
			return;
		}
		try{
			if(!object1.equals(object2)){
				Bug bug = new Bug(new BugException("Failed to make " + object1 + " equal to " + object2), message, offendingClass, null);
				Debugger.sendBug(bug);
			}
		}catch(Exception e){
			throwInternalBug(e, "Failed: " + message);
		}
	}

	public static void verifyNotEqualRegion(ASRegion object1, ASRegion object2, String message, Class<?> offendingClass){
		if(message == null || offendingClass == null){
			Bug bug = new Bug(new BugException("Null: REGION NOT EQUAL TO"), "Values: '" + object1 + "' '" + object2 + "' '" + message + "' '" + offendingClass + "'", BugCheck.class, null);
			Debugger.sendBug(bug);
			return;
		}
		if(object1 == null && object2 == null){ //Equal
			Bug bug = new Bug(new BugException("[TYPE 1] Failed to make region " + (object1 == null ? "null" : object1.getUniqueID()) + " not equal to " + (object2 == null ? "null" : object2.getUniqueID())), message, offendingClass, null);
			Debugger.sendBug(bug);
		}else if(object1 == null || object2 == null){ //Either
			return;
		}
		try{
			if(object1.hashCode() == object2.hashCode()){
				Bug bug = new Bug(new BugException("[TYPE 2] Failed to make region " + object1.getUniqueID() + " not equal to " + object2.getUniqueID()), message, offendingClass, null);
				Debugger.sendBug(bug);
			}
		}catch(Exception e){
			throwInternalBug(e, "Failed: " + message);
		}
	}

	public static void verifyNotEqual(Object object1, Object object2, String message, Class<?> offendingClass){
		if(object1 == null || object2 == null || message == null || offendingClass == null){
			Bug bug = new Bug(new BugException("Null: NOT EQUAL TO"), "Values: '" + object1 + "' '" + object2 + "' '" + message + "' '" + offendingClass + "'", BugCheck.class, null);
			Debugger.sendBug(bug);
			return;
		}
		try{
			if(object1.equals(object2)){
				Bug bug = new Bug(new BugException("Failed to make " + object1 + " not equal to " + object2), message, offendingClass, null);
				Debugger.sendBug(bug);
			}
		}catch(Exception e){
			throwInternalBug(e, "Failed: " + message);
		}
	}

	public static void verifyLessThan(Float object1, Float object2, String message, Class<?> offendingClass){
		if(object1 == null || object2 == null || message == null || offendingClass == null){
			Bug bug = new Bug(new BugException("Null: LESS THAN"), "Values: '" + object1 + "' '" + object2 + "' '" + message + "' '" + offendingClass + "'", BugCheck.class, null);
			Debugger.sendBug(bug);
			return;
		}
		try{
			if(!(object1 < object2)){
				Bug bug = new Bug(new BugException("Failed to make " + object1 + " less than " + object2), message, offendingClass, null);
				Debugger.sendBug(bug);
			}
		}catch(Exception e){
			throwInternalBug(e, "Failed: " + message);
		}
	}

	public static void verifyGreaterThan(Float object1, Float object2, String message, Class<?> offendingClass){
		if(object1 == null || object2 == null || message == null || offendingClass == null){
			Bug bug = new Bug(new BugException("Null: GREATER THAN"), "Values: '" + object1 + "' '" + object2 + "' '" + message + "' '" + offendingClass + "'", BugCheck.class, null);
			Debugger.sendBug(bug);
			return;
		}
		try{
			if(!(object1 > object2)){
				Bug bug = new Bug(new BugException("Failed to make " + object1 + " greater than " + object2), message, offendingClass, null);
				Debugger.sendBug(bug);
			}
		}catch(Exception e){
			throwInternalBug(e, "Failed: " + message);
		}
	}

	public static void verifyWritten(File file, String line, String message, Class<?> offendingClass, boolean caseSensitive, boolean startsWith){
		if(file == null || message == null || offendingClass == null){
			Bug bug = new Bug(new BugException("Null: WRITTEN"), "Values: '" + file + "' '" + message + "' '" + offendingClass + "'", BugCheck.class, null);
			Debugger.sendBug(bug);
			return;
		}
		try{
			line = line.trim().replace("\t", "");
			BufferedReader in = new BufferedReader(new FileReader(file));
			boolean found = false;
			String rline;
			while ((rline = in.readLine()) != null){
				rline = rline.trim().replace("\t", "");
				if(startsWith){
					if(caseSensitive){
						if(rline.startsWith(line)){
							found = true;
							break;
						}
					}else{
						if(rline.toLowerCase().startsWith(line.toLowerCase())){
							found = true;
							break;
						}
					}
				}else{
					if(caseSensitive){
						if(rline.equals(line)){
							found = true;
							break;
						}
					}else{
						if(rline.equalsIgnoreCase(line)){
							found = true;
							break;
						}
					}
				}
			}
			in.close();
			if(!found){
				Bug bug = new Bug(new BugException("Failed to find " + line + " in file " + file.getName()), message, offendingClass, null);
				Debugger.sendBug(bug);
			}
		}catch(Exception e){
			throwInternalBug(e, "Failed: " + message);
		}
	}

	private static void throwInternalBug(Exception e, String message){
		Bug bug = new Bug(e, "INTERNAL: " + message, BugCheck.class, null);
		Debugger.sendBug(bug);
	}
}
