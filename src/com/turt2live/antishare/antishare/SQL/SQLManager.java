package com.turt2live.antishare.antishare.SQL;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.turt2live.antishare.AntiShare;

// TODO: Finish SQL class

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

	public void backup(){
		// TODO: Backup
		try{
			// Create the statement
			Statement stmt = connection.createStatement();

			// Export the data
			String filename = "c:\\\\temp\\\\outfile.txt";
			String tablename = "mysql_2_table";
			stmt.executeUpdate("SELECT * INTO OUTFILE \"" + filename + "\" FROM " + tablename);
		}catch(SQLException e){}
	}

	public void checkConnection(){
		if(connection == null){
			attemptConnectFromConfig();
		}
	}

	// TODO this
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
				"  PRIMARY KEY (`id`)" +
				") ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1");
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
				"  PRIMARY KEY (`id`)" +
				") ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1");
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
		// CREATE 
	}

	public void createTable(String name, String[] values, String key){
		// Build statement and call createQuery();
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
		return connection == null;
	}

	public void restore(File SQLFile){
		// TODO: Restore
		try{
			// Create the statement
			Statement stmt = connection.createStatement();

			// Load the data
			String filename = "c:\\\\temp\\\\infile.txt";
			String tablename = "mysql_2_table";
			stmt.executeUpdate("LOAD DATA INFILE \"" + filename + "\" INTO TABLE " + tablename);

			// If the file is comma-separated, use this statement
			stmt.executeUpdate("LOAD DATA INFILE \"" + filename + "\" INTO TABLE "
					+ tablename + " FIELDS TERMINATED BY ','");

			// If the file is terminated by \r\n, use this statement
			stmt.executeUpdate("LOAD DATA INFILE \"" + filename + "\" INTO TABLE "
					+ tablename + " LINES TERMINATED BY '\\r\\n'");
		}catch(SQLException e){}
	}

	public boolean tableExists(String tablename){
		return false;
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
