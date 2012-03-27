package com.turt2live.antishare;

import org.bukkit.plugin.Plugin;

public class Conflicts {

	public boolean INVENTORY_CONFLICT_PRESENT = false;
	public boolean CREATIVE_MANAGER_CONFLICT_PRESENT = false;
	public boolean WORLD_MANAGER_CONFLICT_PRESENT = false;
	public boolean OTHER_CONFLICT_PRESENT = false;

	public String INVENTORY_CONFLICT = "None";
	public String CREATIVE_MANAGER_CONFLICT = "None";
	public String WORLD_MANAGER_CONFLICT = "None";
	public String OTHER_CONFLICT = "None";

	private AntiShare plugin;

	public Conflicts(AntiShare plugin){
		this.plugin = plugin;
		findInventoryManagerConflicts(plugin.getServer().getPluginManager().getPlugins());
		findCreativeModeManagerConflicts(plugin.getServer().getPluginManager().getPlugins());
		findWorldManagerConflicts(plugin.getServer().getPluginManager().getPlugins());
		findOtherConflicts(plugin.getServer().getPluginManager().getPlugins());
	}

	private void showInventoryConflict(String name){
		if(plugin.getConfig().getBoolean("settings.ignore-conflict-warning")){
			return;
		}
		plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "[CONFLICT] Inventory Manager Conflict: " + name);
		plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "[CONFLICT] AntiShare will not deal with inventories because of the conflict");
	}

	private void showCreativeConflict(String name){
		if(plugin.getConfig().getBoolean("settings.ignore-conflict-warning")){
			return;
		}
		plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "[CONFLICT] Creative Mode Manager Conflict: " + name);
		plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "[CONFLICT] AntiShare will disable itself because of the conflict");
		plugin.getServer().getPluginManager().disablePlugin(plugin);
	}

	private void showWorldManagerConflict(String name){
		if(plugin.getConfig().getBoolean("settings.ignore-conflict-warning")){
			return;
		}
		plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "[CONFLICT] World Manager Conflict: " + name);
		plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "[CONFLICT] AntiShare will not deal with allowance of world transfers because of the conflict");
	}

	private void showOtherConflict(String name){
		if(plugin.getConfig().getBoolean("settings.ignore-conflict-warning")){
			return;
		}
		plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "[CONFLICT] Other Conflict: " + name);
		plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "[CONFLICT] AntiShare won't do anything, but there may be problems because of the conflict");
	}

	private void findInventoryManagerConflicts(Plugin[] plugins){
		for(Plugin plugin : plugins){
			String name = plugin.getName();
			if(name.equalsIgnoreCase("ClearInv")){
				INVENTORY_CONFLICT_PRESENT = true;
				INVENTORY_CONFLICT = name;
				showInventoryConflict(name);
				continue;
			}else if(name.equalsIgnoreCase("ChangeDat")){
				INVENTORY_CONFLICT_PRESENT = true;
				INVENTORY_CONFLICT = name;
				showInventoryConflict(name);
				continue;
			}else if(name.equalsIgnoreCase("MultiInv") || name.equalsIgnoreCase("Multiverse-Inventories")){
				INVENTORY_CONFLICT_PRESENT = true;
				INVENTORY_CONFLICT = name;
				showInventoryConflict(name);
				continue;
			}else if(name.equalsIgnoreCase("Multiworld")){
				INVENTORY_CONFLICT_PRESENT = true;
				INVENTORY_CONFLICT = name;
				showInventoryConflict(name);
				continue;
			}else if(name.equalsIgnoreCase("AutoGamemode")){
				INVENTORY_CONFLICT_PRESENT = true;
				INVENTORY_CONFLICT = name;
				showInventoryConflict(name);
				continue;
			}
		}
	}

	private void findCreativeModeManagerConflicts(Plugin[] plugins){
		for(Plugin plugin : plugins){
			String name = plugin.getName();
			if(name.equalsIgnoreCase("CreativeControl")){
				CREATIVE_MANAGER_CONFLICT_PRESENT = true;
				CREATIVE_MANAGER_CONFLICT = name;
				showCreativeConflict(name);
				continue;
			}else if(name.equalsIgnoreCase("BurningCreativeSuite") || name.equalsIgnoreCase("BurningCreative") || name.equalsIgnoreCase("BCS")){
				CREATIVE_MANAGER_CONFLICT_PRESENT = true;
				CREATIVE_MANAGER_CONFLICT = name;
				showCreativeConflict(name);
				continue;
			}else if(name.equalsIgnoreCase("LimitedCreative")){
				CREATIVE_MANAGER_CONFLICT_PRESENT = true;
				CREATIVE_MANAGER_CONFLICT = name;
				showCreativeConflict(name);
				continue;
			}else if(name.equalsIgnoreCase("Anti-Place-Destroy-Ignite") || name.equalsIgnoreCase("APDI")){
				CREATIVE_MANAGER_CONFLICT_PRESENT = true;
				CREATIVE_MANAGER_CONFLICT = name;
				showCreativeConflict(name);
				continue;
			}else if(name.equalsIgnoreCase("iCreative")){
				CREATIVE_MANAGER_CONFLICT_PRESENT = true;
				CREATIVE_MANAGER_CONFLICT = name;
				showCreativeConflict(name);
				continue;
			}else if(name.equalsIgnoreCase("GameModeNoPlace")){
				CREATIVE_MANAGER_CONFLICT_PRESENT = true;
				CREATIVE_MANAGER_CONFLICT = name;
				showCreativeConflict(name);
				continue;
			}else if(name.equalsIgnoreCase("NoDrop")){
				CREATIVE_MANAGER_CONFLICT_PRESENT = true;
				CREATIVE_MANAGER_CONFLICT = name;
				showCreativeConflict(name);
				continue;
			}
		}
	}

	private void findWorldManagerConflicts(Plugin[] plugins){
		for(Plugin plugin : plugins){
			String name = plugin.getName();
			if(name.toLowerCase().contains("multiverse")){ // Will likely have MV-Core
				WORLD_MANAGER_CONFLICT_PRESENT = true;
				WORLD_MANAGER_CONFLICT = "Multiverse";
				showWorldManagerConflict(name);
				continue;
			}else if(name.equalsIgnoreCase("Multiworld")){
				OTHER_CONFLICT_PRESENT = true;
				OTHER_CONFLICT = name;
				showWorldManagerConflict(name);
				continue;
			}
		}
	}

	private void findOtherConflicts(Plugin[] plugins){
		for(Plugin plugin : plugins){
			String name = plugin.getName();
			if(name.equalsIgnoreCase("AntiGrief")){
				OTHER_CONFLICT_PRESENT = true;
				OTHER_CONFLICT = name;
				showOtherConflict(name);
				continue;
			}else if(name.equalsIgnoreCase("MobEggs")){
				OTHER_CONFLICT_PRESENT = true;
				OTHER_CONFLICT = name;
				showOtherConflict(name);
				continue;
			}else if(name.equalsIgnoreCase("WorldGuard")){
				OTHER_CONFLICT_PRESENT = true;
				OTHER_CONFLICT = name;
				showOtherConflict(name);
				continue;
			}else if(name.equalsIgnoreCase("MobArena")){
				OTHER_CONFLICT_PRESENT = true;
				OTHER_CONFLICT = name;
				showOtherConflict(name);
				continue;
			}else if(name.equalsIgnoreCase("CreativeStick")){
				OTHER_CONFLICT_PRESENT = true;
				OTHER_CONFLICT = name;
				showOtherConflict(name);
				continue;
			}else if(name.equalsIgnoreCase("Regios")){
				OTHER_CONFLICT_PRESENT = true;
				OTHER_CONFLICT = name;
				showOtherConflict(name);
				continue;
			}else if(name.toLowerCase().startsWith("voxel")){
				OTHER_CONFLICT_PRESENT = true;
				OTHER_CONFLICT = name;
				showOtherConflict(name);
				continue;
			}else if(name.equalsIgnoreCase("Essentials")){
				OTHER_CONFLICT_PRESENT = true;
				OTHER_CONFLICT = name;
				showOtherConflict(name);
				continue;
			}
		}
	}

}
