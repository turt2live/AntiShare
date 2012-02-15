package com.turt2live.antishare;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.SQL.SQLManager;

public class ASInventory {

	public static void cleanup(){
		AntiShare plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
		File sdir = new File(plugin.getDataFolder(), "inventories");
		String world = Bukkit.getWorlds().get(0).getName();
		if(sdir.exists()){
			for(File f : sdir.listFiles()){
				if(f.getName().endsWith("CREATIVE.yml")
						|| f.getName().endsWith("SURVIVAL.yml")){
					File newName = new File(f.getParent(), f.getName().replace("SURVIVAL", "SURVIVAL_" + world).replace("CREATIVE", "CREATIVE_" + world));
					f.renameTo(newName);
				}
			}
		}
	}

	@SuppressWarnings ("deprecation")
	public static void load(Player player, GameMode gamemode){
		AntiShare plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
		boolean skip = false;
		if(plugin.getConfig().getBoolean("SQL.use") && plugin.getSQLManager() != null){
			if(plugin.getSQLManager().isConnected()){
				SQLManager sql = plugin.getSQLManager();
				player.getInventory().clear();
				ResultSet inventory = sql.getQuery("SELECT * FROM AntiShare_Inventory WHERE username='" + player.getName() + "' AND gamemode='" + gamemode.toString() + "' AND world='" + player.getWorld().getName() + "'");
				if(inventory != null){
					try{
						while (inventory.next()){
							int slot = inventory.getInt("slot");
							int id = inventory.getInt("itemID");
							//String name = inventory.getString("itemName");
							String durability = inventory.getString("itemDurability");
							int amount = inventory.getInt("itemAmount");
							byte data = Byte.parseByte(inventory.getString("itemData"));
							String enchants[] = inventory.getString("itemEnchant").split(" ");
							ItemStack item = new ItemStack(id);
							item.setAmount(amount);
							MaterialData itemData = item.getData();
							itemData.setData(data);
							item.setData(itemData);
							item.setDurability(Short.parseShort(durability));
							if(inventory.getString("itemEnchant").length() > 0){
								for(String enchant : enchants){
									String parts[] = enchant.split("\\|");
									String enchantID = parts[0];
									int level = Integer.parseInt(parts[1]);
									Enchantment e = Enchantment.getById(Integer.parseInt(enchantID));
									item.addEnchantment(e, level);
								}
							}
							player.getInventory().setItem(slot, item);
							player.updateInventory();
						}
						skip = true;
					}catch(SQLException e){
						plugin.log.severe("[" + plugin.getDescription().getFullName() + "] Cannot handle inventory: " + e.getMessage());
					}
				}else{
					skip = true;
				}
			}
		}
		if(skip){
			return;
		}
		try{
			File sdir = new File(plugin.getDataFolder(), "inventories");
			sdir.mkdirs();
			File saveFile = new File(sdir, player.getName() + "_" + gamemode.toString() + "_" + player.getWorld().getName() + ".yml");
			if(!saveFile.exists()){
				saveFile.createNewFile();
			}
			EnhancedConfiguration config = new EnhancedConfiguration(saveFile, plugin);
			config.load();
			Integer i = 0;
			Integer size = player.getInventory().getSize();
			player.getInventory().clear();
			for(i = 0; i < size; i++){
				ItemStack item = new ItemStack(0, 0);
				if(config.getItemStack(i.toString()) != null){
					item = config.getItemStack(i.toString());
					player.getInventory().setItem(i, item);
					player.updateInventory();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void save(Player player, GameMode gamemode){
		AntiShare plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
		wipe(player);
		boolean skip = false;
		if(plugin.getConfig().getBoolean("SQL.use") && plugin.getSQLManager() != null){
			if(plugin.getSQLManager().isConnected()){
				SQLManager sql = plugin.getSQLManager();
				Integer i = 0;
				Integer size = player.getInventory().getSize();
				for(i = 0; i < size; i++){
					ItemStack item = player.getInventory().getItem(i);
					String id = item.getTypeId() + "";
					String name = item.getType().name();
					String durability = item.getDurability() + "";
					String amount = item.getAmount() + "";
					String data = item.getData().getData() + "";
					String enchant = "";
					Set<Enchantment> enchantsSet = item.getEnchantments().keySet();
					Map<Enchantment, Integer> enchantsMap = item.getEnchantments();
					for(Enchantment e : enchantsSet){
						enchant = enchant + e.getId() + "|" + enchantsMap.get(e) + " ";
					}
					if(enchant.length() > 0){
						enchant = enchant.substring(0, enchant.length() - 1);
					}
					sql.insertQuery("INSERT INTO AntiShare_Inventory (username, gamemode, slot, itemID, itemName, itemDurability, itemAmount, itemData, itemEnchant, world) " +
							"VALUES ('" + player.getName() + "', '" + gamemode.toString() + "', '" + i + "', '" + id + "', '" + name + "', '" + durability + "', '" + amount + "', '" + data + "', '" + enchant + "', '" + player.getWorld().getName() + "')");
				}
				skip = true;
			}
		}
		if(skip){
			return;
		}
		try{
			File sdir = new File(plugin.getDataFolder(), "inventories");
			sdir.mkdirs();
			File saveFile = new File(sdir, player.getName() + "_" + gamemode.toString() + "_" + player.getWorld().getName() + ".yml");
			if(!saveFile.exists()){
				saveFile.createNewFile();
			}
			EnhancedConfiguration config = new EnhancedConfiguration(saveFile, plugin);
			config.load();
			Integer i = 0;
			Integer size = player.getInventory().getSize();
			for(i = 0; i < size; i++){
				ItemStack item = player.getInventory().getItem(i);
				config.set(i.toString(), item);
			}
			config.save();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void wipe(Player player){
		AntiShare plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
		boolean skip = false;
		if(plugin.getConfig().getBoolean("SQL.use") && plugin.getSQLManager() != null){
			if(plugin.getSQLManager().isConnected()){
				SQLManager sql = plugin.getSQLManager();
				sql.deleteQuery("DELETE FROM AntiShare_Inventory WHERE username='" + player.getName() + "' AND gamemode='" + player.getGameMode().toString() + "' AND world='" + player.getWorld().getName() + "'");
			}
		}
		if(skip){
			return;
		}
		File sdir = new File(plugin.getDataFolder(), "inventories");
		sdir.mkdirs();
		File saveFile = new File(sdir, player.getName() + "_" + player.getGameMode().toString() + "_" + player.getWorld().getName() + ".yml");
		if(saveFile.exists()){
			saveFile.delete();
			try{
				saveFile.createNewFile();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
