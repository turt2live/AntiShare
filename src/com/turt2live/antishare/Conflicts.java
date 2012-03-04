package com.turt2live.antishare;

import org.bukkit.plugin.Plugin;

public class Conflicts {

	public boolean INVENTORY_CONFLICT_PRESENT = false;
	public boolean GAMEMODE_CONFLICT_PRESENT = false;
	public boolean CREATIVE_MANAGER_CONFLICT_PRESENT = false;
	public boolean OTHER_CONFLICT_PRESENT = false;

	public String INVENTORY_CONFLICT = "None";
	public String GAMEMODE_CONFLICT = "None";
	public String CREATIVE_MANAGER_CONFLICT = "None";
	public String OTHER_CONFLICT = "None";

	public Conflicts(AntiShare plugin){
		findInventoryManagerConflicts(plugin.getServer().getPluginManager().getPlugins());
		findGamemodeManagerConflicts(plugin.getServer().getPluginManager().getPlugins());
		findCreativeModeManagerConflicts(plugin.getServer().getPluginManager().getPlugins());
		findOtherConflicts(plugin.getServer().getPluginManager().getPlugins());
		if(INVENTORY_CONFLICT_PRESENT){
			AntiShare.log.severe("[AntiShare-Conflicts] Inventory Manager Conflict: " + INVENTORY_CONFLICT);
			AntiShare.log.severe("[AntiShare-Conflicts] AntiShare will not deal with inventories.");
		}
		if(GAMEMODE_CONFLICT_PRESENT){
			AntiShare.log.severe("[AntiShare-Conflicts] GameMode Manager Conflict: " + GAMEMODE_CONFLICT);
			AntiShare.log.severe("[AntiShare-Conflicts] AntiShare will not deal with gamemodes.");
		}
		if(CREATIVE_MANAGER_CONFLICT_PRESENT){
			AntiShare.log.severe("[AntiShare-Conflicts] Creative Mode Manager Conflict: " + CREATIVE_MANAGER_CONFLICT);
			AntiShare.log.severe("[AntiShare-Conflicts] AntiShare will disable itself.");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
		}
		if(OTHER_CONFLICT_PRESENT){
			AntiShare.log.severe("[AntiShare-Conflicts] Other Conflict: " + OTHER_CONFLICT);
			AntiShare.log.severe("[AntiShare-Conflicts] AntiShare won't do anything, but there may be problems.");
		}
	}

	private void findInventoryManagerConflicts(Plugin[] plugins){
		for(Plugin plugin : plugins){
			String name = plugin.getName();
			if(name.contains("ClearInv")){
				INVENTORY_CONFLICT_PRESENT = true;
				INVENTORY_CONFLICT = name;
				break;
			}else if(name.contains("ChangeDat")){
				INVENTORY_CONFLICT_PRESENT = true;
				INVENTORY_CONFLICT = name;
				break;
			}else if(name.contains("MultiInv") || name.contains("Multiverse-Inventories")){
				INVENTORY_CONFLICT_PRESENT = true;
				INVENTORY_CONFLICT = name;
				break;
			}else if(name.contains("Multiworld")){
				INVENTORY_CONFLICT_PRESENT = true;
				INVENTORY_CONFLICT = name;
				break;
			}else if(name.contains("AutoGamemode")){
				INVENTORY_CONFLICT_PRESENT = true;
				INVENTORY_CONFLICT = name;
				break;
			}
		}
	}

	// None found (yet?)
	private void findGamemodeManagerConflicts(Plugin[] plugins){
		//		for(Plugin plugin : plugins){
		//			String name = plugin.getName();
		//			if(name.contains("")){
		//				GAMEMODE_CONFLICT_PRESENT = true;
		//				GAMEMODE_CONFLICT = name;
		//				break;
		//			}
		//		}
	}

	private void findCreativeModeManagerConflicts(Plugin[] plugins){
		for(Plugin plugin : plugins){
			String name = plugin.getName();
			if(name.contains("CreativeControl")){
				CREATIVE_MANAGER_CONFLICT_PRESENT = true;
				CREATIVE_MANAGER_CONFLICT = name;
				break;
			}else if(name.contains("BurningCreativeSuite") || name.contains("BurningCreative") || name.contains("BCS")){
				CREATIVE_MANAGER_CONFLICT_PRESENT = true;
				CREATIVE_MANAGER_CONFLICT = name;
				break;
			}else if(name.contains("LimitedCreative")){
				CREATIVE_MANAGER_CONFLICT_PRESENT = true;
				CREATIVE_MANAGER_CONFLICT = name;
				break;
			}else if(name.contains("Anti-Place-Destroy-Ignite") || name.contains("APDI")){
				CREATIVE_MANAGER_CONFLICT_PRESENT = true;
				CREATIVE_MANAGER_CONFLICT = name;
				break;
			}else if(name.contains("iCreative")){
				CREATIVE_MANAGER_CONFLICT_PRESENT = true;
				CREATIVE_MANAGER_CONFLICT = name;
				break;
			}
		}
	}

	private void findOtherConflicts(Plugin[] plugins){
		for(Plugin plugin : plugins){
			String name = plugin.getName();
			if(name.contains("AntiGrief")){
				OTHER_CONFLICT_PRESENT = true;
				OTHER_CONFLICT = name;
				break;
			}else if(name.contains("MobEggs")){
				OTHER_CONFLICT_PRESENT = true;
				OTHER_CONFLICT = name;
				break;
			}
		}
	}

}
