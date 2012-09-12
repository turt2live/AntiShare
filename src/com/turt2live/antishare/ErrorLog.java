/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare;

import java.io.File;
import java.io.PrintStream;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import com.turt2live.antishare.api.ErrorFile;

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
		if(e instanceof SQLException){
			e.printStackTrace();
			return "<SQL Error not saved>";
		}
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
			ErrorFile err = new ErrorFile(file);
			err.save(Bukkit.getConsoleSender());
			err.alert();
			return file.getAbsolutePath();
		}catch(Exception e1){
			plugin.getLogger().severe("Exception occured in Error Log. Please report to turt2live.");
			return "<FILE NOT SAVED>";
		}
	}

}
