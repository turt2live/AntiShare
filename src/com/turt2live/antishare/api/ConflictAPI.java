package com.turt2live.antishare.api;


public class ConflictAPI extends APIBase {

	/**
	 * Determines if there is an inventory change conflict on the server
	 * 
	 * @return true if a conflict exists
	 */
	public boolean isInventoryConflictPresent(){
		return getPlugin().getConflicts().INVENTORY_CONFLICT_PRESENT;
	}

	/**
	 * Determines if there is a world manager conflict on the server
	 * 
	 * @return true if a conflict exists
	 */
	public boolean isWorldManagerConflictPresent(){
		return getPlugin().getConflicts().WORLD_MANAGER_CONFLICT_PRESENT;
	}

	/**
	 * Determines if a creative manager conflict is on the server
	 * 
	 * @return true if a conflict is present
	 */
	public boolean isCreativeConflictPresent(){
		return getPlugin().getConflicts().CREATIVE_MANAGER_CONFLICT_PRESENT;
	}

	/**
	 * Determines if an "other" conflict is on the server
	 * 
	 * @return true if a conflict is present
	 */
	public boolean isOtherConflictPresent(){
		return getPlugin().getConflicts().OTHER_CONFLICT_PRESENT;
	}

	/**
	 * Gets the name of the currently conflicting inventory plugin (first found)
	 * 
	 * @return 'None' if no plugin is found to be conflicting, anything else is a plugin name
	 */
	public String getInventoryConflictName(){
		return getPlugin().getConflicts().INVENTORY_CONFLICT;
	}

	/**
	 * Gets the name of the currently conflicting world manager plugin (first found)
	 * 
	 * @return 'None' if no plugin is found to be conflicting, anything else is a plugin name
	 */
	public String getWorldManagerConflictName(){
		return getPlugin().getConflicts().WORLD_MANAGER_CONFLICT;
	}

	/**
	 * Gets the name of the currently conflicting creative manager plugin (first found)
	 * 
	 * @return 'None' if no plugin is found to be conflicting, anything else is a plugin name
	 */
	public String getCreativeConflictName(){
		return getPlugin().getConflicts().CREATIVE_MANAGER_CONFLICT;
	}

	/**
	 * Gets the name of the currently conflicting "other" plugin (first found)
	 * 
	 * @return 'None' if no plugin is found to be conflicting, anything else is a plugin name
	 */
	public String getOtherConflictName(){
		return getPlugin().getConflicts().OTHER_CONFLICT;
	}
}
