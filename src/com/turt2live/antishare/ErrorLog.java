package com.turt2live.antishare;

import java.io.File;
import java.io.PrintStream;

/**
 * Logs errors
 * 
 * @author turt2live
 */
public class ErrorLog {

	/**
	 * Log an error
	 * 
	 * @param e the error
	 */
	public static String print(Exception e){
		AntiShare plugin = AntiShare.getInstance();
		File folder = new File(plugin.getDataFolder(), "errors");
		folder.mkdirs();
		File file = new File(folder, "error_" + ASUtils.timestamp() + ".err");
		try{
			if(!file.exists()){
				file.createNewFile();
			}
			PrintStream ps = new PrintStream(file);
			e.printStackTrace(ps);
			ps.close();
			return file.getAbsolutePath();
		}catch(Exception e1){
			plugin.getLogger().severe("Exception occured in Error Log. Please report to turt2live.");
			return "<FILE NOT SAVED>";
		}
	}

}
