package com.turt2live.antishare.pail;

import me.escapeNT.pail.Pail;

import org.bukkit.plugin.Plugin;

/**
 * A class to isolate Pail code from AntiShare code
 * 
 * @author turt2live
 */
public class PailHook {

	/**
	 * Starts the Pail hook
	 * 
	 * @param pailPlugin the Pail plugin object
	 */
	public static void start(Plugin pailPlugin){
		Pail pail = (Pail) pailPlugin;
		pail.loadInterfaceComponent("AntiShare", new PailPane(pail));
	}

}
