package com.turt2live.antishare.compatibility.other;

import com.turt2live.antishare.AntiShare;

/**
 * PermissionsEx Compatibility
 * 
 * @author turt2live
 */
public class PEX{

	private PEXAbstract pex;

	public PEX(){
		if(AntiShare.p.getServer().getPluginManager().getPlugin("PermissionsEx") != null){
			pex = new PEXAbstract();
		}
	}

	/**
	 * Determines if this server has PEX installed
	 * 
	 * @return true if installed and found
	 */
	public boolean hasPEX(){
		return pex != null;
	}

	/**
	 * Gets the raw abstract
	 * 
	 * @return the abstract
	 */
	public PEXAbstract getAbstract(){
		return pex;
	}

}
