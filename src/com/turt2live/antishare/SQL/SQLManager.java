package com.turt2live.antishare.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.debug.Bug;
import com.turt2live.antishare.debug.Debugger;

/*
 * Thanks go to Example Depot for teaching me everything in this class.
 * http://www.exampledepot.com/egs/java.sql/pkg.html
 */
public class SQLManager {

	private AntiShare plugin;
	private Connection connection;

	public SQLManager(AntiShare plugin){
		this.plugin = plugin;
	}

	public boolean attemptConnectFromConfig(){
		String host = plugin.getConfig().getString("SQL.host");
		String username = plugin.getConfig().getString("SQL.username");
		String password = plugin.getConfig().getString("SQL.password");
		int port = plugin.getConfig().getInt("SQL.port");
		String database = plugin.getConfig().getString("SQL.database");
		return connect(host, username, password, port, database);
	}

	public boolean connect(String host, String username, String password, int port, String database){
		try{
			String driverName = "org.gjt.mm.mysql.Driver";
			Class.forName(driverName);
			String url = "jdbc:mysql://" + host + "/" + database;
			connection = DriverManager.getConnection(url, username, password);
			return true;
		}catch(ClassNotFoundException e){
			plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "You do not have a MySQL driver, please install one. AntiShare will use Flat-File for now");
		}catch(SQLException e){
			plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "Cannot connect to SQL! Check your settings. AntiShare will use Flat-File for now");
		}
		return false;
	}

	public void disconnect(){
		if(connection != null){
			try{
				if(!connection.isClosed()){
					connection.close();
				}
			}catch(SQLException e){
				Bug bug = new Bug(e, "Cannot close SQL", this.getClass(), null);
				Debugger.sendBug(bug);
				plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "Cannot close SQL connection: " + e.getMessage());
			}
		}
	}

	public void checkConnection(){
		if(connection == null){
			attemptConnectFromConfig();
		}
	}

	public boolean isConnected(){
		return connection != null;
	}

	public void checkValues(){
		if(!isConnected()){
			return;
		}
		createQuery("CREATE TABLE IF NOT EXISTS `AntiShare_Inventory` (" +
				"  `id` int(11) NOT NULL AUTO_INCREMENT," +
				"  `username` varchar(20) NOT NULL," +
				"  `gamemode` varchar(25) NOT NULL," +
				"  `slot` int(11) NOT NULL," +
				"  `itemID` int(11) NOT NULL," +
				"  `itemName` varchar(25) NOT NULL," +
				"  `itemDurability` int(11) NOT NULL," +
				"  `itemAmount` int(11) NOT NULL," +
				"  `itemData` int(11) NOT NULL," +
				"  `itemEnchant` varchar(100) NOT NULL," +
				"  `world` varchar(100) NOT NULL," +
				"  PRIMARY KEY (`id`)" +
				")");
		createQuery("CREATE TABLE IF NOT EXISTS `AntiShare_Regions` (" +
				"  `id` int(11) NOT NULL AUTO_INCREMENT," +
				"  `regionName` varchar(255) NOT NULL," +
				"  `mix` decimal(11,25) NOT NULL," +
				"  `miy` decimal(11,25) NOT NULL," +
				"  `miz` decimal(11,25) NOT NULL," +
				"  `max` decimal(11,25) NOT NULL," +
				"  `may` decimal(11,25) NOT NULL," +
				"  `maz` decimal(11,25) NOT NULL," +
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
		createQuery("CREATE TABLE IF NOT EXISTS `AntiShare_RegionInfo` (" +
				"  `id` int(11) NOT NULL AUTO_INCREMENT," +
				"  `player` varchar(255) NOT NULL," +
				"  `region` varchar(255) NOT NULL," +
				"  `gamemode` varchar(255) NOT NULL," +
				"  PRIMARY KEY (`id`)" +
				")");
		createQuery("CREATE TABLE IF NOT EXISTS `AntiShare_MiscInventory` (" +
				"  `id` int(11) NOT NULL AUTO_INCREMENT," +
				"  `uniqueID` varchar(255) NOT NULL," +
				"  `slot` int(11) NOT NULL," +
				"  `itemID` int(11) NOT NULL," +
				"  `itemName` varchar(25) NOT NULL," +
				"  `itemDurability` int(11) NOT NULL," +
				"  `itemAmount` int(11) NOT NULL," +
				"  `itemData` int(11) NOT NULL," +
				"  `itemEnchant` varchar(100) NOT NULL," +
				"  PRIMARY KEY (`id`)" +
				")");
	}

	public String getDatabase(){
		return plugin.getConfig().getString("SQL.database");
	}

	public void createQuery(String query){
		updateQuery(query);
	}

	public int deleteQuery(String query){
		return updateQuery(query);
	}

	public ResultSet getQuery(String query){
		try{
			Statement stmt = connection.createStatement();
			return stmt.executeQuery(query);
		}catch(SQLException e){
			Bug bug = new Bug(e, "QUERY: " + query, this.getClass(), null);
			Debugger.sendBug(bug);
			plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "Something went wrong with the query. Send this to the developer:");
			plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "QUERY: " + query);
			plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "MESSAGE: " + e.getMessage());
		}
		return null;
	}

	public void insertQuery(String query){
		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
		}catch(SQLException e){
			Bug bug = new Bug(e, "QUERY: " + query, this.getClass(), null);
			Debugger.sendBug(bug);
			plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "Something went wrong with the query. Send this to the developer:");
			plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "QUERY: " + query);
			plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "MESSAGE: " + e.getMessage());
		}
	}

	public int updateQuery(String query){
		try{
			Statement stmt = connection.createStatement();
			return stmt.executeUpdate(query);
		}catch(SQLException e){
			Bug bug = new Bug(e, "QUERY: " + query, this.getClass(), null);
			Debugger.sendBug(bug);
			plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "Something went wrong with the query. Send this to the developer:");
			plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "QUERY: " + query);
			plugin.log.severe("[" + plugin.getDescription().getVersion() + "] " + "MESSAGE: " + e.getMessage());
		}
		return 0;
	}
}
