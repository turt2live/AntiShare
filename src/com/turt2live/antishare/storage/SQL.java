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
package com.turt2live.antishare.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.lib.patpeter.sqllibrary.Database;
import com.turt2live.antishare.lib.patpeter.sqllibrary.DatabaseConfig;
import com.turt2live.antishare.lib.patpeter.sqllibrary.DatabaseConfig.DatabaseType;
import com.turt2live.antishare.lib.patpeter.sqllibrary.DatabaseConfig.Parameter;
import com.turt2live.antishare.lib.patpeter.sqllibrary.DatabaseFactory;
import com.turt2live.antishare.lib.patpeter.sqllibrary.InvalidConfiguration;

/**
 * SQL Manager
 * 
 * @author turt2live
 */
public class SQL {

	public static final String REGIONS_TABLE = "AS_Regions";
	public static final String INVENTORIES_TABLE = "AS_Inventories";

	private AntiShare plugin;
	private String database = "";
	private DatabaseConfig config;
	private Database sql;

	/**
	 * Creates a new SQL Manager
	 */
	public SQL(){
		this.plugin = AntiShare.getInstance();
	}

	/**
	 * Attempts a connection
	 * 
	 * @param host the hostname
	 * @param username the username
	 * @param password the password
	 * @param database the database
	 * @param port the port number (default is 3306 for MySQL)
	 * @return true if connected
	 */
	public boolean connect(String host, String username, String password, String database, String port){
		// Setup configuration
		config = new DatabaseConfig();
		config.setType(DatabaseType.MYSQL);
		try{
			config.setParameter(Parameter.HOSTNAME, host);
			config.setParameter(Parameter.PORT_NUMBER, port);
			config.setParameter(Parameter.DATABASE, database);
			config.setParameter(Parameter.USER, username);
			config.setParameter(Parameter.PASSWORD, password);
		}catch(InvalidConfiguration e){
			plugin.getLogger().warning("Cannot connect to SQL! Check your settings. AntiShare will use Flat-File for now");
			return false;
		}

		// Connect
		try{
			sql = DatabaseFactory.createDatabase(config);
		}catch(InvalidConfiguration e){
			plugin.getLogger().warning("Cannot connect to SQL! Check your settings. AntiShare will use Flat-File for now");
			return false;
		}

		return true; // All went well, we hope
	}

	/**
	 * Disconnects from the SQL server
	 */
	public void disconnect(){
		if(sql != null){
			sql.close();
		}
	}

	/**
	 * Reconnects to the SQL server
	 */
	public void reconnect(){
		disconnect();
		try{
			sql = DatabaseFactory.createDatabase(config);
		}catch(InvalidConfiguration e){
			plugin.getLogger().warning("Cannot connect to SQL! Check your settings. AntiShare will use Flat-File for now");
		}
	}

	/**
	 * Checks for a connection
	 * 
	 * @return true if connected
	 */
	public boolean isConnected(){
		return sql != null && sql.checkConnection();
	}

	/**
	 * Gets the SQL database in use
	 * 
	 * @return the database name
	 */
	public String getDatabase(){
		return database;
	}

	/**
	 * Executes setup on the SQL server
	 */
	public void setup(){
		if(!isConnected()){
			reconnect();
		}
		update("CREATE TABLE IF NOT EXISTS `" + INVENTORIES_TABLE + "` (" +
				"  `id` int(11) NOT NULL AUTO_INCREMENT," +
				"  `type` varchar(25) NOT NULL, " +
				"  `name` varchar(50) NOT NULL," +
				"  `gamemode` varchar(25) NOT NULL," +
				"  `world` varchar(100) NOT NULL," +
				"  `slot` int(11) NOT NULL," +
				"  `itemID` int(11) NOT NULL," +
				"  `itemName` varchar(25) NOT NULL," +
				"  `itemDurability` int(11) NOT NULL," +
				"  `itemAmount` int(11) NOT NULL," +
				"  `itemData` int(11) NOT NULL," +
				"  `itemEnchant` text NOT NULL," +
				"  PRIMARY KEY (`id`)" +
				")");
		update("CREATE TABLE IF NOT EXISTS `" + REGIONS_TABLE + "` (" +
				"  `id` int(11) NOT NULL AUTO_INCREMENT," +
				"  `regionName` varchar(255) NOT NULL," +
				"  `mix` decimal(25,4) NOT NULL," +
				"  `miy` decimal(25,4) NOT NULL," +
				"  `miz` decimal(25,4) NOT NULL," +
				"  `max` decimal(25,4) NOT NULL," +
				"  `may` decimal(25,4) NOT NULL," +
				"  `maz` decimal(25,4) NOT NULL," +
				"  `creator` varchar(25) NOT NULL," +
				"  `gamemode` varchar(25) NOT NULL," +
				"  `showEnter` int(11) NOT NULL," +
				"  `showExit` int(11) NOT NULL," +
				"  `world` varchar(100) NOT NULL," +
				"  `uniqueID` varchar(100) NOT NULL," +
				"  `enterMessage` varchar(300) NOT NULL," +
				"  `exitMessage` varchar(300) NOT NULL," +
				"  PRIMARY KEY (`id`)" +
				")");
	}

	/**
	 * Wipes a table
	 * 
	 * @param tablename the table name
	 */
	public void wipeTable(String tablename){
		if(!isConnected()){
			reconnect();
		}
		update("DELETE FROM " + tablename);
	}

	public int update(String query){
		try{
			return sql.prepare(query).executeUpdate();
		}catch(SQLException e){
			AntiShare.getInstance().log("AntiShare encountered and error. Please report this to turt2live.", Level.SEVERE);
			e.printStackTrace();
		}
		return 0;
	}

	public ResultSet get(String query){
		try{
			return sql.prepare(query).executeQuery();
		}catch(SQLException e){
			AntiShare.getInstance().log("AntiShare encountered and error. Please report this to turt2live.", Level.SEVERE);
			e.printStackTrace();
		}
		return null;
	}

}
