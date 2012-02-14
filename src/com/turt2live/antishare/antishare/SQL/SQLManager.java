package com.turt2live.antishare.antishare.SQL;

import com.turt2live.antishare.AntiShare;

// TODO: Finish SQL class
public class SQLManager {

	private AntiShare plugin;

	public SQLManager(AntiShare plugin){
		this.plugin = plugin;
	}

	public boolean attemptConnectFromConfig(){
		String host = plugin.getConfig().getString("SQL.host");
		String username = plugin.getConfig().getString("SQL.username");
		String password = plugin.getConfig().getString("SQL.password");
		int port = plugin.getConfig().getInt("SQL.port");
		return connect(host, username, password, port);
	}

	public void checkConnection(){

	}

	public void checkValues(){
		if(!isConnected()){
			return;
		}
		String database = plugin.getConfig().getString("SQL.database");
		setDatabase(database);
		if(!tableExists("AntiShare_Inventories")){
			String[] values = {
					"id INT"
			};
			createTable("AntiShare_Inventories", values, "id");
		}
		if(!tableExists("AntiShare_Blocks")){
			String[] values = {
					"id INT"
			};
			createTable("AntiShare_Blocks", values, "id");
		}
	}

	public boolean connect(String host, String username, String password, int port){
		return false;
	}

	public void createQuery(String query){
	// CREATE 
	}

	public void createTable(String name, String[] values, String key){
	// Build statement and call createQuery();
	}

	public boolean databaseExists(String database){
		return false;
	}

	public void disconnect(){

	}

	public String getDatabase(){
		// TODO: Make this return the currently connected DB
		return plugin.getConfig().getString("SQL.database");
	}

	public void getQuery(String query){
	// SELECT
	}

	public boolean isConnected(){
		return false;
	}

	public void setDatabase(String database){
	// Connect and create (if required) DB
	}

	public boolean tableExists(String tablename){
		return false;
	}

	public void updateQuery(String query){
	// UPDATE 
	}
}
