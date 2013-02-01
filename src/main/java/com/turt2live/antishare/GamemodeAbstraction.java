package com.turt2live.antishare;

import org.bukkit.GameMode;

public class GamemodeAbstraction {

	/**
	 * Determines if AntiShare sees 2 GameModes as the same
	 * 
	 * @param gm1 first gamemode
	 * @param gm2 second gamemode
	 * @return true if AntiShare sees gm1 and gm2 as the same
	 */
	public static boolean isMatch(GameMode gm1, GameMode gm2){
		if(isCreative(gm1) && isCreative(gm2)){
			return true;
		}
		return gm1 == gm2;
	}

	/**
	 * Determines if a GameMode is just like Creative Mode (to AntiShare)
	 * 
	 * @param gm1 the gamemode
	 * @return true if AntiShare sees gm1 as Creative Mode
	 */
	public static boolean isCreative(GameMode gm1){
		if(gm1 == null){
			return false;
		}
		if(!adventureIsCreative()){
			return gm1 == GameMode.CREATIVE;
		}
		return gm1 == GameMode.CREATIVE || gm1 == GameMode.ADVENTURE;
	}

	/**
	 * Determines if AntiShare will be seeing Adventure mode and Creative mode as the same
	 * 
	 * @return true if Adventure and Creative mode are the same (to AntiShare)
	 */
	public static boolean adventureIsCreative(){
		AntiShare plugin = AntiShare.getInstance();
		return plugin.getConfig().getBoolean("other.adventure-is-creative");
	}

}
