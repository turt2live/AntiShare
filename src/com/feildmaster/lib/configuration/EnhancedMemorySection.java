package com.feildmaster.lib.configuration;

import java.util.regex.Pattern;

import org.bukkit.configuration.MemorySection;

// TODO: Make configuration *always* use Enhanced Sections
// TODO: Move section overrides to this class
/**
 * A placeholder for enhanced sections
 * 
 * @author Feildmaster
 */
public class EnhancedMemorySection extends MemorySection {
	protected final EnhancedConfiguration superParent;

	public EnhancedMemorySection(EnhancedConfiguration superParent, MemorySection parent, String path){
		super(parent, path);
		this.superParent = superParent;
	}

	@Override
	public void set(String path, Object value){
		if(value != null && !value.equals(get(path)) || value == null && get(path) != null){
			superParent.modified = true;
		}
		super.set(path, value);
	}

	@Override
	public EnhancedMemorySection getConfigurationSection(String path){
		return (EnhancedMemorySection) super.getConfigurationSection(path);
	}

	@Override
	public EnhancedMemorySection createSection(String path){
		if(path == null){
			throw new IllegalArgumentException("Path cannot be null");
		}else if(path.length() == 0){
			throw new IllegalArgumentException("Cannot create section at empty path");
		}

		String[] split = path.split(Pattern.quote(Character.toString(getRoot().options().pathSeparator())));
		EnhancedMemorySection section = this;

		for(int i = 0; i < split.length - 1; i++){
			EnhancedMemorySection last = section;
			section = getConfigurationSection(split[i]);

			if(section == null){
				section = last.createLiteralSection(split[i]);
			}
		}

		String key = split[split.length - 1];
		if(section == this){
			return createLiteralSection(key);
		}else{
			return section.createLiteralSection(key);
		}
	}

	//    @Override
	//    public EnhancedMemorySection createSection(String path) {
	//        if (path == null) {
	//            throw new IllegalArgumentException("Path cannot be null");
	//        } else if (path.length() == 0) {
	//            throw new IllegalArgumentException("Cannot create section at empty path");
	//        }
	//
	//        String key = getKey(path);
	//        EnhancedMemorySection section = this;
	//
	//        while (!path.isEmpty()) {
	//            if (!key.equals(path)) {
	//                path = path.substring(path.length()+1);
	//            } else {
	//                path = "";
	//            }
	//
	//            EnhancedMemorySection last = section;
	//            section = section.getConfigurationSection(key);
	//
	//            if (section == null) {
	//                section = last.createLiteralSection(key);
	//            }
	//
	//            key = getKey(path);
	//        }
	//
	//
	//        if (section == this) {
	//            return this.createLiteralSection(key);
	//        } else {
	//            return section.createLiteralSection(key);
	//        }
	//    }

	public EnhancedMemorySection createLiteralSection(String key){
		EnhancedMemorySection newSection = new EnhancedMemorySection(superParent, this, key);
		map.put(key, newSection);
		return newSection;
	}

	// Set
	// Get
	// mapChildrenValues/Keys
	// getValues
	// static CreatePath

	@SuppressWarnings ("unused")
	private String getKey(String path){
		int i = path.indexOf(".");
		int j = path.indexOf("'");

		if(i != -1 && i < j){
			return path.substring(0, i - 1);
		}else if(j != -1){
			return path.substring(0, j - 1);
		}else{
			return path;
		}
	}
}
