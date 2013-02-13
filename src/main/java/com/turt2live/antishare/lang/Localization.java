package com.turt2live.antishare.lang;

import java.io.File;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;

public class Localization {

	private static Localization instance;

	/**
	 * Gets the localization instance
	 * 
	 * @return the localization instance
	 */
	public static Localization getInstance(){
		if(instance == null){
			instance = new Localization();
		}
		return instance;
	}

	private final AntiShare plugin;
	private final EnhancedConfiguration locale;
	private final String localeFileName;

	private Localization(){
		plugin = AntiShare.getInstance();
		for(Locale locale : Locale.values()){
			load(locale);
		}
		String localeFileName = plugin.getConfig().getString("other.locale-file");
		File locale = new File(plugin.getDataFolder(), "locale" + File.separator + localeFileName);
		if(!locale.exists()){
			plugin.getLogger().warning("Locale file (" + localeFileName + ") not found. Using locale_en_US.yml");
			locale = new File(plugin.getDataFolder(), "locale" + File.separator + "locale_en_US.yml");
		}
		this.locale = new EnhancedConfiguration(locale, plugin);
		this.localeFileName = locale.getName();
		this.locale.load();
		checkLocale(this.locale);
		if(!locale.getName().equalsIgnoreCase("locale_en_US.yml")){
			plugin.getLogger().info(getMessage(LocaleMessage.LOCALE_FILE));
		}
	}

	private void checkLocale(EnhancedConfiguration locale){
		locale.loadDefaults(plugin.getResource("locale/" + Locale.EN_US.getFileName()));
		if(locale.needsUpdate() || !locale.fileExists()){
			locale.saveDefaults();
		}
		locale.load();
	}

	private void load(Locale locale){
		File file = new File(plugin.getDataFolder(), "locale" + File.separator + locale.getFileName());
		EnhancedConfiguration yaml = new EnhancedConfiguration(file, plugin);
		yaml.loadDefaults(plugin.getResource("locale/" + locale.getFileName()));
		if(yaml.needsUpdate() || !yaml.fileExists()){
			yaml.saveDefaults();
		}
	}

	/**
	 * Gets a localized message
	 * 
	 * @param message the message
	 * @param arguments arguments for message, optional
	 * @return the localized message
	 */
	public String getLocalizedMessage(LocaleMessage message, String... arguments){
		if(message == null){
			return null;
		}
		// TODO: Warn if arguments are not being filled
		String localeMessage = locale.getString(message.getConfigurationNode());
		if(arguments != null){
			for(int i = 0; i < arguments.length; i++){
				String replaceString = "{" + i + "}";
				localeMessage = localeMessage.replace(replaceString, arguments[i]);
			}
		}
		if(AntiShare.isDebug()){
			AntiShare.getInstance().getLogger().info("[DEBUG] [Locale] " + message + " = " + localeMessage);
		}
		return localeMessage;
	}

	/**
	 * Gets a localized message
	 * 
	 * @param message the message
	 * @param arguments arguments for message, optional
	 * @return the localized message
	 */
	public String getLocalizedMessage(LocaleMessage message, LocaleMessage... arguments){
		if(arguments != null && arguments.length > 0){
			String[] strings = new String[arguments.length];
			int i = 0;
			for(LocaleMessage msg : arguments){
				strings[i] = getLocalizedMessage(msg, (String[]) null);
				i++;
			}
			return getLocalizedMessage(message, strings);
		}
		return getLocalizedMessage(message, (String[]) null);
	}

	/**
	 * Gets a localized message
	 * 
	 * @param message the message
	 * @param arguments arguments for message, optional
	 * @return the localized message
	 */
	public static String getMessage(LocaleMessage message, String... arguments){
		return getInstance().getLocalizedMessage(message, arguments);
	}

	/**
	 * Gets a localized message
	 * 
	 * @param message the message
	 * @param arguments arguments for message, optional
	 * @return the localized message
	 */
	public static String getMessage(LocaleMessage message, LocaleMessage... arguments){
		return getInstance().getLocalizedMessage(message, arguments);
	}

	/**
	 * Gets a localized message
	 * 
	 * @param message the message
	 * @return the localized message
	 */
	public static String getMessage(LocaleMessage message){
		return getInstance().getLocalizedMessage(message, (String[]) null);
	}

	/**
	 * Gets the locale file name
	 * 
	 * @return the locale file name
	 */
	public static String getLocaleFileName(){
		return getInstance().localeFileName;
	}

}
