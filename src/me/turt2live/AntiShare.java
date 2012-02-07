package me.turt2live;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.feildmaster.lib.configuration.PluginWrapper;

public class AntiShare extends PluginWrapper {

	public static String addColor(String message){
		String colorSeperator = "&";
		message = message.replaceAll(colorSeperator + "0", ChatColor.getByChar('0').toString());
		message = message.replaceAll(colorSeperator + "1", ChatColor.getByChar('1').toString());
		message = message.replaceAll(colorSeperator + "2", ChatColor.getByChar('2').toString());
		message = message.replaceAll(colorSeperator + "3", ChatColor.getByChar('3').toString());
		message = message.replaceAll(colorSeperator + "4", ChatColor.getByChar('4').toString());
		message = message.replaceAll(colorSeperator + "5", ChatColor.getByChar('5').toString());
		message = message.replaceAll(colorSeperator + "6", ChatColor.getByChar('6').toString());
		message = message.replaceAll(colorSeperator + "7", ChatColor.getByChar('7').toString());
		message = message.replaceAll(colorSeperator + "8", ChatColor.getByChar('8').toString());
		message = message.replaceAll(colorSeperator + "9", ChatColor.getByChar('9').toString());
		message = message.replaceAll(colorSeperator + "a", ChatColor.getByChar('a').toString());
		message = message.replaceAll(colorSeperator + "b", ChatColor.getByChar('b').toString());
		message = message.replaceAll(colorSeperator + "c", ChatColor.getByChar('c').toString());
		message = message.replaceAll(colorSeperator + "d", ChatColor.getByChar('d').toString());
		message = message.replaceAll(colorSeperator + "e", ChatColor.getByChar('e').toString());
		message = message.replaceAll(colorSeperator + "f", ChatColor.getByChar('f').toString());
		message = message.replaceAll(colorSeperator + "A", ChatColor.getByChar('a').toString());
		message = message.replaceAll(colorSeperator + "B", ChatColor.getByChar('b').toString());
		message = message.replaceAll(colorSeperator + "C", ChatColor.getByChar('c').toString());
		message = message.replaceAll(colorSeperator + "D", ChatColor.getByChar('d').toString());
		message = message.replaceAll(colorSeperator + "E", ChatColor.getByChar('e').toString());
		message = message.replaceAll(colorSeperator + "F", ChatColor.getByChar('f').toString());
		return message;
	}

	public static File getSaveFolder(){
		Plugin plugin = Bukkit.getPluginManager().getPlugin("AntiShare");
		return plugin.getDataFolder();
	}

	public static boolean isBlocked(String message, int id){
		boolean ret = false;
		if(message.equalsIgnoreCase("none")){
			return false;
		}else if(message.equalsIgnoreCase("*")){
			return true;
		}
		String parts[] = message.split(" ");
		String item = id + "";
		for(String s : parts){
			//System.out.println("ITEM: " + s);
			if(s.equalsIgnoreCase(item)){
				ret = true;
				break;
			}
		}
		return ret;
	}

	private AntiShareListener listener;

	private ASConfig config;

	public Logger log = Logger.getLogger("Minecraft");

	public ASConfig config(){
		return config;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args){
		if(sender instanceof Player){
			if(((Player) sender).hasPermission("AntiShare.reload")){
				if(cmd.equalsIgnoreCase("antishare") ||
						cmd.equalsIgnoreCase("as") ||
						cmd.equalsIgnoreCase("antis") ||
						cmd.equalsIgnoreCase("ashare")){
					config.reload();
					((Player) sender).sendMessage(ChatColor.GREEN + "AntiShare Reloaded.");
				}
			}
			return false;
		}else{
			if(cmd.equalsIgnoreCase("antishare") ||
					cmd.equalsIgnoreCase("as") ||
					cmd.equalsIgnoreCase("antis") ||
					cmd.equalsIgnoreCase("ashare")){
				reloadConfig();
				log.info("AntiShare Reloaded.");
			}
			return false;
		}
	}

	@Override
	public void onDisable(){
		listener = null;
		log.info("[" + getDescription().getFullName() + "] Disabled! (turt2live)");
	}

	@Override
	public void onEnable(){
		config = new ASConfig(this);
		config.create();
		config.reload();
		listener = new AntiShareListener(this);
		listener.init();
		log.info("[" + getDescription().getFullName() + "] Enabled! (turt2live)");
	}
}
