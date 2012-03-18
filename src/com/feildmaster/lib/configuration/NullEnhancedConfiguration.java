package com.feildmaster.lib.configuration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * Enhances Configuration further by saving null values
 * 
 * @author Feildmaster
 */
public class NullEnhancedConfiguration extends EnhancedConfiguration {
	public NullEnhancedConfiguration(Plugin plugin){
		super(plugin, false);

		reflectYaml();
	}

	public NullEnhancedConfiguration(String file, Plugin plugin){
		super(file, plugin, false);

		reflectYaml();
	}

	public NullEnhancedConfiguration(File file, Plugin plugin){
		super(file, plugin, false);

		reflectYaml();
	}

	@Override
	public void set(String path, Object value){
		if(path.length() == 0){
			throw new IllegalArgumentException("Cannot set to an empty path");
		}

		String[] split = path.split(Pattern.quote(Character.toString(getRoot().options().pathSeparator())));
		NullEnhancedMemorySection section = null;

		for(int i = 0; i < split.length - 1; i++){
			NullEnhancedMemorySection last = section;

			if(last != null){
				section = last.getConfigurationSection(split[i]);
				if(section == null){
					section = last.createSection(split[i]);
				}
			}
		}

		String key = split[split.length - 1];
		if(section == null){
			this.map.put(key, value);
		}else{
			section.set(key, value);
		}
	}

	/**
	 * Removes the specified path from the configuration.
	 * 
	 * @param path The path to remove
	 */
	@Override
	public void unset(String path){
		String[] split = path.split(Pattern.quote(Character.toString(getRoot().options().pathSeparator())));
		NullEnhancedMemorySection section = getConfigurationSection(path);

		String key = split[split.length - 1];
		if(section == null){
			remove(key);
		}else{
			section.remove(key);
		}
	}

	protected void remove(String key){
		map.remove(key);
	}

	@Override
	public NullEnhancedMemorySection getConfigurationSection(String path){ // Sections are exact paths now!
		if(path == null){
			throw new IllegalArgumentException("Path cannot be null");
		}

		NullEnhancedMemorySection section = (NullEnhancedMemorySection) super.getConfigurationSection(path);
		if(section == null){
			section = createSection(path);
		}

		return section;
	}

	@Override
	public NullEnhancedMemorySection createSection(String path){
		if(path == null){
			throw new IllegalArgumentException("Path cannot be null");
		}else if(path.length() == 0){
			throw new IllegalArgumentException("Cannot create section at empty path");
		}

		String[] split = path.split(Pattern.quote(Character.toString(getRoot().options().pathSeparator())));
		NullEnhancedMemorySection section = null;

		for(int i = 0; i < split.length - 1; i++){
			NullEnhancedMemorySection last = section;
			if(section != null){
				section = getConfigurationSection(split[i]);
			}

			if(section == null){
				if(last == null){
					section = createLiteralSection(split[i]);
				}else{
					section = last.createLiteralSection(split[i]);
				}
			}
		}

		String key = split[split.length - 1];
		if(section == null){
			return createLiteralSection(key);
		}else{
			return section.createLiteralSection(key);
		}
	}

	@Override
	public NullEnhancedMemorySection createLiteralSection(String key){
		NullEnhancedMemorySection newSection = new NullEnhancedMemorySection(this, this, key);
		map.put(key, newSection);
		return newSection;
	}

	private void reflectYaml(){
		try{
			@SuppressWarnings ("rawtypes")
			Class yamlClass = this.getClass();
			while (!yamlClass.equals(YamlConfiguration.class)){
				yamlClass = yamlClass.getSuperclass();
			}

			// Set the representer
			Field representer = yamlClass.getDeclaredField("yamlRepresenter");
			representer.setAccessible(true);
			representer.set(this, new EnhancedRepresenter());
			// Get the options
			Field options = yamlClass.getDeclaredField("yamlOptions");
			options.setAccessible(true);
			// Set the yaml
			Field yaml = yamlClass.getDeclaredField("yaml");
			yaml.setAccessible(true);
			yaml.set(this, new Yaml(new YamlConstructor(), (EnhancedRepresenter) representer.get(this), (DumperOptions) options.get(this)));
		}catch(Exception ex){
			getPlugin().getLogger().log(java.util.logging.Level.SEVERE, null, ex);
		}

		load();
	}
}
