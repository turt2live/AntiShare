package com.turt2live.antishare;

import org.bukkit.GameMode;

import com.turt2live.materials.ServerHas;

// TODO: Document

public class GamemodeAbstraction {

	public static boolean isMatch(GameMode gm1, GameMode gm2){
		if(isCreative(gm1) && isCreative(gm2)){
			return true;
		}
		return gm1 == gm2;
	}

	public static boolean isCreative(GameMode gm1){
		if(gm1 == null){
			return false;
		}
		if(!adventureIsCreative()){
			return gm1 == GameMode.CREATIVE;
		}
		return gm1 == GameMode.CREATIVE || (ServerHas.adventureMode() && gm1 == GameMode.ADVENTURE);
	}

	public static boolean adventureIsCreative(){
		AntiShare plugin = AntiShare.getInstance();
		return plugin.getConfig().getBoolean("other.adventure-is-creative");
	}

}
