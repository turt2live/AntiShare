package com.turt2live.antishare.worldedit;

import com.turt2live.antishare.enums.RegionKeyType;

public class RegionKey {

	public static boolean isKey(String key){
		if(key.equalsIgnoreCase("name")
				|| key.equalsIgnoreCase("ShowEnterMessage")
				|| key.equalsIgnoreCase("ShowExitMessage")
				|| key.equalsIgnoreCase("inventory")
				|| key.equalsIgnoreCase("area")
				|| key.equalsIgnoreCase("gamemode")){
			return true;
		}
		return false;
	}

	public static RegionKeyType getKey(String key){
		if(key.equalsIgnoreCase("name")){
			return RegionKeyType.NAME;
		}
		if(key.equalsIgnoreCase("ShowEnterMessage")){
			return RegionKeyType.ENTER_MESSAGE_SHOW;
		}
		if(key.equalsIgnoreCase("ShowExitMessage")){
			return RegionKeyType.EXIT_MESSAGE_SHOW;
		}
		if(key.equalsIgnoreCase("inventory")){
			return RegionKeyType.INVENTORY;
		}
		if(key.equalsIgnoreCase("gamemode")){
			return RegionKeyType.GAMEMODE;
		}
		if(key.equalsIgnoreCase("area")){
			return RegionKeyType.SELECTION_AREA;
		}
		return RegionKeyType.UNKNOWN;
	}

	public static boolean requiresValue(RegionKeyType type){
		switch (type){
		case SELECTION_AREA:
			return false;
		}
		return true;
	}
}
