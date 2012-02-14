package com.turt2live.antishare.antishare.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.turt2live.antishare.AntiShare;

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

	public void checkConnection(){
		if(connection == null){
			attemptConnectFromConfig();
		}
	}

	public void checkValues(){
		if(!isConnected()){
			return;
		}
		createQuery("CREATE TABLE IF NOT EXISTS `AntiShare_Blocks` (" +
				"  `id` int(11) NOT NULL AUTO_INCREMENT," +
				"  `username_placer` varchar(20) NOT NULL," +
				"  `blockX` int(11) NOT NULL," +
				"  `blockY` int(11) NOT NULL," +
				"  `blockZ` int(11) NOT NULL," +
				"  `blockID` int(11) NOT NULL," +
				"  `blockName` varchar(25) NOT NULL," +
				"  `world` varchar(100) NOT NULL," +
				"  PRIMARY KEY (`id`)" +
				")");
		createQuery("CREATE TABLE IF NOT EXISTS `AntiShare_Inventories` (" +
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
	}

	public boolean connect(String host, String username, String password, int port, String database){
		try{
			String driverName = "org.gjt.mm.mysql.Driver";
			Class.forName(driverName);
			String url = "jdbc:mysql://" + host + "/" + database;
			connection = DriverManager.getConnection(url, username, password);
			return true;
		}catch(ClassNotFoundException e){
			plugin.log.severe("[" + plugin.getDescription().getFullName() + "] You do not have a MySQL driver, please install one. AntiShare will use Flat-File for now");
		}catch(SQLException e){
			plugin.log.severe("[" + plugin.getDescription().getFullName() + "] Cannot connect to SQL! Check your settings. AntiShare will use Flat-File for now");
		}
		return false;
	}

	public void createQuery(String query){
		updateQuery(query);
	}

	public void disconnect(){
		if(connection != null){
			try{
				if(!connection.isClosed()){
					connection.close();
				}
			}catch(SQLException e){
				plugin.log.severe("[" + plugin.getDescription().getFullName() + "] Cannot close SQL connection: " + e.getMessage());
			}
		}
	}

	public String getDatabase(){
		return plugin.getConfig().getString("SQL.database");
	}

	public ResultSet getQuery(String query){
		try{
			Statement stmt = connection.createStatement();
			return stmt.executeQuery(query);
		}catch(SQLException e){
			plugin.log.severe("[" + plugin.getDescription().getFullName() + "] Something went wrong with the query. Send this to the developer:");
			plugin.log.severe("[" + plugin.getDescription().getFullName() + "] QUERY: " + query);
		}
		return null;
	}

	public void insertQuery(String query){
		try{
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(query);
		}catch(SQLException e){
			plugin.log.severe("[" + plugin.getDescription().getFullName() + "] Something went wrong with the query. Send this to the developer:");
			plugin.log.severe("[" + plugin.getDescription().getFullName() + "] QUERY: " + query);
		}
	}

	public boolean isConnected(){
		return connection != null;
	}

	/**
	 * Returns number of rows updated
	 * 
	 * @param query query string
	 * @return rows updated
	 */
	public int updateQuery(String query){
		try{
			Statement stmt = connection.createStatement();
			return stmt.executeUpdate(query);
		}catch(SQLException e){
			plugin.log.severe("[" + plugin.getDescription().getFullName() + "] Something went wrong with the query. Send this to the developer:");
			plugin.log.severe("[" + plugin.getDescription().getFullName() + "] QUERY: " + query);
		}
		return 0;
	}
}
