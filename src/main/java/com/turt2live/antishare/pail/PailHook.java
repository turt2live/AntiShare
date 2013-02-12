package com.turt2live.antishare.pail;

import me.escapeNT.pail.Pail;

import org.bukkit.plugin.Plugin;

public class PailHook {

	public static void start(Plugin pailPlugin){
		Pail pail = (Pail) pailPlugin;
		pail.loadInterfaceComponent("AntiShare", new PailPane(pail));
	}

}
