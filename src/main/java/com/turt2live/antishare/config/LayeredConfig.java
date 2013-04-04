package com.turt2live.antishare.config;

import java.util.List;

import com.feildmaster.lib.configuration.EnhancedConfiguration;

// TODO: Document
public class LayeredConfig{

	private final EnhancedConfiguration region, world, plugin;

	LayeredConfig(EnhancedConfiguration region, EnhancedConfiguration world, EnhancedConfiguration plugin){
		if(plugin == null){
			throw new IllegalArgumentException("Plugin configuration cannot be null");
		}
		this.region = region;
		this.world = world;
		this.plugin = plugin;
	}

	public void loadAll(){
		if(region != null)
			region.load();
		if(world != null)
			world.load();
		plugin.load();
	}

	public EnhancedConfiguration configFor(String path, boolean isList){
		if(region == null && world == null){
			return plugin;
		}
		final EnhancedConfiguration world = this.world == null ? plugin : this.world;
		final EnhancedConfiguration region = this.region == null ? world : this.region;
		if(isList){
			List<String> strings = region.getStringList(path);
			if(strings != null && strings.size() > 0 && (strings.get(0).equalsIgnoreCase("world") || strings.get(0).equalsIgnoreCase("global"))){
				if(strings.get(0).equalsIgnoreCase("world")){
					return world;
				}else{
					return plugin;
				}
			}else{
				return region;
			}
		}else{
			String string = region.getString(path);
			if(string == null){
				return region;
			}
			if(string.equalsIgnoreCase("world")){
				return world;
			}else if(string.equalsIgnoreCase("global")){
				return plugin;
			}
			return region;
		}
	}

}
