package com.feildmaster.lib.configuration;

import java.util.regex.Pattern;
import org.bukkit.configuration.MemorySection;

public class NullEnhancedMemorySection extends EnhancedMemorySection {
    public NullEnhancedMemorySection(EnhancedConfiguration superParent, MemorySection parent, String path) {
        super(superParent, parent, path);
    }

    public void set(String path, Object value) {
        if (path.length() == 0) {
            throw new IllegalArgumentException("Cannot set to an empty path");
        }

        String[] split = path.split(Pattern.quote(Character.toString(getRoot().options().pathSeparator())));
        NullEnhancedMemorySection section = this;

        for (int i = 0; i < split.length - 1; i++) {
            NullEnhancedMemorySection last = section;

            section = last.getConfigurationSection(split[i]);

            if (section == null) {
                section = last.createSection(split[i]);
            }
        }

        String key = split[split.length - 1];
        if (section == this) {
            this.map.put(key, value);
        } else {
            section.set(key, value);
        }
    }

    /**
     * Removes the specified path from the configuration.
     *
     * @param path The path to remove
     */
    public void unset(String path) {
        String[] split = path.split(Pattern.quote(Character.toString(getRoot().options().pathSeparator())));
        NullEnhancedMemorySection section = getConfigurationSection(path);

        if (section != null) {
            String key = split[split.length-1];
            section.remove(key);
        }
    }

    protected void remove(String key) {
        this.map.remove(key);
    }

    @Override
    public NullEnhancedMemorySection getConfigurationSection(String path) { // Sections are exact paths now!
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        NullEnhancedMemorySection section = (NullEnhancedMemorySection) super.getConfigurationSection(path);
        if (section == null) {
            section = createSection(path);
        }

        return section;
    }

    @Override
    public NullEnhancedMemorySection createSection(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        } else if (path.length() == 0) {
            throw new IllegalArgumentException("Cannot create section at empty path");
        }

        String[] split = path.split(Pattern.quote(Character.toString(getRoot().options().pathSeparator())));
        NullEnhancedMemorySection section = this;

        for (int i = 0; i < split.length - 1; i++) {
            NullEnhancedMemorySection last = section;
            if (section != null) {
                section = getConfigurationSection(split[i]);
            }

            if (section == null) {
                if (last == null) {
                    section = createLiteralSection(split[i]);
                } else {
                    section = last.createLiteralSection(split[i]);
                }
            }
        }

        String key = split[split.length - 1];
        return section.createLiteralSection(key);
    }

    public NullEnhancedMemorySection createLiteralSection(String key) {
        NullEnhancedMemorySection newSection = new NullEnhancedMemorySection(superParent, this, key);
        map.put(key, newSection);
        return newSection;
    }
}
