package com.turt2live.antishare.io;

import java.util.Collection;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;

/**
 * Saves potion effects for players
 * 
 * @author turt2live
 */
public class PotionSaver extends GenericDataFile{

	/**
	 * Saves the potion effects of a player
	 * 
	 * @param player the player to get the effects from
	 * @param gamemode the gamemode to save as
	 */
	public static void saveEffects(Player player, GameMode gamemode){
		EnhancedConfiguration file = getFile("potions");
		Collection<PotionEffect> effects = player.getActivePotionEffects();
		file.set(player.getName() + "." + gamemode, null); // Remove current save
		int i = 0;
		for(PotionEffect effect : effects){
			file.set(player.getName() + "." + gamemode + ".potion" + i, effect);
			i++;
		}
		file.save();
	}

	/**
	 * Applies the saved effects to the player. This assumes that the current effects on the
	 * player are already dealt with. This method will clear any existing effects on the player.
	 * 
	 * @param player the player to apply the effects to
	 * @param gamemode the gamemode to get the effects from
	 */
	public static void applySavedEffects(Player player, GameMode gamemode){
		EnhancedConfiguration file = getFile("potions");
		for(PotionEffect effect : player.getActivePotionEffects()){
			player.removePotionEffect(effect.getType());
		}
		ConfigurationSection section = file.getConfigurationSection(player.getName() + "." + gamemode);
		if(section == null){
			return;
		}
		Set<String> saved = section.getKeys(false);
		if(saved != null){
			for(String key : saved){
				Object obj = file.get(player.getName() + "." + gamemode + "." + key);
				if(obj instanceof PotionEffect){
					boolean added = player.addPotionEffect((PotionEffect) obj);
					if(!added){
						AntiShare.p.getLogger().warning(AntiShare.p.getMessages().getMessage("potion-effects-error1"));
					}
				}else{
					AntiShare.p.getLogger().warning(AntiShare.p.getMessages().getMessage("potion-effects-error2"));
				}
			}
		}
	}

}
