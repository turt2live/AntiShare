package com.turt2live.antishare.regions;

/**
 * Region Key - Used for simpler editing of regions
 * 
 * @author turt2live
 */
public class RegionKey {

	/**
	 * An enum to represent Region Key Types
	 * 
	 * @author turt2live
	 */
	public static enum RegionKeyType{
		NAME,
		ENTER_MESSAGE_SHOW,
		EXIT_MESSAGE_SHOW,
		INVENTORY,
		SELECTION_AREA,
		GAMEMODE,
		ENTER_MESSAGE,
		EXIT_MESSAGE,
		UNKNOWN
	}

	/**
	 * Determines if a typed key is actually a key
	 * 
	 * @param key the key
	 * @return true if it is a key
	 */
	public static boolean isKey(String key){
		if(key.equalsIgnoreCase("name")
				|| key.equalsIgnoreCase("ShowEnterMessage")
				|| key.equalsIgnoreCase("ShowExitMessage")
				|| key.equalsIgnoreCase("inventory")
				|| key.equalsIgnoreCase("area")
				|| key.equalsIgnoreCase("gamemode")
				|| key.equalsIgnoreCase("EnterMessage")
				|| key.equalsIgnoreCase("ExitMessage")){
			return true;
		}
		return false;
	}

	/**
	 * Gets the key associated with a typed key
	 * 
	 * @param key the key
	 * @return the actual key
	 */
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
		if(key.equalsIgnoreCase("entermessage")){
			return RegionKeyType.ENTER_MESSAGE;
		}
		if(key.equalsIgnoreCase("exitmessage")){
			return RegionKeyType.EXIT_MESSAGE;
		}
		return RegionKeyType.UNKNOWN;
	}

	/**
	 * Determines if a key requires a value or not
	 * 
	 * @param type the key
	 * @return true if a value is required
	 */
	public static boolean requiresValue(RegionKeyType type){
		switch (type){
		case SELECTION_AREA:
			return false;
		}
		return true;
	}
}
