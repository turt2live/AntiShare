package com.turt2live.antishare;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ASUtils {
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
			if(s.equalsIgnoreCase(item)){
				ret = true;
				break;
			}
		}
		return ret;
	}

	public static void sendToPlayer(CommandSender target, String message){
		if(!message.equalsIgnoreCase("nomsg")
				&& !message.equalsIgnoreCase("no message")
				&& !message.equalsIgnoreCase("none")
				&& !message.equalsIgnoreCase("noshow")
				&& !message.equalsIgnoreCase("no show")){
			message = addColor(message);
			target.sendMessage(message);
		}
	}

	public static void transfer(File original, File destination){
		try{
			if(!destination.exists()){
				File d = new File(destination.getParent());
				d.mkdirs();
				destination.createNewFile();
			}
			InputStream in = new FileInputStream(original);
			OutputStream out = new FileOutputStream(destination);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
